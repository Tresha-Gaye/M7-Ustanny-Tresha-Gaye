package com.trilogyed.musicstorecatalog.controller;


import com.trilogyed.musicstorecatalog.model.Album;
import com.trilogyed.musicstorecatalog.model.Label;
import com.trilogyed.musicstorecatalog.repository.LabelRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class LabelController {
    @Autowired
    LabelRepository labelRepository;

    @GetMapping("/label")
    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    @GetMapping("/label/{id}")
    public Label getLabelById(@PathVariable Integer id) throws Exception  {

        Optional<Label> foundLabel = labelRepository.findById(id);

        if (foundLabel.isPresent() == false ) {
            throw new NotFoundException("We couldn't find that label");
        }  return labelRepository.findById(id).get();

    }

    @PostMapping("/label")
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(@RequestBody @Valid Label label) {
       return labelRepository.save(label);
    }


    @PutMapping("/label/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@PathVariable Integer id, @RequestBody @Valid Label label) {

        if (label.getId() == null) {
            label.setId(id);
        } else if (label.getId() != id) {
            throw new IllegalArgumentException("Id does not match");
        }
        labelRepository.save(label);
    }

    @PutMapping("/label")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@RequestBody @Valid Label label) {

        labelRepository.save(label);
    }

    @DeleteMapping("/label/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable Integer id) {
        Optional<Label> labelToDelete = labelRepository.findById(id);
        if(labelToDelete.isPresent() == false ){
            throw new IllegalArgumentException("No label with the id "+id);
        }
        labelRepository.deleteById(id);
    }
}