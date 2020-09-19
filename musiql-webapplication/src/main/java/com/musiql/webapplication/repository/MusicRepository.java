package com.musiql.webapplication.repository;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFParser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class MusicRepository {

    public Model getModel(String name) {
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
        while (resultSet.hasNext()) {
            QuerySolution querySolution = resultSet.nextSolution();
            String[] ss = querySolution.toString().split("\"");
            albums.add(ss[1]);
        }

        return albums;
    }

    public List<String> getSongsOfAlbum(String album) {
        ArrayList<String> songs = new ArrayList<>();

        String queryString = "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                "PREFIX dbr: <http://dbpedia.org/resource/>" +
                "select distinct ?song_name\n" +
                "where{\n" +
                "<ALBUM> dbp:title ?song.\n" +
                "?song dbp:thisSingle ?song_name\n" +
                "}";
        queryString = queryString.replace("ALBUM", album);
        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("https://dbpedia.org/sparql", query);
        ResultSet resultSet = queryExecution.execSelect();
        while (resultSet.hasNext()) {
            QuerySolution querySolution = resultSet.nextSolution();

            String[] ss = querySolution.toString().split("\"");
            songs.add(ss[2].replace("\\", ""));
        }

        return songs;
    }
}