package com.musiql.webapplication.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlbumDto {
    private String name;

    private String description;

    private List<String> artists;

    private List<String> genres;

    private List<String> producers;

    private String releaseDate;

    private String runtime;

    private String thumbnail;

    private List<String> songs;
}

