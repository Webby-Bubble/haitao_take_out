package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.reggie.entity.AddressBook;
import org.example.reggie.entity.OrderDetail;
import org.example.reggie.mapper.AddressBookMapper;
import org.example.reggie.mapper.OrderDetailMapper;
import org.example.reggie.service.AddressBookService;
import org.example.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
