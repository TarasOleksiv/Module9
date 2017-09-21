package ua.goit.java8.module9.youtube.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by Taras on 21.09.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Snippet {
    public String title;
    public String channelTitle;
    //public String publishedAt;
    public Date publishedAt;
    public Thumbnails thumbnails;
}
