package ua.goit.java8.module9.youtube.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Taras on 21.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Thumbnails {
    // використовуєм лише medium
    public Medium medium;
}
