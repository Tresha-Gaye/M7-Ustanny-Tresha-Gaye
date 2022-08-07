package com.trilogyed.musicstorecatalog.controller;

import com.trilogyed.musicstorecatalog.model.Label;
import com.trilogyed.musicstorecatalog.model.Track;
import com.trilogyed.musicstorecatalog.model.Track;
import com.trilogyed.musicstorecatalog.repository.TrackRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class TrackController {
    @Autowired
    TrackRepository trackRepository;

    @GetMapping("/track")
    public List<Track> getTracks() {
        return trackRepository.findAll();
    }

    @GetMapping("/track/{id}")
    public Track getTrackById(@PathVariable Integer id) throws Exception {
        Optional<Track> foundTrack = trackRepository.findById(id);

        if (foundTrack.isPresent() == false ) {
            throw new NotFoundException("We couldn't find that track");
        } return trackRepository.findById(id).get();
    }

    @PostMapping("/track")
    @ResponseStatus(HttpStatus.CREATED)
    public Track createTrack(@RequestBody @Valid Track track) {
        return trackRepository.save(track);
    }


    @PutMapping("/track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@PathVariable Integer id, @RequestBody @Valid Track track) {

        if (track.getId() == null) {
            track.setId(id);
        } else if (track.getId() != id) {
            throw new IllegalArgumentException("Id does not match");
        }
        trackRepository.save(track);
    }

    @PutMapping("/track")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@RequestBody @Valid Track track) {

        trackRepository.save(track);
    }

    @DeleteMapping("/track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrack(@PathVariable Integer id) {
        Optional<Track> trackToDelete = trackRepository.findById(id);
        if(trackToDelete.isPresent() == false ){
            throw new IllegalArgumentException("We can't find track "+id);
        }
        trackRepository.deleteById(id);
    }
}