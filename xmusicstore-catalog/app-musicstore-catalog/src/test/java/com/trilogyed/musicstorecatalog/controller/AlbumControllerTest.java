package com.trilogyed.musicstorecatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.musicstorecatalog.model.Album;
import com.trilogyed.musicstorecatalog.repository.AlbumRepository;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {

    @Autowired
    MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();


    @MockBean
    AlbumRepository albumRepo;

    private Album album;
    private List<Album> allAlbums = new ArrayList<>();
    private String albumJson;
    private String allAlbumsJson;

    @Before
    public void setup() throws Exception {
        setUpAlbumMock();
    }

    public void setUpAlbumMock() {
        album = new Album();
        album.setTitle("Victory");
        album.setLabelId(1);
        album.setArtistId(1);
        album.setListPrice(new BigDecimal("14.99"));
        album.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 22));

        Album albumWithId = new Album();
        albumWithId.setId(1);
        albumWithId.setTitle("Victory");
        albumWithId.setLabelId(1);
        albumWithId.setArtistId(1);
        albumWithId.setListPrice(new BigDecimal("14.99"));
        albumWithId.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 22));

        Album otherAlbum = new Album();
        otherAlbum.setId(2);
        otherAlbum.setTitle("Atmosphere Shift");
        otherAlbum.setLabelId(1);
        otherAlbum.setArtistId(2);
        otherAlbum.setListPrice(new BigDecimal("21.99"));
        otherAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        allAlbums.add(albumWithId);
        allAlbums.add(otherAlbum);

        doReturn(allAlbums).when(albumRepo).findAll();
        doReturn(albumWithId).when(albumRepo).save(album);

    }

    @Test
    public void shouldCreateAlbumAndReturnNewAlbumAndStatus201() throws Exception {
        Album inputAlbum = new Album();
        inputAlbum.setTitle("Victory");
        inputAlbum.setLabelId(1);
        inputAlbum.setArtistId(1);
        inputAlbum.setListPrice(new BigDecimal("14.99"));
        inputAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 22));

        Album outputAlbum = new Album();
        outputAlbum.setId(1);
        outputAlbum.setTitle("Victory");
        outputAlbum.setLabelId(1);
        outputAlbum.setArtistId(1);
        outputAlbum.setListPrice(new BigDecimal("14.99"));
        outputAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 22));

        String outputJson = mapper.writeValueAsString(outputAlbum);
        String inputJson = mapper.writeValueAsString(inputAlbum);

        doReturn(outputAlbum).when(albumRepo).save(inputAlbum);

        mockMvc.perform(post("/album")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));


    }

    @Test
    public void shouldReturnAllAlbumsAndStatus200() throws Exception {
        List<Album> albumList = new ArrayList<>();

        Album album1 = new Album();
        album1.setId(1);
        album1.setTitle("Victory");
        album1.setLabelId(1);
        album1.setArtistId(1);
        album1.setListPrice(new BigDecimal("14.99"));
        album1.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 22));

        Album album2 = new Album();
        album2.setId(2);
        album2.setTitle("Atmosphere Shift");
        album2.setLabelId(1);
        album2.setArtistId(2);
        album2.setListPrice(new BigDecimal("21.99"));
        album2.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        albumList.add(album1);
        albumList.add(album2);

        doReturn(albumList).when(albumRepo).findAll();

        String expectedJsonValue = mapper.writeValueAsString(albumList);

        mockMvc.perform(get("/album"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }


    @Test
    public void shouldUpdateAlbumAndReturnStatus204() throws Exception {

        Album original = new Album();
        original.setId(2);
        original.setTitle("Genesis");
        original.setLabelId(1);
        original.setArtistId(2);
        original.setListPrice(new BigDecimal("21.99"));
        original.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        Album updated = new Album();
        updated.setId(2);
        updated.setTitle("Genesis of Life");
        updated.setLabelId(1);
        updated.setArtistId(2);
        updated.setListPrice(new BigDecimal("21.99"));
        updated.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        String inputJson = mapper.writeValueAsString(original);
        String outputJson = mapper.writeValueAsString(updated);

        when(albumRepo.save(original)).thenReturn(updated);

        mockMvc.perform(
                        put("/album/2")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn422ErrorIfUpdatingWrongAlbum() throws Exception {

        Album inputAlbum = new Album();
        inputAlbum.setId(2);
        inputAlbum.setTitle("Genesis");
        inputAlbum.setLabelId(1);
        inputAlbum.setArtistId(2);
        inputAlbum.setListPrice(new BigDecimal("21.99"));
        inputAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        Album outputAlbum = new Album();
        outputAlbum.setId(2);
        outputAlbum.setTitle("Genesis of Life");
        outputAlbum.setLabelId(1);
        outputAlbum.setArtistId(2);
        outputAlbum.setListPrice(new BigDecimal("21.99"));
        outputAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        String inputJson = mapper.writeValueAsString(inputAlbum);
        String outputJson = mapper.writeValueAsString(outputAlbum);

        when(albumRepo.save(inputAlbum)).thenReturn(outputAlbum);

        mockMvc.perform(
                        put("/album/1")
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void ShouldFindAlbumByIdAndReturnStatus200() throws Exception {

        Album lostAlbum = new Album();
        lostAlbum.setId(21);
        lostAlbum.setTitle("Genesis of Life");
        lostAlbum.setLabelId(1);
        lostAlbum.setArtistId(2);
        lostAlbum.setListPrice(new BigDecimal("21.99"));
        lostAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        Optional<Album> thisAlbum = Optional.of(lostAlbum);
        when(albumRepo.findById(21)).thenReturn(thisAlbum);

        String expectedJsonValue = mapper.writeValueAsString(lostAlbum);

        mockMvc.perform(get("/album/21"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedJsonValue));

    }

    @Test
    public void ShouldReturn404ErrorIfAlbumNotFound() throws Exception {
        Album lostAlbum = new Album();
        lostAlbum.setId(21);
        lostAlbum.setTitle("Genesis of Life");
        lostAlbum.setLabelId(1);
        lostAlbum.setArtistId(2);
        lostAlbum.setListPrice(new BigDecimal("21.99"));
        lostAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        Optional<Album> thisAlbum = Optional.of(lostAlbum);
        when(albumRepo.findById(21)).thenReturn(thisAlbum);

        String expectedJsonValue = mapper.writeValueAsString(lostAlbum);

        mockMvc.perform(get("/album/12"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void ShouldDeleteAlbumByIdAndReturnStatus204() throws Exception {
        Album terribleAlbum = new Album();
        terribleAlbum.setId(22);
        terribleAlbum.setTitle("Tone Deaf Singers");
        terribleAlbum.setLabelId(1);
        terribleAlbum.setArtistId(2);
        terribleAlbum.setListPrice(new BigDecimal("21.99"));
        terribleAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        Optional<Album> thisAlbum = Optional.of(terribleAlbum);
        when(albumRepo.findById(22)).thenReturn(thisAlbum);

        albumRepo.delete(terribleAlbum);
        Mockito.verify(albumRepo, times(1)).delete(terribleAlbum);

        String expectedJsonValue = mapper.writeValueAsString(terribleAlbum);

        mockMvc.perform(delete("/album/22")) //Act
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void ShouldReturn422ErrorIfDeleteAlbumNotExists() throws Exception {
        Album terribleAlbum = new Album();
        terribleAlbum.setId(22);
        terribleAlbum.setTitle("Tone Deaf Singers");
        terribleAlbum.setLabelId(1);
        terribleAlbum.setArtistId(2);
        terribleAlbum.setListPrice(new BigDecimal("21.99"));
        terribleAlbum.setReleaseDate(LocalDate.ofEpochDay(2021 - 10 - 31));

        Optional<Album> thisAlbum = Optional.of(terribleAlbum);
        when(albumRepo.findById(22)).thenReturn(thisAlbum);

        albumRepo.delete(terribleAlbum);
        Mockito.verify(albumRepo, times(1)).delete(terribleAlbum);

        String expectedJsonValue = mapper.writeValueAsString(terribleAlbum);

        mockMvc.perform(delete("/album/222")) //Act
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}

