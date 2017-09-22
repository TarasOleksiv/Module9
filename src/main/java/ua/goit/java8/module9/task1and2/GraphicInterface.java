package ua.goit.java8.module9.task1and2;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import ua.goit.java8.module9.utils.DateUtils;
import ua.goit.java8.module9.youtube.entities.Search;
import ua.goit.java8.module9.youtube.entities.SearchResponse;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Taras on 21.09.2017.
 */
public class GraphicInterface {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 825;
    private Stage primaryStage;
    private Pane root = new Pane();
    private Label labelTitleValue = new Label();
    private Label labelChannelTitleValue = new Label();
    private Label labelPublishedAtValue = new Label();
    private WebView webview = new WebView();
    private Button view = new Button();
    private Image image = null;
    private ImageView imageView = new ImageView(image);

    private DateUtils dateUtils = new DateUtils();
    private String videoId;
    private String url;

    private static final String URL = "http://www.youtube.com/embed/";
    private static final String AUTO_PLAY = "?autoplay=1";
    private static final String SEARCH_LINK = "https://www.googleapis.com/youtube/v3/search";
    private static final String MY_KEY = "AIzaSyDwu_AH-9_PNHCKIiIzJ-uqXGwNWOfAURw";


    public GraphicInterface(Stage primaryStage) {
        this.primaryStage = primaryStage;
        draw(primaryStage);
    }

    public void draw(Stage primaryStage) {
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setTitle("YouTube Search");

        // текстове поле для пошуку відео
        TextField keyWord = new TextField();
        keyWord.setTranslateX(100);
        keyWord.setTranslateY(20);
        keyWord.setPrefWidth(300);
        keyWord.setText("Mark Knopfler");
        root.getChildren().add(keyWord);

        // кнопка пошуку
        Button search = new Button();
        search.setTranslateX(20);
        search.setTranslateY(20);
        search.setPrefWidth(60);
        search.setText("Search");
        search.setOnMouseClicked(event -> {
            new Thread(()->{
                try {
                    Platform.runLater(()->{
                        view.setDisable(true);
                        clearLabels();
                        webview.getEngine().load(null);
                    });

                    getSearch(keyWord.getText());

                    new Thread(()->{
                        try {
                            drawImage(new URL(url));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    Platform.runLater(()->{
                        view.setDisable(false);
                    });
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        root.getChildren().add(search);

        // область для трансляції відео
        webview.setTranslateX(100);
        webview.setTranslateY(100);
        webview.setPrefWidth(700);
        webview.setPrefHeight(500);
        root.getChildren().add(webview);

        Label labelTitle = new Label();
        labelTitle.setTranslateX(100);
        labelTitle.setTranslateY(650);
        labelTitle.setPrefWidth(150);
        labelTitle.setFont(Font.font(null, FontWeight.BOLD, 14));
        labelTitle.setText("Назва відео:");
        root.getChildren().add(labelTitle);

        Label labelChannelTitle = new Label();
        labelChannelTitle.setTranslateX(100);
        labelChannelTitle.setTranslateY(680);
        labelChannelTitle.setPrefWidth(150);
        labelChannelTitle.setFont(Font.font(null, FontWeight.BOLD, 14));
        labelChannelTitle.setText("Назва каналу:");
        root.getChildren().add(labelChannelTitle);

        Label labelPublishedAt = new Label();
        labelPublishedAt.setTranslateX(100);
        labelPublishedAt.setTranslateY(710);
        labelPublishedAt.setPrefWidth(150);
        labelPublishedAt.setFont(Font.font(null, FontWeight.BOLD, 14));
        labelPublishedAt.setText("Дата публікації:");
        root.getChildren().add(labelPublishedAt);

        // назва відео
        labelTitleValue.setTranslateX(250);
        labelTitleValue.setTranslateY(650);
        labelTitleValue.setPrefWidth(300);
        labelTitleValue.setFont(Font.font(null, FontWeight.NORMAL, 14));
        labelTitleValue.setText("");
        root.getChildren().add(labelTitleValue);

        // назва каналу
        labelChannelTitleValue.setTranslateX(250);
        labelChannelTitleValue.setTranslateY(680);
        labelChannelTitleValue.setPrefWidth(300);
        labelChannelTitleValue.setFont(Font.font(null, FontWeight.NORMAL, 14));
        labelChannelTitleValue.setText("");
        root.getChildren().add(labelChannelTitleValue);

        // дата публікації
        labelPublishedAtValue.setTranslateX(250);
        labelPublishedAtValue.setTranslateY(710);
        labelPublishedAtValue.setPrefWidth(300);
        labelPublishedAtValue.setFont(Font.font(null, FontWeight.NORMAL, 14));
        labelPublishedAtValue.setText("");
        root.getChildren().add(labelPublishedAtValue);

        // кнопка перегляду відео
        view.setTranslateX(740);
        view.setTranslateY(650);
        view.setPrefWidth(60);
        view.setText("View");
        view.setDisable(true);
        view.setOnMouseClicked(event -> {
            new Thread(()->{
                Platform.runLater(()->{
                    view.setDisable(true);
                    showVideo(videoId);
                });
            }).start();
        });
        root.getChildren().add(view);

        // поле виводу зображення
        imageView.setTranslateX(600);
        imageView.setTranslateY(650);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        root.getChildren().add(imageView);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // пошук відео
    private void getSearch(String q) throws UnirestException {
        HttpResponse<SearchResponse> response = Unirest.get(SEARCH_LINK)
                .queryString("part", "snippet")
                .queryString("q", q)
                .queryString("maxResults", "1")
                .queryString("type", "video")
                .queryString("key", MY_KEY)
                .asObject(SearchResponse.class);
        SearchResponse searchResponse = response.getBody();
        final Search item = searchResponse.items.get(0);
        Platform.runLater(()-> {
            labelTitleValue.setText("\"" + item.snippet.title + "\"");
            labelChannelTitleValue.setText("\"" + item.snippet.channelTitle + "\"");
            labelPublishedAtValue.setText("\"" + dateUtils.convertDateToString(item.snippet.publishedAt) + "\"");
        });
        videoId = item.id.videoId;
        url = item.snippet.thumbnails.medium.url;   // шукаєм зображення роздільної здатності medium
        //System.out.println(item.snippet.thumbnails.medium.url);
    }

    // запуск відео
    private void showVideo(String videoId){
        webview.getEngine().load(
                URL+videoId+AUTO_PLAY
        );
    }

    // вивід зображення
    private void drawImage(URL url) throws IOException {
        image = new Image(url.openStream());
        Platform.runLater(() -> {
            imageView.setImage(image);
        });

    }

    private void clearLabels(){
        labelTitleValue.setText("");
        labelChannelTitleValue.setText("");
        labelPublishedAtValue.setText("");
        imageView.setImage(null);
    }

}