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
    public ResponseEntity<ArtistDto> getArtists(String artistName) {
        Model model = repository.getArtistModel(artistName);
        ArtistDto artist = ArtistDto.builder().build();

        String resURL = "http://dbpedia.org/data/XXX.ttl"
                .replace("XXX", artistName)
                .replace("data", "resource")
                .replace(".ttl", "");

        Resource performer = model.getResource(resURL);

        artist.setName(artistName);

        artist.setBio(performer.getProperty(RDFS.comment, "en").getObject().toString());

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

        artist.setArtists(new ArrayList<>());
        StmtIterator activeMembers = performer.listProperties(new PropertyImpl("http://dbpedia.org/ontology/bandMember"));
        if (activeMembers.hasNext()) {
            while (activeMembers.hasNext()) {
                Statement member = activeMembers.nextStatement();
                String fullUrl = member.getObject().toString();
                String[] stringSplit = fullUrl.split("/");
                System.out.println(stringSplit[stringSplit.length - 1]);
                artist.getArtists().add(stringSplit[stringSplit.length - 1].replace("_", " "));
            }
        }

        artist.setGenre(new ArrayList<>());
        StmtIterator genres = performer.listProperties(new PropertyImpl("http://dbpedia.org/ontology/genre"));
        if (genres.hasNext()) {
            while (genres.hasNext()) {
                Statement genre = genres.nextStatement();
                String fullUrl = genre.getObject().toString();
                String[] stringSplit = fullUrl.split("/");
                System.out.println(stringSplit[stringSplit.length - 1]);

                artist.getGenre().add(stringSplit[stringSplit.length - 1].replace("_", " "));
            }
        }

        artist.setRecordLabel(new ArrayList<>());
        StmtIterator recordLabels = performer.listProperties(new PropertyImpl("http://dbpedia.org/ontology/recordLabel"));
        if (recordLabels.hasNext()) {
            while (recordLabels.hasNext()) {
                Statement recordLabel = recordLabels.nextStatement();
                String fullUrl = recordLabel.getObject().toString();
                String[] ss = fullUrl.split("/");
                artist.getRecordLabel().add(ss[ss.length - 1].replace("_", " "));
            }
        }

        artist.setThumbnail(performer.getProperty(FOAF.depiction).getObject().toString());

        artist.setHomepage("");
        if (performer.hasProperty(FOAF.homepage))
            artist.setHomepage(performer.getProperty(FOAF.homepage).getObject().toString());

        artist.setAlbums(repository.getAlbumsOfArtist(artist.getName()));

        return new ResponseEntity<ArtistDto>(artist, HttpStatus.ACCEPTED);
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
