package com.example.kltn.services.common.iplm;


import com.example.kltn.exceptions.AppException;
import com.example.kltn.models.Category;
import com.example.kltn.repos.CategoryRepo;
import com.example.kltn.services.common.CategorySrv;
import com.example.kltn.utils.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryIplm implements CategorySrv {
    private final CategoryRepo categoryRepo;
    @Override
    public Category findById(String id) {
        Optional<Category> category = categoryRepo.findById(id);
        return category.orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category save(Category category) {
        if (category.getSubcategories()!=null && category.getSubcategories().size() > 0){
            category.getSubcategories().forEach(cate -> cate.setSlug(SlugGenerator.slugify(cate.getCategoryName())));
            categoryRepo.saveAll(category.getSubcategories());
        }
        category.setSlug(SlugGenerator.slugify(category.getCategoryName()));
        return categoryRepo.save(category);
    }

    @Override
    public void updateCategory(Category category) {
        Category categoryUpdate = findById(category.getId());

        if (categoryUpdate != null) {
            if (category.getCategoryName() != null) {
                categoryUpdate.setCategoryName(category.getCategoryName());
            }
            if (category.getSubcategories() != null){
                categoryRepo.saveAll(category.getSubcategories());
                categoryUpdate.getSubcategories().addAll(categoryUpdate.getSubcategories());
            }
            categoryRepo.save(categoryUpdate);
        } else throw new AppException(404, "Comment ID not found");
    }

    @Override
    public void deleteCategory(String categoryId) {
        categoryRepo.deleteById(categoryId);
    }
}


