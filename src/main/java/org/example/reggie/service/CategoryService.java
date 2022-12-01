package org.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.reggie.entity.Category;
import org.example.reggie.entity.Employee;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
