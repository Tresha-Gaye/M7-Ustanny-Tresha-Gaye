package com.trilogyed.musicstorerecommendation.controller;

import com.trilogyed.musicstorerecommendation.model.AlbumRecommendation;
import com.trilogyed.musicstorerecommendation.model.TrackRecommendation;
import com.trilogyed.musicstorerecommendation.repository.TrackRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TrackRecommendationController {
    @Autowired
    TrackRecommendationRepository trackRecRepo;

    @GetMapping("/track-likes")
    public List<TrackRecommendation> getTracks() {
        return trackRecRepo.findAll();
    }

    @GetMapping("/track-likes/{id}")
    public TrackRecommendation getTrackById(@PathVariable Integer id) {

        Optional<TrackRecommendation> foundTrack = trackRecRepo.findById(id);

        if (foundTrack.isPresent() == false ) {
            throw new IllegalArgumentException("We couldn't find that track");
        }
        return trackRecRepo.findById(id).get();
    }

    @PostMapping("/track-likes")
    @ResponseStatus(HttpStatus.CREATED)
    public TrackRecommendation createTrack(@RequestBody TrackRecommendation track) {
        return trackRecRepo.save(track);
    }


    @PutMapping("/track-likes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@PathVariable Integer id, @RequestBody TrackRecommendation track) {

        if (track.getId() == null) {
            track.setId(id);
        } else if (track.getId() != id) {
            throw new IllegalArgumentException("Id does not match");
        }
        trackRecRepo.save(track);
    }

    @PutMapping("/track-likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@RequestBody TrackRecommendation track) {

        trackRecRepo.save(track);
    }

    @DeleteMapping("/track-likes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrack(@PathVariable Integer id) {
        Optional<TrackRecommendation> trackToDelete = trackRecRepo.findById(id);
        if(trackToDelete.isPresent() == false ){
            throw new IllegalArgumentException("No track with the id "+id);
        }
        trackRecRepo.deleteById(id);
    }
}