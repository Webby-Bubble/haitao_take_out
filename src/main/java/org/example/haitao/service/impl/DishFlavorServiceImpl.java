package org.example.haitao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.haitao.entity.DishFlavor;
import org.example.haitao.mapper.DishFlavorMapper;
import org.example.haitao.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
