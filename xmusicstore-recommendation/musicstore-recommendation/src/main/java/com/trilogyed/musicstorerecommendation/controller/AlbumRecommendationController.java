package com.trilogyed.musicstorerecommendation.controller;


import com.trilogyed.musicstorerecommendation.model.AlbumRecommendation;
import com.trilogyed.musicstorerecommendation.repository.AlbumRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AlbumRecommendationController {
    @Autowired
    AlbumRecommendationRepository albumRecRepo;

    @GetMapping("/album-likes")
    public List<AlbumRecommendation> getAlbums() {
        return albumRecRepo.findAll();
    }

    @GetMapping("/album-likes/{id}")
    public AlbumRecommendation getAlbumById(@PathVariable Integer id) throws Exception {

        Optional<AlbumRecommendation> foundAlbum = albumRecRepo.findById(id);

        if (foundAlbum.isPresent() == false ) {
            throw new IllegalArgumentException("We couldn't find that album");
        }
        return albumRecRepo.findById(id).get();
    }

    @PostMapping("/album-likes")
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumRecommendation createAlbum(@RequestBody AlbumRecommendation album) {
        return albumRecRepo.save(album);
    }

    @PutMapping("/album-likes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbum(@PathVariable Integer id, @RequestBody AlbumRecommendation album) {

        if (album.getId() == null) {
            album.setId(id);
        } else if (album.getId() != id) {
            throw new IllegalArgumentException("Id does not match");
        }
        albumRecRepo.save(album);
    }

    @PutMapping("/album-likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbum(@RequestBody AlbumRecommendation album) {

        albumRecRepo.save(album);
    }

    @DeleteMapping("/album-likes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable Integer id) {
        Optional<AlbumRecommendation> albumToDelete = albumRecRepo.findById(id);
        if (albumToDelete.isPresent() == false) {
            throw new IllegalArgumentException("No album with the id " + id);
        }
        albumRecRepo.deleteById(id);
    }
}