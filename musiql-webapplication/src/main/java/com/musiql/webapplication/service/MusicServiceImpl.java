package com.musiql.webapplication.service;

import com.musiql.webapplication.model.dto.AlbumDto;
import com.musiql.webapplication.model.dto.ArtistDto;
import com.musiql.webapplication.model.dto.SongDto;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFParser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class MusicServiceImpl implements MusicService {

    @Override
    public ResponseEntity<List<ArtistDto>> getArtists(String artistName) {
        String URL = String.format("http://dbpedia.org/data/%s.ttl", artistName);
        Model model = ModelFactory.createDefaultModel();
        RDFParser.source(URL).httpAccept("text/turtle").parse(model.getGraph());
        String resourceUrl = URL.replace("data", "resource").replace(".ttl", "");
    }

    @Override
    public ResponseEntity<List<SongDto>> getSongs(String songName) {
        return null;
    }

    @Override
    public ResponseEntity<List<AlbumDto>> getAlbums(String albumName) {
        return null;
    }
}
