package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public Category getById(int categoryId)
    {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        return category;
    }

    public Category create(Category category)
    {
        return categoryRepository.save(category);
    }

    public Category update(int categoryId, Category category)
    {
        Category toUpdate = categoryRepository.findById(categoryId).orElseThrow();

        if (!(category.getName() == null))
        {
            toUpdate.setName(category.getName());
        }

        if (!(category.getCategoryId() == 0))
        {
            toUpdate.setCategoryId(category.getCategoryId());
        }

        if (!(category.getName() == null))
        {
            toUpdate.setDescription(category.getDescription());
        }

        return toUpdate;
    }

    public void delete(int categoryId)
    {
        categoryRepository.deleteById(categoryId);
    }
}
