package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CategoryDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.CategoryMapper;
import com.ccpc.yeprogress.model.Category;
import com.ccpc.yeprogress.repository.CategoryRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerService.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ValidationService validationService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ValidationService validationService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.validationService = validationService;
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        LoggerService.logCreateAttempt(logger, "Category", categoryDTO.getName());

        try {
            validationService.validateCategoryDTO(categoryDTO);

            Category category = categoryMapper.toEntity(categoryDTO);
            Category savedCategory = categoryRepository.save(category);
            LoggerService.logCreateSuccess(logger, "Category", savedCategory.getCategoryId());

            return categoryMapper.toDto(savedCategory);

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Validation failed for category creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "category creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create category due to unexpected error", e);
        }
    }

    public CategoryDTO getCategoryById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "Category", id);

        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Category", id);
                        return new UserNotFoundException("Category with ID " + id + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "Category", id);
            return categoryMapper.toDto(category);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving category: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "category retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve category due to unexpected error", e);
        }
    }

    public List<CategoryDTO> getAllCategories() {
        LoggerService.logRetrieveAttempt(logger, "All Categories", "all");

        try {
            List<Category> categories = categoryRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All Categories", categories.size());

            return categories.stream()
                    .map(categoryMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all categories", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve categories due to unexpected error", e);
        }
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        LoggerService.logUpdateAttempt(logger, "Category", id);

        try {
            validationService.validateCategoryDTO(categoryDTO);

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Category", id);
                        return new UserNotFoundException("Category with ID " + id + " not found");
                    });

            categoryMapper.updateEntityFromDto(categoryDTO, category);
            Category savedCategory = categoryRepository.save(category);
            LoggerService.logUpdateSuccess(logger, "Category", id);

            return categoryMapper.toDto(savedCategory);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating category: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "category update", e.getMessage(), e);
            throw new RuntimeException("Failed to update category due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        LoggerService.logDeleteAttempt(logger, "Category", id);

        try {
            if (!categoryRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "Category", id);
                throw new UserNotFoundException("Category with ID " + id + " not found");
            }

            categoryRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "Category", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting category: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "category deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete category due to unexpected error", e);
        }
    }
}