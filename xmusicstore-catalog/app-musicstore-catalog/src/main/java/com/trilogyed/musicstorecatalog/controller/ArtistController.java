package com.trilogyed.musicstorecatalog.controller;

import com.trilogyed.musicstorecatalog.model.Artist;
import com.trilogyed.musicstorecatalog.repository.ArtistRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class ArtistController {
    
    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/artist")
    public List<Artist> getArtists() {
        return artistRepository.findAll();
    }

    @GetMapping("/artist/{id}")
    public Artist getArtistById(@PathVariable Integer id) throws Exception {
        Optional<Artist> foundArtist = artistRepository.findById(id);

        if (foundArtist.isPresent() == false ) {
            throw new NotFoundException("We couldn't find that artist");
        }  return artistRepository.findById(id).get();
    }

    @PostMapping("/artist")
    @ResponseStatus(HttpStatus.CREATED)
    public Artist createArtist(@RequestBody @Valid Artist artist) {
        return artistRepository.save(artist);
    }

    @PutMapping("/artist/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArtist(@PathVariable Integer id, @RequestBody @Valid Artist artist) {

        if (artist.getId() == null) {
            artist.setId(id);
        } else if (artist.getId() != id) {
            throw new IllegalArgumentException("Id does not match");
        }
        artistRepository.save(artist);
    }

    @PutMapping("/artist")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArtist(@RequestBody @Valid Artist artist) {

        artistRepository.save(artist);
    }

    @DeleteMapping("/artist/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(@PathVariable Integer id)  {
        Optional<Artist> artistToDelete = artistRepository.findById(id);
        if(artistToDelete.isPresent() == false ){
            throw new IllegalArgumentException("No artist with the id "+id);
        }
        artistRepository.deleteById(id);
    }
}
