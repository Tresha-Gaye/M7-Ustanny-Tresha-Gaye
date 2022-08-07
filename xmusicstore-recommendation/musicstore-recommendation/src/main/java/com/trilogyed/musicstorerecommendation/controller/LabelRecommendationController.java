package com.trilogyed.musicstorerecommendation.controller;

import com.trilogyed.musicstorerecommendation.model.AlbumRecommendation;
import com.trilogyed.musicstorerecommendation.model.LabelRecommendation;
import com.trilogyed.musicstorerecommendation.repository.LabelRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LabelRecommendationController {

    @Autowired
    LabelRecommendationRepository labelRecRepo;

    @GetMapping("/label-likes")
    public List<LabelRecommendation> getLabels() {
        return labelRecRepo.findAll();
    }

    @GetMapping("/label-likes/{id}")
    public LabelRecommendation getLabelById(@PathVariable Integer id) {

        Optional<LabelRecommendation> foundLabel = labelRecRepo.findById(id);

        if (foundLabel.isPresent() == false ) {
            throw new IllegalArgumentException("We couldn't find that label");
        }
        return labelRecRepo.findById(id).get();
    }

    @PostMapping("/label-likes")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelRecommendation createLabel(@RequestBody LabelRecommendation label) {
       return labelRecRepo.save(label);
    }


    @PutMapping("/label-likes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@PathVariable Integer id, @RequestBody LabelRecommendation label) {

        if (label.getId() == null) {
            label.setId(id);
        } else if (label.getId() != id) {
            throw new IllegalArgumentException("Id does not match");
        }
        labelRecRepo.save(label);
    }

    @PutMapping("/label-likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@RequestBody LabelRecommendation label) {

        labelRecRepo.save(label);
    }

    @DeleteMapping("/label-likes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable Integer id) {
        Optional<LabelRecommendation> labelToDelete = labelRecRepo.findById(id);
        if(labelToDelete.isPresent() == false ){
            throw new IllegalArgumentException("No label with the id "+id);
        }
        labelRecRepo.deleteById(id);
    }
}