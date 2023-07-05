package org.example.haitao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.haitao.common.R;
import org.example.haitao.entity.User;
import org.example.haitao.service.UserService;
import org.example.haitao.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信息验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("海涛点餐","",phone,code);
            //需要将生产的验证码保存到session
            ///session.setAttribute(phone,code);

            //将生成的验证码缓存到Redis中，并且设置有效期为五分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            //需要将生成的验证码
            return R.success("手机验证码短信发送成功");
        }

        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code=map.get("code").toString();
        //从session中获取保存的验证码
        //Object codeInSession = session.getAttribute(phone);

        //从Redis中获取保存的验证码
        Object codeInSession=redisTemplate.opsForValue().get(phone);
        //进行验证码比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession!=null&&codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user=userService.getOne(queryWrapper);
            if(user==null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户自动完成注册
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());

            //如果用户登录成功，删除Redis何种缓存的验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }
    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登陆用户的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
    /**
     * 客户信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        //执行查询
        userService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /**
     *根据id修改员工信息
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody User user){
        log.info(user.toString());
        long id=Thread.currentThread().getId();
        log.info("线程id为{}",id);
        userService.updateById(user);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        User user = userService.getById(id);
        if(user != null){
            return R.success(user);
        }
        return R.error("没有查询到对应员工信息");
    }


}
