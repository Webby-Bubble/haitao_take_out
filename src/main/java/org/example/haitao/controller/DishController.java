package org.example.haitao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.haitao.common.R;
import org.example.haitao.dto.DishDto;
import org.example.haitao.entity.Category;
import org.example.haitao.entity.Dish;
import org.example.haitao.entity.DishFlavor;
import org.example.haitao.service.CategoryService;
import org.example.haitao.service.DishFlavorService;
import org.example.haitao.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){//同时传两个实体类
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //  构造分页构造器
        Page<Dish>pageInfo=new Page<>(page,pageSize);
        Page<DishDto>dishDtoPage=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish>queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById((categoryId));

            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto= dishService.getByIdWidthFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){//同时传两个实体类
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        //清理所有菜品缓存数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //精确清理，清理某个分类下面的菜品缓存
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
/*    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list=dishService.list(queryWrapper);

        return R.success(list);
    }*/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList=null;
        //动态构造key
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //先从redis中获取缓存数据
        dishDtoList= (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(dishDtoList!=null){
            //如果存在，直接返回，无需查询数据库，将查询到的彩屏数据缓存到Redis
            return R.success(dishDtoList);
        }

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list=dishService.list(queryWrapper);
        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品的id
            Long dishId=item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id=?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        //如果不存在，需要查询数据库，将车u难道的菜品数据缓存到Redis
        redisTemplate.opsForValue().set(key,dishDtoList,30, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
    @PostMapping("/status/{st}")
    public R<String> setStatus(@PathVariable int st, String ids){
        //处理string 转成Long
        String[] split = ids.split(",");
        List<Long> idList = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        //将每个id new出来一个Dish对象，并设置状态
        List<Dish> dishes = idList.stream().map((item) -> {
            Dish dish = new Dish();
            dish.setId(item);
            dish.setStatus(st);
            return dish;
        }).collect(Collectors.toList()); //Dish集合

        log.info("status ids : {}",ids);
        dishService.updateBatchById(dishes);//批量操作
        return R.success("操作成功");
    }

    /**
     * 根据id删除一个或批量删除菜品。
     *
     * @param ids 待删除的菜品id。
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long[] ids) {
        // 增加日志验证是否接收到前端参数。
        log.info("根据id删除一个菜品:{}", ids);
        // 根据ids中的id值删除所有的菜品。
        List<Long> idList = Arrays.asList(ids);
        // TODO: 2022/5/3 还要删除菜品所对应的口味记录
        // 根据id删除所对应的口味对象。
        for (int i = 0; i < ids.length; i++) {
            // 得到每个dish对象
            Long id = ids[i];
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            // 删除菜品对应口味记录。
            //      DishFlavor dishFlavor = dishFlavorService.getById(queryWrapper);
            dishFlavorService.remove(queryWrapper);
            //      System.out.println("删除对应口味是否成功："+b);
        }
        // 执行删除菜品。
        if (!dishService.removeByIds(idList)) {
            return R.success("删除菜品失败");
        }

        return R.success("删除菜品成功");
    }


}
