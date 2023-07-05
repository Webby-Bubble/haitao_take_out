package org.example.haitao.dto;

import lombok.Data;
import org.example.haitao.entity.Dish;
import org.example.haitao.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
