package com.trilogyed.musicstorerecommendation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorerecommendation.model.AlbumRecommendation;
import com.trilogyed.musicstorerecommendation.model.LabelRecommendation;
import com.trilogyed.musicstorerecommendation.repository.LabelRecommendationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LabelRecommendationController.class)
public class LabelRecommendationControllerTest {

    @MockBean
    LabelRecommendationRepository labelRepo;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private LabelRecommendation label;
    private List<LabelRecommendation> allLabels = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        setUpLabelRecommendationMock();
    }

    public void setUpLabelRecommendationMock() {
        label = new LabelRecommendation();
        label.setId(1);
        label.setLabelId(2);
        label.setUserId(3);
        label.setLiked(true);

        LabelRecommendation labelWithId = new LabelRecommendation();
        label.setId(1);
        label.setLabelId(2);
        label.setUserId(3);
        label.setLiked(true);

        LabelRecommendation label1 = new LabelRecommendation();
        label1.setId(3);
        label1.setLabelId(2);
        label1.setUserId(1);
        label1.setLiked(false);

        allLabels.add(labelWithId);
        allLabels.add(label1);

        doReturn(allLabels).when(labelRepo).findAll();
        doReturn(labelWithId).when(labelRepo).save(label);

    }

    @Test
    public void shouldReturnAllLabelRecommendations() throws Exception {

        doReturn(allLabels).when(labelRepo).findAll();

        String expectedJsonValue = mapper.writeValueAsString(allLabels);

        mockMvc.perform(get("/label-likes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));
    }


    @Test
    public void ShouldFindLabelRecommendationsByIdAndReturnStatus200() throws Exception {
        LabelRecommendation labelRec = new LabelRecommendation();
        labelRec.setId(12);
        labelRec.setLabelId(13);
        labelRec.setUserId(14);
        labelRec.setLiked(false);

        Optional<LabelRecommendation> thisLabel = Optional.of(labelRec);
        when(labelRepo.findById(1)).thenReturn(thisLabel);

        if (thisLabel.isPresent()) {
            labelRec = thisLabel.get();
        }

        String expectedJsonValue = mapper.writeValueAsString(labelRec);

        mockMvc.perform(get("/label-likes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }

    @Test
    public void shouldReturnAllLabelRecommendationsAndStatus200() throws Exception {
        List<LabelRecommendation> labelList = new ArrayList<>();

        LabelRecommendation labelRec1 = new LabelRecommendation();
        labelRec1.setId(1);
        labelRec1.setLabelId(1);
        labelRec1.setUserId(1);
        labelRec1.setLiked(true);

        LabelRecommendation labelRec2 = new LabelRecommendation();
        labelRec2.setId(2);
        labelRec2.setLabelId(2);
        labelRec2.setUserId(2);
        labelRec2.setLiked(false);

        labelList.add(labelRec1);
        labelList.add(labelRec2);

        doReturn(labelList).when(labelRepo).findAll();

        String expectedJsonValue = mapper.writeValueAsString(labelList);

        mockMvc.perform(get("/label-likes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }

    @Test
    public void shouldCreateLabelRecommendationAndReturnNewLabelRecommendationAndStatus200() throws Exception {

        LabelRecommendation labelRec1 = new LabelRecommendation();
        labelRec1.setLabelId(1);
        labelRec1.setUserId(1);
        labelRec1.setLiked(true);

        LabelRecommendation savedLabelRec1 = new LabelRecommendation();
        savedLabelRec1.setId(1);
        savedLabelRec1.setLabelId(1);
        savedLabelRec1.setUserId(1);
        savedLabelRec1.setLiked(true);

        String outputLabelJson = mapper.writeValueAsString(savedLabelRec1);
        String inputLabelJson = mapper.writeValueAsString(labelRec1);

        // doReturn(expectedTrack).when(labelRepository).save(actualLabel);
        when(labelRepo.save(labelRec1)).thenReturn(savedLabelRec1);

        mockMvc.perform(post("/label-likes")
                        .content(inputLabelJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(outputLabelJson));

    }

    @Test
    public void shouldUpdateLabelRecommendationAndReturnStatus204() throws Exception {

        LabelRecommendation actualLabel = new LabelRecommendation();
        actualLabel.setId(10);
        actualLabel.setLabelId(12);
        actualLabel.setUserId(36);
        actualLabel.setLiked(true);


        LabelRecommendation updatedLabel = new LabelRecommendation();
        updatedLabel.setId(10);
        updatedLabel.setLabelId(12);
        updatedLabel.setUserId(360);
        actualLabel.setLiked(false);


        String outputJson = mapper.writeValueAsString(updatedLabel);
        String inputJson = mapper.writeValueAsString(actualLabel);

        when(labelRepo.save(actualLabel)).thenReturn(updatedLabel);

        mockMvc.perform(
                        put("/label-likes")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldUpdateLabelRecommendationByIdAndReturnStatus204() throws Exception {

        LabelRecommendation actualLabel = new LabelRecommendation();
        actualLabel.setId(100);
        actualLabel.setLabelId(12);
        actualLabel.setUserId(36);
        actualLabel.setLiked(true);


        LabelRecommendation updatedLabel = new LabelRecommendation();
        updatedLabel.setId(100);
        updatedLabel.setLabelId(12);
        updatedLabel.setUserId(36);
        actualLabel.setLiked(false);


        String outputJson = mapper.writeValueAsString(updatedLabel);
        String inputJson = mapper.writeValueAsString(actualLabel);

        when(labelRepo.save(actualLabel)).thenReturn(updatedLabel);

        mockMvc.perform(
                        put("/label-likes/100")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void ShouldFindAndDeleteLabelRecommendationAndReturnStatus204() throws Exception {

        LabelRecommendation label = new LabelRecommendation();
        label.setId(1);
        label.setLabelId(2);
        label.setUserId(3);
        label.setLiked(true);

        Optional<LabelRecommendation> thisLabel = Optional.of(label);
        when(labelRepo.findById(1)).thenReturn(thisLabel);

        labelRepo.delete(label);
        Mockito.verify(labelRepo, times(1)).delete(label);

        String expectedJsonValue = mapper.writeValueAsString(label);

        mockMvc.perform(delete("/label-likes/1")) //Act
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void ShouldReturn422ErrorOnDeleteIfLabelRecommendationNotExists() throws Exception {
        LabelRecommendation label = new LabelRecommendation();
        label.setId(1);
        label.setLabelId(2);
        label.setUserId(3);
        label.setLiked(true);

        Optional<LabelRecommendation> thisLabel = Optional.of(label);
        when(labelRepo.findById(1)).thenReturn(thisLabel);

        labelRepo.delete(label);
        Mockito.verify(labelRepo, times(1)).delete(label);

        String expectedJsonValue = mapper.writeValueAsString(label);

        mockMvc.perform(delete("/label-likes/11")) //Act
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

}