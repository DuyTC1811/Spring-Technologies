package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.ICategoryHandler;
import org.example.springsecurity.models.CategoryInfo;
import org.example.springsecurity.responses.CategoryListResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "CATEGORIES", description = "API CATEGORY")
public class CategoryController {
    private final ICategoryHandler categoryHandler;

    @PostMapping("/create")
//    public CategoryResponse create(@Valid @RequestBody CreateCategoryRequest request) {
////        return categoryHandler.create(request);
//    }

//    @PutMapping("/update/{categoryId}")
//    public CategoryResponse update(
//            @PathVariable String categoryId,
//            @Valid @RequestBody UpdateCategoryRequest request
//    ) {
//        return categoryService.update(categoryId, request);
//    }

//    @DeleteMapping("/delete/{categoryId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable String categoryId) {
//        categoryService.delete(categoryId);
//    }

    @GetMapping("/info/{categoryId}")
    public ResponseEntity<CategoryInfo> getInfo(String categoryId) {
        CategoryInfo category = categoryHandler.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<CategoryListResp> getList(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {
        List<CategoryInfo> categories = categoryHandler.getCategories(limit, offset);
        CategoryListResp resp = new CategoryListResp(categories);
        return ResponseEntity.ok(resp);
    }
}
