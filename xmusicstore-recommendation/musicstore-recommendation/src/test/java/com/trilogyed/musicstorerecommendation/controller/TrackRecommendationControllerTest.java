package com.trilogyed.musicstorerecommendation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorerecommendation.model.AlbumRecommendation;
import com.trilogyed.musicstorerecommendation.model.LabelRecommendation;
import com.trilogyed.musicstorerecommendation.model.TrackRecommendation;
import com.trilogyed.musicstorerecommendation.repository.TrackRecommendationRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackRecommendationController.class)
public class TrackRecommendationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrackRecommendationRepository trackRepository;

    ObjectMapper mapper = new ObjectMapper();

    private TrackRecommendation track;
    private List<TrackRecommendation> allTracks = new ArrayList<>();


    @Before
    public void setup() throws Exception {
        setupTrackControllerMock();
    }

    private void setupTrackControllerMock() {
        TrackRecommendation track = new TrackRecommendation();
        track.setId(1);
        track.setTrackId(1);
        track.setUserId(1);
        track.setLiked(true);

        TrackRecommendation trackWithId = new TrackRecommendation();
        trackWithId.setId(1);
        trackWithId.setTrackId(1);
        trackWithId.setUserId(1);
        trackWithId.setLiked(true);

        TrackRecommendation track2 = new TrackRecommendation();
        track2.setTrackId(2);
        track2.setTrackId(2);
        track2.setUserId(2);
        track2.setLiked(false);

        allTracks.add(trackWithId);
        allTracks.add(track2);

        doReturn(allTracks).when(trackRepository).findAll();
        doReturn(trackWithId).when(trackRepository).save(track);

    }

    @Test
    public void shouldReturnAllTracks() throws Exception {

        List<TrackRecommendation> trackList = new ArrayList<TrackRecommendation>();
        trackList.add(new TrackRecommendation(1, 1, 1, true));
        trackList.add(new TrackRecommendation(2, 2, 2, false));
        Mockito.when(trackRepository.findAll()).thenReturn(trackList);
        String expectedJson = mapper.writeValueAsString(trackList);

        mockMvc.perform(get("/track-likes"))//Act
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));

    }


    @Test
    public void shouldCreateTrackAndReturnNewTrackAndStatus200() throws Exception {
        TrackRecommendation inputTrack = new TrackRecommendation();
        inputTrack.setTrackId(1);
        inputTrack.setUserId(1);
        inputTrack.setLiked(true);

        TrackRecommendation outputTrack = new TrackRecommendation();
        outputTrack.setId(1);
        outputTrack.setUserId(1);
        outputTrack.setTrackId(1);
        outputTrack.setLiked(true);

        String outputProduceJson = mapper.writeValueAsString(outputTrack);
        String inputProduceJson = mapper.writeValueAsString(inputTrack);

        // doReturn(outputTrack).when(trackRepository).save(inputTrack);
        when(trackRepository.save(inputTrack)).thenReturn(outputTrack);

        mockMvc.perform(post("/track-likes")
                        .content(inputProduceJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(outputProduceJson));

    }

    @Test
    public void shouldUpdateTrackByIdAndReturnStatus204() throws Exception {

        TrackRecommendation inputTrack = new TrackRecommendation();
        inputTrack.setTrackId(121);
        inputTrack.setUserId(15);
        inputTrack.setLiked(true);

        TrackRecommendation outputTrack = new TrackRecommendation();
        outputTrack.setId(11);
        outputTrack.setTrackId(121);
        outputTrack.setUserId(15);
        outputTrack.setLiked(false);

        String outputJson = mapper.writeValueAsString(outputTrack);
        String inputJson = mapper.writeValueAsString(inputTrack);

        when(trackRepository.save(inputTrack)).thenReturn(outputTrack);

        mockMvc.perform(
                        put("/track-likes/11")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldUpdateTrackAndReturnStatus204() throws Exception {

        TrackRecommendation inputTrack = new TrackRecommendation();
        inputTrack.setId(11);
        inputTrack.setTrackId(121);
        inputTrack.setUserId(15);
        inputTrack.setLiked(true);

        TrackRecommendation outputTrack = new TrackRecommendation();
        outputTrack.setId(11);
        outputTrack.setTrackId(121);
        outputTrack.setUserId(15);
        outputTrack.setLiked(false);

        String outputJson = mapper.writeValueAsString(outputTrack);
        String inputJson = mapper.writeValueAsString(inputTrack);

        when(trackRepository.save(inputTrack)).thenReturn(outputTrack);

        mockMvc.perform(
                        put("/track-likes")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn422ErrorIfUpdatingWrongTrackRecommendation() throws Exception {

        TrackRecommendation inputTrack = new TrackRecommendation();
        inputTrack.setId(11);
        inputTrack.setTrackId(121);
        inputTrack.setUserId(15);
        inputTrack.setLiked(true);

        TrackRecommendation outputTrack = new TrackRecommendation();
        outputTrack.setId(11);
        outputTrack.setTrackId(121);
        outputTrack.setUserId(15);
        outputTrack.setLiked(false);

        String outputJson = mapper.writeValueAsString(outputTrack);
        String inputJson = mapper.writeValueAsString(inputTrack);

        when(trackRepository.save(inputTrack)).thenReturn(outputTrack);

        mockMvc.perform(
                        put("/track-likes/24")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    public void ShouldDeleteAlbumById() throws Exception {
        TrackRecommendation track1 = new TrackRecommendation(1, 1, 1, false);

        Optional<TrackRecommendation> optionalTrack = Optional.of(track1);
        Mockito.when(trackRepository.findById(1)).thenReturn(optionalTrack);

        if (optionalTrack.isPresent()) {
            track1 = optionalTrack.get();
        }

        String expectedJsonValue = mapper.writeValueAsString(track1);

        mockMvc.perform(delete("/track-likes/1")) //Act
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void ShouldReturn422ErrorOnDeleteIfLabelRecommendationNotExists() throws Exception {
        TrackRecommendation track1 = new TrackRecommendation(10, 9, 8, true);

        Optional<TrackRecommendation> optionalTrack = Optional.of(track1);
        Mockito.when(trackRepository.findById(1)).thenReturn(optionalTrack);

        if (optionalTrack.isPresent()) {
            track1 = optionalTrack.get();
        }

        String expectedJsonValue = mapper.writeValueAsString(track1);

        mockMvc.perform(delete("/track-likes/100")) //Act
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

}