package com.nextsteprecommendengine.api.repository;

import com.nextsteprecommendengine.api.model.PostEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostEmbeddingRepository extends JpaRepository<PostEmbedding, Long>, JpaSpecificationExecutor<PostEmbedding> {
}
