package com.example.kltn.controllers.customer;

import com.example.kltn.dto.CategoryReq;
import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.Category;
import com.example.kltn.services.common.CategorySrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategorySrv categoryService;

    //Get all categoty
    @GetMapping("/category")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(new ResponseDTO(true,"Success",categoryService.findAll()));
    }

    // Get category by ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> findById(@PathVariable String categoryId){
        Category category = categoryService.findById(categoryId);
        if (category!=null)
            return ResponseEntity.ok(new ResponseDTO(true,"Success",category));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(false,"Category ID not exits",null));
    }

    // Add Category
    //requied Name
    @PostMapping("/adminSys/category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryReq categoryReq){
        Category categorySave =  categoryService.save(categoryReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",categorySave));
    }

    // Update category
    @PutMapping("/adminSys/category")
    public ResponseEntity<?> updateCategory(@RequestBody Category categoryReq){
        categoryService.updateCategory(categoryReq);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }

    //Delete category
    @DeleteMapping("/adminSys/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id){
       categoryService.deleteCategory(id);
       return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
}
