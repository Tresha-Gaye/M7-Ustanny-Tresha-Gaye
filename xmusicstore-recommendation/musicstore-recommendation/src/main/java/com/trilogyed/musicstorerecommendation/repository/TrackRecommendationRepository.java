package com.trilogyed.musicstorerecommendation.repository;

import com.trilogyed.musicstorerecommendation.model.TrackRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRecommendationRepository extends JpaRepository<TrackRecommendation, Integer> {

}
