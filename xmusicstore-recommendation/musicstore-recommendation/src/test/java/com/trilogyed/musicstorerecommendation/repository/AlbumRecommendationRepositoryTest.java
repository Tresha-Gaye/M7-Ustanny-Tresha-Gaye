package com.trilogyed.musicstorerecommendation.repository;

import com.trilogyed.musicstorerecommendation.model.AlbumRecommendation;
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
public class AlbumRecommendationRepositoryTest {

    @Autowired
    AlbumRecommendationRepository repo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
    }

    @Test
    public void addGetDeleteAlbumRecommendation() {

        AlbumRecommendation album = new AlbumRecommendation();
        album.setAlbumId(1);
        album.setUserId(1);
        album.setLiked(true);

        album = repo.save(album);

        Optional<AlbumRecommendation> found = repo.findById(album.getId());

        assertEquals(found.get(), album);

        repo.deleteById(album.getId());

        found = repo.findById(album.getId());

        assertFalse(found.isPresent());
    }

    @Test
    public void updateAlbumRecommendation() {

        AlbumRecommendation album = new AlbumRecommendation();
        album.setId(1);
        album.setAlbumId(1);
        album.setUserId(1);
        album.setLiked(true);

        album = repo.save(album);

        AlbumRecommendation updated = new AlbumRecommendation();
        updated.setId(1);
        updated.setAlbumId(1);
        updated.setUserId(1);
        updated.setLiked(false);

        updated = repo.save(album);

        Optional<AlbumRecommendation> found = repo.findById(updated.getId());
        assertEquals(found.get(), album);
    }

    @Test
    public void getAllAlbumRecommendations() {

        AlbumRecommendation album1 = new AlbumRecommendation();
        album1.setId(1);
        album1.setAlbumId(1);
        album1.setUserId(1);
        album1.setLiked(true);

        repo.save(album1);

        AlbumRecommendation album2 = new AlbumRecommendation();
        album2.setId(2);
        album2.setAlbumId(2);
        album2.setUserId(2);
        album2.setLiked(false);

        repo.save(album2);

        List<AlbumRecommendation> aList = repo.findAll();
        assertEquals(aList.size(), 2);

    }

}