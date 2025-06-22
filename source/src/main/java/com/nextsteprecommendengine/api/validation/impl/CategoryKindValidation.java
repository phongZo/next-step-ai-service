package com.nextsteprecommendengine.api.validation.impl;

import com.nextsteprecommendengine.api.constant.NextStepRecommendengineConstant;
import com.nextsteprecommendengine.api.validation.CategoryKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CategoryKindValidation implements ConstraintValidator<CategoryKind, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(CategoryKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer kind, ConstraintValidatorContext constraintValidatorContext) {
        if (kind == null && allowNull) {
            return true;
        }
        if (!Objects.equals(kind, NextStepRecommendengineConstant.CATEGORY_KIND_NEWS)) {
            return false;
        }
        return true;
    }
}
