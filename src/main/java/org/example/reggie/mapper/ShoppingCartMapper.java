package org.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.reggie.entity.ShoppingCart;
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
