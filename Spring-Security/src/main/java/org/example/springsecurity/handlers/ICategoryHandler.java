package org.example.springsecurity.handlers;

import org.example.springsecurity.models.CategoryInfo;

import java.util.List;

public interface ICategoryHandler {
    List<CategoryInfo> getCategories(int limit, int offset);

    CategoryInfo getCategoryById(String categoryId);

    String createCategory();

    void updateCategory(String categoryId, CategoryInfo categoryInfo);

    void deleteCategory(String categoryId);
}
