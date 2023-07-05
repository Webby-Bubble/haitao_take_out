package org.example.haitao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.haitao.entity.OrderDetail;
import org.example.haitao.mapper.OrderDetailMapper;
import org.example.haitao.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
