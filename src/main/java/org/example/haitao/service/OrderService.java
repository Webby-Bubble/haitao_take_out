package org.example.haitao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.haitao.entity.Orders;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
