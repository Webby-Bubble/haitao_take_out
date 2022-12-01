package org.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.reggie.entity.Category;
import org.example.reggie.entity.Employee;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
