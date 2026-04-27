package org.example.springsecurity.handlers.impl;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity.handlers.ICategoryHandler;
import org.example.springsecurity.mappers.ICategoryMapper;
import org.example.springsecurity.models.CategoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryHandlerImpl implements ICategoryHandler {
    private final ICategoryMapper categoryMapper;

    @Override
    public List<CategoryInfo> getCategories(int limit, int offset) {
        return categoryMapper.findList(limit, offset);
    }

    @Override
    public CategoryInfo getCategoryById(String categoryId) {
        return categoryMapper.findById(categoryId);
    }

    @Override
    public String createCategory() {
        return "";
    }

    @Override
    public void updateCategory(String categoryId, CategoryInfo categoryInfo) {
        // categoryMapper.update(categoryId, categoryInfo);
    }

    @Override
    public void deleteCategory(String categoryId) {
        categoryMapper.softDelete(categoryId);
    }
}
