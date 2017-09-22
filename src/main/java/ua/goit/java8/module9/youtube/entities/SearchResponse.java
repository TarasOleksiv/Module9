package ua.goit.java8.module9.youtube.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Taras on 21.09.2017.
 */

/*
видео => type = "video"
В результатах должно содержаться:
        Название видео => snippet.title (string)
        Название канала => snippet.channelTitle (string)
        Дата публикации => snippet.publishedAt
        Кнопка - View. При нажатии на которую воспроизводиться видео в окне программы. (потрібно id.videoId для відтворення відео)
*/

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
    public List<Search> items;
}
