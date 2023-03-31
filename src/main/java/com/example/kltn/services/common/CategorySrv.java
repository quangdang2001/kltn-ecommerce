package com.example.kltn.services.common;


import com.example.kltn.models.Category;

import java.util.List;

public interface CategorySrv {

    Category findById(String id);
    List<Category> findAll();
    Category save(Category categoryDTO);
    void updateCategory(Category categoryDTO);
    void deleteCategory(String categoryId);
}
