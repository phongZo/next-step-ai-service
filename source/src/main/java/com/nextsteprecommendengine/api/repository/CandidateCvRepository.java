package com.nextsteprecommendengine.api.repository;

import com.nextsteprecommendengine.api.model.CandidateCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CandidateCvRepository extends JpaRepository<CandidateCv, Long>, JpaSpecificationExecutor<CandidateCv> {
}
