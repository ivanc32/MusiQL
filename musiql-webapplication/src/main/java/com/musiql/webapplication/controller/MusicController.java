package com.musiql.webapplication.controller;

import com.musiql.webapplication.model.dto.AlbumDto;
import com.musiql.webapplication.model.dto.ArtistDto;
import com.musiql.webapplication.model.dto.SongDto;
import com.musiql.webapplication.service.MusicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class MusicController {

    private MusicService musicService;

    @GetMapping("/song")
    public ResponseEntity<List<SongDto>> getSongs(@RequestParam String songName) {
        return musicService.getSongs(songName.replace(" ", "_"));
    }

    @GetMapping("/album")
    public ResponseEntity<AlbumDto> getAlbums(@RequestParam String albumName) {
        return musicService.getAlbum(albumName.replace(" ", "_"));
    }

    @GetMapping("/artist")
    public ResponseEntity<ArtistDto> getArtists(@RequestParam String artistName) {
        return musicService.getArtist(artistName.replace(" ", "_"));
    }
}
