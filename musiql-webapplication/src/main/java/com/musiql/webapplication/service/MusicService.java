package com.musiql.webapplication.service;

import com.musiql.webapplication.model.dto.AlbumDto;
import com.musiql.webapplication.model.dto.ArtistDto;
import com.musiql.webapplication.model.dto.SongDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MusicService {

    ResponseEntity<List<SongDto>> getSongs(String songName);

    ResponseEntity<ArtistDto> getArtists(String artistName);

    ResponseEntity<List<AlbumDto>> getAlbums(String albumName);
}
