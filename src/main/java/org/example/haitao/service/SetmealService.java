package org.example.haitao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.haitao.dto.SetmealDto;
import org.example.haitao.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐喝菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * 删除套餐，同时删除菜品关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    SetmealDto getByIdWithDish(Long id);


    void updateWithDish(SetmealDto setmealDto);
}
