package com.trilogyed.musicstorecatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorecatalog.model.Track;
import com.trilogyed.musicstorecatalog.repository.TrackRepository;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackController.class)
public class TrackControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrackRepository trackRepo;

    ObjectMapper mapper = new ObjectMapper();

    private Track track;
    private List<Track> allTracks = new ArrayList<>();
    private String trackJson;
    private String allTracksJson;

    @Before
    public void setup() throws Exception {
        setUpTrackMock();
    }

    public void setUpTrackMock() {
        track = new Track();
        track.setTitle("Victory");
        track.setAlbumId(1);
        track.setRuntime(100);

        Track trackWithId = new Track();
        trackWithId.setId(1);
        trackWithId.setTitle("Victory");
        trackWithId.setAlbumId(1);
        trackWithId.setRuntime(100);

        Track track2 = new Track();
        track2.setId(2);
        track2.setTitle("Atmosphere Shift");
        track2.setTitle("Amazing");
        track2.setAlbumId(2);
        track2.setRuntime(250);

        allTracks.add(trackWithId);
        allTracks.add(track2);

        doReturn(allTracks).when(trackRepo).findAll();
        doReturn(trackWithId).when(trackRepo).save(track);

    }


    @Test
    public void shouldCreateTrackAndReturnNewTrackAndStatus200() throws Exception {
        Track inputTrack = new Track();
        inputTrack.setTitle("Awesome Song");
        inputTrack.setAlbumId(1);
        inputTrack.setRuntime(100);

        Track outputTrack = new Track();
        outputTrack.setId(1);
        outputTrack.setTitle("Awesome Song");
        outputTrack.setAlbumId(1);
        outputTrack.setRuntime(100);

        String outputProduceJson = mapper.writeValueAsString(outputTrack);
        String inputProduceJson = mapper.writeValueAsString(inputTrack);

        // doReturn(outputTrack).when(trackRepository).save(inputTrack);
        when(trackRepo.save(inputTrack)).thenReturn(outputTrack);

        mockMvc.perform(post("/track")
                        .content(inputProduceJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(outputProduceJson));

 }

    @Test
    public void shouldReturnAllTracksAndStatus200() throws Exception {
        List<Track> trackList = new ArrayList<>();

        Track track1 = new Track();
        track1.setId(1);
        track1.setTitle("Awesome Song");
        track1.setAlbumId(1);
        track1.setRuntime(100);


        Track track2 = new Track();
        track1.setId(2);
        track1.setTitle("Epic Song");
        track1.setAlbumId(14);
        track1.setRuntime(999);

        trackList.add(track1);
        trackList.add(track2);

        doReturn(trackList).when(trackRepo).findAll();

        String expectedJsonValue = mapper.writeValueAsString(trackList);

        mockMvc.perform(get("/track"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }


    @Test
    public void shouldUpdateTrackAndReturnStatus204() throws Exception {

        Track inputTrack = new Track();
//        inputTrack.setId(1);
        inputTrack.setTitle("Awesome Song");
        inputTrack.setAlbumId(1);
        inputTrack.setRuntime(100);

        Track outputTrack = new Track();
        outputTrack.setId(1);
        outputTrack.setTitle("Awesome Song");
        outputTrack.setAlbumId(1);
        outputTrack.setRuntime(123);

        String outputJson = mapper.writeValueAsString(outputTrack);
        String inputJson = mapper.writeValueAsString(inputTrack);

        when(trackRepo.save(inputTrack)).thenReturn(outputTrack);

        mockMvc.perform(
                        put("/track/1")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }


    @Test
    public void ShouldFindTrackByIdAndReturnStatus200() throws Exception {
        Track track3 = new Track();
        track3.setId(2);
        track3.setTitle("Epic Song");
        track3.setAlbumId(14);
        track3.setRuntime(999);

        Optional<Track> thisTrack = Optional.of(track3);
        when(trackRepo.findById(2)).thenReturn(thisTrack);

        if (thisTrack.isPresent()){
            track3 = thisTrack.get();
        }

        String expectedJsonValue = mapper.writeValueAsString(track3);

        mockMvc.perform(get("/track/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }

    @Test
    public void ShouldDeleteTrackByIdAndReturnStatus204() throws Exception {
        Track track2 = new Track();
        track2.setId(2);
        track2.setTitle("Epic Song");
        track2.setAlbumId(14);
        track2.setRuntime(999);

        Optional<Track> thisTrack = Optional.of(track2);
        when(trackRepo.findById(1)).thenReturn(thisTrack);

        trackRepo.delete(track2);
        Mockito.verify(trackRepo, times(1)).delete(track2);
    }

}