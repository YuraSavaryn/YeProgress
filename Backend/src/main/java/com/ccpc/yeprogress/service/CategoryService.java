package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CategoryDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.CategoryMapper;
import com.ccpc.yeprogress.model.Category;
import com.ccpc.yeprogress.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final int MAX_NAME_LENGTH = 50;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        logger.info("Attempting to create category with name: {}", categoryDTO.getName());

        try {
            validateCategoryDTO(categoryDTO);

            Category category = categoryMapper.toEntity(categoryDTO);
            Category savedCategory = categoryRepository.save(category);
            logger.info("Successfully created category with ID: {}", savedCategory.getCategoryId());

            return categoryMapper.toDto(savedCategory);

        } catch (UserValidationException e) {
            logger.error("Validation failed for category creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during category creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create category due to unexpected error", e);
        }
    }

    public CategoryDTO getCategoryById(Long id) {
        logger.info("Retrieving category with ID: {}", id);

        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category with ID {} not found", id);
                        return new UserNotFoundException("Category with ID " + id + " not found");
                    });

            logger.debug("Successfully retrieved category with ID: {}", id);
            return categoryMapper.toDto(category);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving category: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving category: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve category due to unexpected error", e);
        }
    }

    public List<CategoryDTO> getAllCategories() {
        logger.info("Retrieving all categories");

        try {
            List<Category> categories = categoryRepository.findAll();
            logger.debug("Retrieved {} categories", categories.size());

            return categories.stream()
                    .map(categoryMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all categories: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve categories due to unexpected error", e);
        }
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        logger.info("Attempting to update category with ID: {}", id);

        try {
            validateCategoryDTO(categoryDTO);

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category with ID {} not found", id);
                        return new UserNotFoundException("Category with ID " + id + " not found");
                    });

            categoryMapper.updateEntityFromDto(categoryDTO, category);
            Category savedCategory = categoryRepository.save(category);
            logger.info("Successfully updated category with ID: {}", id);

            return categoryMapper.toDto(savedCategory);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating category: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during category update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update category due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        logger.info("Attempting to delete category with ID: {}", id);

        try {
            if (!categoryRepository.existsById(id)) {
                logger.warn("Category deletion failed: ID {} not found", id);
                throw new UserNotFoundException("Category with ID " + id + " not found");
            }

            categoryRepository.deleteById(id);
            logger.info("Successfully deleted category with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting category: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during category deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete category due to unexpected error", e);
        }
    }

    private void validateCategoryDTO(CategoryDTO categoryDTO) {
        logger.debug("Validating category DTO");

        if (!StringUtils.hasText(categoryDTO.getName())) {
            logger.warn("Validation failed: Category name is required");
            throw new UserValidationException("Category name is required");
        }

        if (categoryDTO.getName().length() > MAX_NAME_LENGTH) {
            logger.warn("Validation failed: Category name exceeds {} characters", MAX_NAME_LENGTH);
            throw new UserValidationException(
                    "Category name must not exceed " + MAX_NAME_LENGTH + " characters");
        }

        if (!categoryDTO.getName().matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
            logger.warn("Validation failed: Category name contains invalid characters");
            throw new UserValidationException("Category name contains invalid characters");
        }
    }
}