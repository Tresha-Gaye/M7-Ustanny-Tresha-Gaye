package com.trilogyed.musicstorecatalog.controller;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.trilogyed.musicstorecatalog.model.Album;
import com.trilogyed.musicstorecatalog.model.Artist;
import com.trilogyed.musicstorecatalog.model.Artist;
import com.trilogyed.musicstorecatalog.repository.ArtistRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private ArtistRepository artistRepo;
    
    private Artist artist;
    private List<Artist> allArtists = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        setUpArtistMock();

    }

    public void setUpArtistMock() throws Exception {
        artist = new Artist();
        artist.setName("Victoria Orenze");
        artist.setTwitter("HappyVicky");
        artist.setTwitter("InstaVicky");

        Artist saveArtist = new Artist();
        saveArtist.setId(21);
        saveArtist.setName("Victoria Orenze");
        saveArtist.setTwitter("HappyVickyTweets");
        saveArtist.setTwitter("InstaVicky");

        Artist awardArtist = new Artist();
        awardArtist.setId(18);
        awardArtist.setName("Adele");
        awardArtist.setTwitter("AdeleTweets");
        awardArtist.setTwitter("InstAdele");

        allArtists.add(saveArtist);
        allArtists.add(awardArtist);

        doReturn(allArtists).when(artistRepo).findAll();
        doReturn(saveArtist).when(artistRepo).save(artist);

    }


    @Test
    public void ShouldCreateArtist() throws Exception{

        Artist inputBody = new Artist();
        inputBody.setName("Victoria Orenze");
        inputBody.setTwitter("HappyVicky");
        inputBody.setInstagram("InstaVicky");

        Artist outputBody = new Artist();
        outputBody.setId(21);
        outputBody.setName("Victoria Orenze");
        outputBody.setTwitter("HappyVickyTweets");
        outputBody.setInstagram("InstaVicky");

        String outputJson = mapper.writeValueAsString(outputBody);
        String inputJson = mapper.writeValueAsString(inputBody);

        doReturn(outputBody).when(artistRepo).save(inputBody);

        mockMvc.perform(post("/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnAllArtists() throws Exception{

        List<Artist> artists = new ArrayList<>();

        Artist saveArtist = new Artist();
        saveArtist.setId(21);
        saveArtist.setName("Victoria Orenze");
        saveArtist.setTwitter("HappyVickyTweets");
        saveArtist.setInstagram("InstaVicky");

        Artist awardArtist = new Artist();
        awardArtist.setId(18);
        awardArtist.setName("Adele");
        awardArtist.setTwitter("AdeleTweets");
        awardArtist.setInstagram("InstAdele");

        artists.add(saveArtist);
        artists.add(awardArtist);

        doReturn(artists).when(artistRepo).findAll();

        String expectedJson = mapper.writeValueAsString(artists);

        mockMvc.perform(get("/artist"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));

    }

    @Test
    public void shouldFindArtistByIdAndReturnStatus200() throws Exception{

        Artist awardArtist = new Artist();
        awardArtist.setId(18);
        awardArtist.setName("Adele");
        awardArtist.setTwitter("AdeleTweets");
        awardArtist.setInstagram("InstAdele");

        Optional<Artist> returnArtist = Optional.of(awardArtist);
        when(artistRepo.findById(18)).thenReturn(returnArtist);

        String expectedJson = mapper.writeValueAsString(awardArtist);

        mockMvc.perform(get("/artist/18")) //Act
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJson));

    }

    @Test
    public void ShouldReturn404ErrorIfArtistIdNotFound() throws Exception {
        Artist awardArtist = new Artist();
        awardArtist.setId(18);
        awardArtist.setName("Adele");
        awardArtist.setTwitter("AdeleTweets");
        awardArtist.setInstagram("InstAdele");

        Optional<Artist> locateArtist = Optional.of(awardArtist);
        when(artistRepo.findById(18)).thenReturn(locateArtist);

        String expectedJsonValue = mapper.writeValueAsString(awardArtist);

        mockMvc.perform(get("/artist/81"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldUpdateArtistAndReturnStatus204() throws Exception {

        Artist inputArtist = new Artist();
        inputArtist.setName("Adele");
        inputArtist.setTwitter("AdeleTweets");
        inputArtist.setInstagram("InstAdele");

        Artist outputArtist = new Artist();
        inputArtist.setId(18);
        inputArtist.setName("Adele");
        inputArtist.setTwitter("TheRealAdeleTweets");
        inputArtist.setInstagram("TheRealInstAdele");

        String inputJson = mapper.writeValueAsString(inputArtist);
        String outputJson = mapper.writeValueAsString(outputArtist);

        when(artistRepo.save(inputArtist)).thenReturn(outputArtist);

        mockMvc.perform(
                        put("/artist/18")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn422ErrorIfUpdatingWrongArtist() throws Exception {

        Artist inputArtist = new Artist();
        inputArtist.setId(18);
        inputArtist.setName("Adele");
        inputArtist.setTwitter("AdeleTweets");
        inputArtist.setInstagram("InstAdele");

        Artist outputArtist = new Artist();
        inputArtist.setId(18);
        inputArtist.setName("Adele");
        inputArtist.setTwitter("TheRealAdeleTweets");
        inputArtist.setInstagram("TheRealInstAdele");

        String inputJson = mapper.writeValueAsString(inputArtist);
        String outputJson = mapper.writeValueAsString(outputArtist);

        when(artistRepo.save(inputArtist)).thenReturn(outputArtist);

        mockMvc.perform(
                        put("/artist/118")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }



    @Test
    public void ShouldDeleteArtistByIdAndReturnStatus200() throws Exception{

        Artist newArtist = new Artist();
        newArtist.setId(18);
        newArtist.setName("Adele");
        newArtist.setTwitter("AdeleTweets");
        newArtist.setTwitter("InstAdele");

        Optional<Artist> locateArtist = Optional.of(newArtist);
        when(artistRepo.findById(18)).thenReturn(locateArtist);

        artistRepo.delete(newArtist);
        Mockito.verify(artistRepo, times(1)).delete(newArtist);

        String expectedJsonValue = mapper.writeValueAsString(newArtist);

        mockMvc.perform(delete("/artist/18")) //Act
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void ShouldReturn422ErrorIfDeletingAndArtistNotExist() throws Exception{

        Artist newArtist = new Artist();
        newArtist.setId(18);
        newArtist.setName("Adele");
        newArtist.setTwitter("AdeleTweets");
        newArtist.setTwitter("InstAdele");

        Optional<Artist> locateArtist = Optional.of(newArtist);
        when(artistRepo.findById(18)).thenReturn(locateArtist);

        artistRepo.delete(newArtist);
        Mockito.verify(artistRepo, times(1)).delete(newArtist);

        String expectedJsonValue = mapper.writeValueAsString(newArtist);

        mockMvc.perform(delete("/artist/81")) //Act
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }









}