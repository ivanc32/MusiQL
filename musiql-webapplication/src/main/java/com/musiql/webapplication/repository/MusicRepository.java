package com.musiql.webapplication.repository;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFParser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class MusicRepository {

    public Model getArtistModel(String name) {
        String URL = "http://dbpedia.org/data/XXX.ttl".replace("XXX", name);
        Model model = ModelFactory.createDefaultModel();
        RDFParser.source(URL).httpAccept("text/turtle").parse(model.getGraph());

        return model;
    }

    public List<String> getAlbumsOfArtist(String artist) {
        ArrayList<String> albums = new ArrayList<>();

        String queryString = "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                "PREFIX dbr: <http://dbpedia.org/resource/>" +
                "select distinct ?album_name ?year\n" +
                "where{\n" +
                "?album dbo:artist dbr:ARTIST.\n" +
                "?album dbp:thisAlbum ?album_name.\n" +
                "?album dbo:releaseDate ?year\n" +
                "}";
        queryString = queryString.replace("ARTIST", artist);
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("https://dbpedia.org/sparql", query);
        ResultSet resultSet = queryExecution.execSelect();
        System.out.println("Albums:\n");
        while (resultSet.hasNext()) {
            QuerySolution querySolution = resultSet.nextSolution();
            String[] ss = querySolution.toString().split("\"");
            albums.add(ss[1]);
        }

        return albums;
    }
}