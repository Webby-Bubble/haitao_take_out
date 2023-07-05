package org.example.haitao.dto;

import lombok.Data;
import org.example.haitao.entity.Setmeal;
import org.example.haitao.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
