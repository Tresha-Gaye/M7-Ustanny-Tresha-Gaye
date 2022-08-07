package com.trilogyed.musicstorerecommendation.repository;


import com.trilogyed.musicstorerecommendation.model.ArtistRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRecommendationRepository extends JpaRepository<ArtistRecommendation, Integer> {
}
