package com.trilogyed.musicstorerecommendation.repository;


import com.trilogyed.musicstorerecommendation.model.LabelRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRecommendationRepository extends JpaRepository<LabelRecommendation, Integer> {

}
