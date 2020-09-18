package com.musiql.webapplication.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtistDto {
    private String name;

    private String bio;

    private List<String> artists;

    private String yearFounded;

    private String yearDisbanded;

    private List<String> genre;

    private List<String> recordLabel;

    private List<String> albums;

    private String thumbnail;

    private String homepage;
}

