package com.trilogyed.musicstorecatalog.controller;

import com.trilogyed.musicstorecatalog.model.Album;
import com.trilogyed.musicstorecatalog.repository.AlbumRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class AlbumController {
    @Autowired
    AlbumRepository albumRepository;

    @GetMapping("/album")
    public List<Album> getAlbums() {
        return albumRepository.findAll();
    }

    @GetMapping("/album/{id}")
    public Album getAlbumById(@PathVariable Integer id) throws Exception {

        Optional<Album> foundAlbum = albumRepository.findById(id);

        if (foundAlbum.isPresent() == false ) {
            throw new NotFoundException("We couldn't find that album");
        }  return albumRepository.findById(id).get();

    }

    @PostMapping("/album")
    @ResponseStatus(HttpStatus.CREATED)
    public Album createAlbum(@RequestBody @Valid Album album) {
        return albumRepository.save(album);
    }

    @PutMapping("/album/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbum(@PathVariable Integer id, @RequestBody @Valid Album album) throws Exception {

        if (album.getId() == null) {
            album.setId(id);
        } else if (album.getId() != id) {
            throw new IllegalArgumentException("Id does not match");
        }
        albumRepository.save(album);
    }

    @PutMapping("/album")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbum(@RequestBody @Valid Album album) {

        albumRepository.save(album);
    }

    @DeleteMapping("/album/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable Integer id) throws Exception {
        Optional<Album> albumToDelete = albumRepository.findById(id);
        if (albumToDelete.isPresent() == false) {
            throw new IllegalArgumentException("No album with the id " + id);
        }
        albumRepository.deleteById(id);
    }
}