package com.example.kltn.services.common;


import com.example.kltn.dto.CategoryReq;
import com.example.kltn.models.Category;

import java.util.List;

public interface CategorySrv {

    Category findById(String id);
    List<Category> findAll();
    Category save(CategoryReq categoryReq);
    void updateCategory(Category categoryDTO);
    void deleteCategory(String categoryId);
}
