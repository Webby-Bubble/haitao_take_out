package org.example.haitao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.haitao.entity.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
