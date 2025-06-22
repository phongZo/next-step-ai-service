package com.nextsteprecommendengine.api.repository;

import com.nextsteprecommendengine.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Category findByName(String name);
}
