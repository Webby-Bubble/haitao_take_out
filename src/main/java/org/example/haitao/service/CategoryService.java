package org.example.haitao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.haitao.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
