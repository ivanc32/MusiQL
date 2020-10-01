package com.musiql.webapplication.service;

import com.musiql.webapplication.model.dto.AlbumDto;
import com.musiql.webapplication.model.dto.ArtistDto;
import com.musiql.webapplication.model.dto.SongDto;
import com.musiql.webapplication.repository.MusicRepository;
import lombok.AllArgsConstructor;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MusicServiceImpl implements MusicService {

    private MusicRepository repository;

    @Override
    public ResponseEntity<ArtistDto> getArtist(String artistName) {
        Model model = repository.getModel(artistName);
        ArtistDto artist = ArtistDto.builder().build();

        String resURL = "http://dbpedia.org/data/XXX.ttl"
                .replace("XXX", artistName)
                .replace("data", "resource")
                .replace(".ttl", "");

        Resource performer = model.getResource(resURL);

        artist.setName(artistName.replace("_", " "));

        if (performer.hasProperty(RDFS.comment)) {
            artist.setBio(performer.getProperty(RDFS.comment, "en").getObject().toString());
        }

        artist.setYearFounded("");
        if (performer.hasProperty(new PropertyImpl("http://dbpedia.org/ontology/activeYearsStartYear"))) {
            artist.setYearFounded(performer.getProperty(new PropertyImpl("http://dbpedia.org/ontology/activeYearsStartYear"))
                    .getObject().toString().substring(0, 4));
        }

        artist.setYearDisbanded("");
        if (performer.hasProperty(new PropertyImpl("http://dbpedia.org/ontology/activeYearsEndYear"))) {
            artist.setYearDisbanded(performer.getProperty(new PropertyImpl("http://dbpedia.org/ontology/activeYearsEndYear"))
                    .getObject().toString().substring(0, 4));
        }

        artist.setArtists(propertyListToString("http://dbpedia.org/ontology/bandMember", performer));

        artist.setGenres(propertyListToString("http://dbpedia.org/ontology/genre", performer));

        artist.setRecordLabel(propertyListToString("http://dbpedia.org/ontology/recordLabel", performer));

        artist.setThumbnail(performer.getProperty(FOAF.depiction).getObject().toString());

        artist.setHomepage("");
        if (performer.hasProperty(FOAF.homepage))
            artist.setHomepage(performer.getProperty(FOAF.homepage).getObject().toString());

        artist.setAlbums(repository.getAlbumsOfArtist(artist.getName()));

        return new ResponseEntity<>(artist, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<List<SongDto>> getSongs(String songName) {
        return null;
    }

    @Override
    public ResponseEntity<AlbumDto> getAlbum(String albumName) {
        Model model = repository.getModel(albumName);
        AlbumDto album = AlbumDto.builder().build();

        String resURL = "http://dbpedia.org/data/XXX.ttl"
                .replace("XXX", albumName)
                .replace("data", "resource")
                .replace(".ttl", "");

        Resource albumResource = model.getResource(resURL);

        album.setName(albumName.replace("_", " "));

        album.setDescription("");
        if (albumResource.hasProperty(new PropertyImpl("http://dbpedia.org/ontology/abstract"))) {
            album.setDescription(albumResource.getProperty(new PropertyImpl("http://dbpedia.org/ontology/abstract"))
                    .getObject().toString());
        }

        album.setReleaseDate("");
        if (albumResource.hasProperty(new PropertyImpl("http://dbpedia.org/ontology/releaseDate"))) {
            album.setReleaseDate(albumResource.getProperty(new PropertyImpl("http://dbpedia.org/ontology/releaseDate"))
                    .getObject().toString().replaceAll("\\^\\^.*", ""));
        }

        album.setThumbnail("");
        if (albumResource.hasProperty(new PropertyImpl("http://dbpedia.org/ontology/thumbnail"))) {
            album.setThumbnail(albumResource.getProperty(new PropertyImpl("http://dbpedia.org/ontology/thumbnail"))
                    .getObject().toString());
        }

        album.setRuntime("");
        if (albumResource.hasProperty(new PropertyImpl("http://dbpedia.org/ontology/Work/runtime"))) {
            album.setRuntime(albumResource.getProperty(new PropertyImpl("http://dbpedia.org/ontology/Work/runtime"))
                    .getObject().toString().replaceAll("\\^\\^.*", ""));
        }

        album.setArtists(propertyListToString("http://dbpedia.org/ontology/artist", albumResource));
        album.setGenres(propertyListToString("http://dbpedia.org/ontology/genre", albumResource));
        album.setProducers(propertyListToString("http://dbpedia.org/ontology/producer", albumResource));
        album.setSongs(repository.getSongsOfAlbum(resURL));

        return new ResponseEntity<>(album, HttpStatus.ACCEPTED);
    }

    private List<String> propertyListToString(String url, Resource resource) {
        ArrayList<String> result = new ArrayList<>();

        StmtIterator iterator = resource.listProperties(new PropertyImpl(url));
        if (iterator.hasNext()) {
            while (iterator.hasNext()) {
                Statement item = iterator.nextStatement();
                String fullUrl = item.getObject().toString();
                String[] stringSplit = fullUrl.split("/");
                result.add(stringSplit[stringSplit.length - 1].replace("_", " "));
            }
        }

        return result;
    }
}
