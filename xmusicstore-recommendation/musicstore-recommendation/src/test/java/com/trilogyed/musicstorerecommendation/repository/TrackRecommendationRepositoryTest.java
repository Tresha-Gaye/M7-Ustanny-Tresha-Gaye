package com.trilogyed.musicstorerecommendation.repository;

import com.trilogyed.musicstorerecommendation.model.TrackRecommendation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TrackRecommendationRepositoryTest {

    @Autowired
    TrackRecommendationRepository repo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
    }

    @Test
    public void addGetDeleteTrackRecommendation() {

        TrackRecommendation track = new TrackRecommendation();
        track.setTrackId(1);
        track.setUserId(1);
        track.setLiked(true);

        track = repo.save(track);

        Optional<TrackRecommendation> found = repo.findById(track.getId());

        assertEquals(found.get(), track);

        repo.deleteById(track.getId());

        found = repo.findById(track.getId());

        assertFalse(found.isPresent());
    }

    @Test
    public void updateTrackRecommendation() {

        TrackRecommendation track = new TrackRecommendation();
        track.setId(1);
        track.setTrackId(1);
        track.setUserId(1);
        track.setLiked(true);

        track = repo.save(track);

        TrackRecommendation updated = new TrackRecommendation();
        updated.setId(1);
        updated.setTrackId(1);
        updated.setUserId(1);
        updated.setLiked(false);

        updated = repo.save(track);

        Optional<TrackRecommendation> found = repo.findById(updated.getId());
        assertEquals(found.get(), track);
    }

    @Test
    public void getAllTrackRecommendations() {

        TrackRecommendation track1 = new TrackRecommendation();
        track1.setId(1);
        track1.setTrackId(1);
        track1.setUserId(1);
        track1.setLiked(true);

        track1=repo.save(track1);

        TrackRecommendation track2 = new TrackRecommendation();
        track2.setId(2);
        track2.setTrackId(2);
        track2.setUserId(2);
        track2.setLiked(false);

        track2=repo.save(track2);

        List<TrackRecommendation> trackList = repo.findAll();
        assertEquals(trackList.size(), 2);

    }

}