package com.trilogyed.musicstorecatalog.controller;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorecatalog.model.Album;
import com.trilogyed.musicstorecatalog.model.Label;
import com.trilogyed.musicstorecatalog.repository.LabelRepository;
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


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LabelController.class)

public class LabelControllerTest {

    @MockBean
    LabelRepository labelRepository;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private Label label;
    private String labelJson;
    private List<Label> allLabels = new ArrayList<>();
    private String allLabelsJson;

    @Before
    public void setup() throws Exception {
        setUpLabelMock();
    }

    public void setUpLabelMock() {
        label = new Label();
        label.setName("Rock & Roller Inc.");
        label.setWebsite("www.rockallsocks.net");

        Label savedLabel = new Label();
        savedLabel.setId(10);
        savedLabel.setName("Rock & Roller Inc.");
        savedLabel.setWebsite("www.rockallsocks.net");

        Label nextLabel = new Label();
        nextLabel.setId(11);
        nextLabel.setName("Yodelling Company");
        nextLabel.setWebsite("www.yodeloryell.com");

        allLabels.add(savedLabel);
        allLabels.add(nextLabel);

        doReturn(allLabels).when(labelRepository).findAll();
        doReturn(savedLabel).when(labelRepository).save(label);

    }

    @Test
    public void shouldReturnAllLabels() throws Exception{

        List<Label> listOfLabels = new ArrayList<>();
        listOfLabels.add(new Label("Rock & Roller Inc.", "www.rockallsocks.net"));
        listOfLabels.add(new Label("Yodelling Company", "www.yodeloryell.com"));

        when(labelRepository.findAll()).thenReturn(listOfLabels);

        assertEquals(2,labelRepository.findAll().size());

        String expectedJsonValue = mapper.writeValueAsString(listOfLabels);

        mockMvc.perform(get("/label"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }

    @Test
    public void ShouldFindLabelByIdAndReturnStatus200() throws Exception {
        Label nextLabel = new Label();
        nextLabel.setId(11);
        nextLabel.setName("Yodelling Company");
        nextLabel.setWebsite("www.yodeloryell.com");

        Optional<Label> thisLabel = Optional.of(nextLabel);
        when(labelRepository.findById(11)).thenReturn(thisLabel);

        if (thisLabel.isPresent()){
            nextLabel = thisLabel.get();
        }

        String expectedJsonValue = mapper.writeValueAsString(nextLabel);

        mockMvc.perform(get("/label/11"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }

    @Test
    public void shouldReturnAllLabelsAndStatus200() throws Exception {
        List<Label> listOfLabels = new ArrayList<Label>();
        listOfLabels.add(new Label("Rock & Roller Inc.", "www.rockallsocks.net"));
        listOfLabels.add(new Label("Yodelling Company", "www.yodeloryell.com"));

        when(labelRepository.findAll()).thenReturn(listOfLabels);

        doReturn(listOfLabels).when(labelRepository).findAll();

        String expectedJsonValue = mapper.writeValueAsString(listOfLabels);

        mockMvc.perform(get("/label"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }

    @Test
    public void shouldCreateLabelAndReturnNewLabelAndStatus201() throws Exception {

        Label inputLabel = new Label();
        inputLabel.setName("Rock & Roller Inc.");
        inputLabel.setWebsite("www.rockallsocks.net");

        Label outputLabel = new Label();
        outputLabel.setId(10);
        outputLabel.setName("Rock & Roller Inc.");
        outputLabel.setWebsite("www.rockallsocks.net");

        String outputJson = mapper.writeValueAsString(outputLabel);
        String inputJson = mapper.writeValueAsString(inputLabel);

        when(labelRepository.save(outputLabel)).thenReturn(inputLabel);

        mockMvc.perform(post("/label")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(outputJson));

    }

    @Test
    public void shouldUpdateTrackAndReturnStatus204() throws Exception {

        Label originalLabel = new Label();
        originalLabel.setName("Rock & Roller Inc.");
        originalLabel.setWebsite("www.rockallsocks.net");

        Label updatedLabel = new Label();
        updatedLabel.setId(14);
        updatedLabel.setName("Rock And Roller Inc.");
        updatedLabel.setWebsite("www.rockallsocks.inc");

        String outputJson = mapper.writeValueAsString(updatedLabel);
        String inputJson = mapper.writeValueAsString(originalLabel);

        when(labelRepository.save(updatedLabel)).thenReturn(originalLabel);

        mockMvc.perform(
                        put("/label/14")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    public void shouldReturnError422IfUpdatingWrongTrack() throws Exception {

        Label originalLabel = new Label();
        originalLabel.setName("Rock & Roller Inc.");
        originalLabel.setWebsite("www.rockallsocks.net");

        Label updatedLabel = new Label();
        updatedLabel.setId(14);
        updatedLabel.setName("Rock And Roller Inc.");
        updatedLabel.setWebsite("www.rockallsocks.inc");

        String outputJson = mapper.writeValueAsString(updatedLabel);
        String inputJson = mapper.writeValueAsString(originalLabel);

        when(labelRepository.save(updatedLabel)).thenReturn(originalLabel);

        mockMvc.perform(
                        put("/label/41")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void ShouldFindAndDeleteLabelAndReturnStatus204() throws Exception {

        Label inputLabel = new Label();
        inputLabel.setId(10);
        inputLabel.setName("Rock & Roller Inc.");
        inputLabel.setWebsite("www.rockallsocks.net");

        Optional<Label> thisLabel = Optional.of(inputLabel);
        when(labelRepository.findById(10)).thenReturn(thisLabel);

        labelRepository.delete(inputLabel);
        Mockito.verify(labelRepository, times(1)).delete(inputLabel);

        String expectedJsonValue = mapper.writeValueAsString(inputLabel);

        mockMvc.perform(delete("/label/10")) //Act
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void ShouldReturn404ErrorIfLabelNotFound() throws Exception {
        Label askLabel = new Label();
        askLabel.setId(44);
        askLabel.setName("Rock & Roller Inc.");
        askLabel.setWebsite("www.rockallsocks.net");

        Optional<Label> foundLabel = Optional.of(askLabel);
        when(labelRepository.findById(21)).thenReturn(foundLabel);

        String expectedJsonValue = mapper.writeValueAsString(foundLabel);

        mockMvc.perform(get("/album/64"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

}