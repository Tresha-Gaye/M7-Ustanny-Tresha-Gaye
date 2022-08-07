package com.trilogyed.musicstorecatalog.repository;

import com.trilogyed.musicstorecatalog.model.Album;
import com.trilogyed.musicstorecatalog.model.Artist;
import com.trilogyed.musicstorecatalog.model.Label;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AlbumRepositoryTest{

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private LabelRepository labelRepository;


    @Before
    public void setUp() throws Exception {

        trackRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    public void addGetDeleteAlbum() {

        Label newlabel = new Label();
        newlabel.setName("JamRock");
        newlabel.setWebsite("www.jamrock.com");
        newlabel = labelRepository.save(newlabel);

        Artist newArtist = new Artist();
        newArtist.setName("TuffGong");
        newArtist.setInstagram("@TuffGong");
        newArtist.setTwitter("@TheRealTuffGong");
        newArtist = artistRepository.save(newArtist);

        Album newAlbum = new Album();
        newAlbum.setTitle("Top New Reggae");
        newAlbum.setArtistId(newArtist.getId());
        newAlbum.setLabelId(newlabel.getId());
        newAlbum.setReleaseDate(LocalDate.of(2022, 8, 5));
        newAlbum.setListPrice(new BigDecimal("21.95"));

        newAlbum = albumRepository.save(newAlbum);

        Optional<Album> album1 = albumRepository.findById(newAlbum.getId());

        assertEquals(album1.get(), newAlbum);

        albumRepository.deleteById(newAlbum.getId());

        album1 = albumRepository.findById(newAlbum.getId());

        assertFalse(album1.isPresent());

    }

    @Test(expected  = DataIntegrityViolationException.class)
    public void addWithRefIntegrityException() {

        Album newAlbum = new Album();
        newAlbum.setTitle("Greatest Hits");
        newAlbum.setArtistId(54);
        newAlbum.setLabelId(91);
        newAlbum.setReleaseDate(LocalDate.of(2010, 1, 5));
        newAlbum.setListPrice(new BigDecimal("21.95"));

        albumRepository.save(newAlbum);

    }

    @Test
    public void getAllAlbums() {

        Label newlabel = new Label();
        newlabel.setName("JamRock");
        newlabel.setWebsite("www.jamrock.com");
        newlabel = labelRepository.save(newlabel);

        Artist newArtist = new Artist();
        newArtist.setName("TuffGong");
        newArtist.setInstagram("@TuffGong");
        newArtist.setTwitter("@TheRealTuffGong");
        newArtist = artistRepository.save(newArtist);

        Album newAlbum = new Album();
        newAlbum.setTitle("Top New Reggae");
        newAlbum.setArtistId(newArtist.getId());
        newAlbum.setLabelId(newlabel.getId());
        newAlbum.setReleaseDate(LocalDate.of(2022, 8, 5));
        newAlbum.setListPrice(new BigDecimal("21.95"));

        albumRepository.save(newAlbum);


        Album album2 = new Album();
        album2.setTitle("Blue Mountain Coffee");
        album2.setArtistId(newArtist.getId());
        album2.setLabelId(newlabel.getId());
        album2.setReleaseDate(LocalDate.of(2022, 4, 5));
        album2.setListPrice(new BigDecimal("18.95"));

        albumRepository.save(album2);

        List<Album> aList = albumRepository.findAll();

        assertEquals(aList.size(), 2);

    }

    @Test
    public void updateAlbum() {

        Label newlabel = new Label();
        newlabel.setName("JamRock");
        newlabel.setWebsite("www.jamrock.com");
        newlabel = labelRepository.save(newlabel);

        Artist newArtist = new Artist();
        newArtist.setName("TuffGong");
        newArtist.setInstagram("@TuffGong");
        newArtist.setTwitter("@TheRealTuffGong");
        newArtist = artistRepository.save(newArtist);

        Album newAlbum = new Album();
        newAlbum.setTitle("Top New Reggae");
        newAlbum.setArtistId(newArtist.getId());
        newAlbum.setLabelId(newlabel.getId());
        newAlbum.setReleaseDate(LocalDate.of(2022, 8, 5));
        newAlbum.setListPrice(new BigDecimal("21.95"));

        newAlbum = albumRepository.save(newAlbum);

        newAlbum.setTitle("NEW TITLE");
        newAlbum.setReleaseDate(LocalDate.of(2000, 7, 7));
        newAlbum.setListPrice(new BigDecimal("15.68"));

        albumRepository.save(newAlbum);

        Optional<Album> album1 = albumRepository.findById(newAlbum.getId());
        assertEquals(album1.get(), newAlbum);

    }

}