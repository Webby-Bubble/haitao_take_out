package org.example.haitao.dto;

import lombok.Data;
import org.example.haitao.entity.OrderDetail;
import org.example.haitao.entity.Orders;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

    private int sumNum;

}

