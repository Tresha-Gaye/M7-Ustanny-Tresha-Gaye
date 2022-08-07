package com.trilogyed.musicstorerecommendation.repository;

import com.trilogyed.musicstorerecommendation.model.AlbumRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRecommendationRepository extends JpaRepository<AlbumRecommendation, Integer> {

}
