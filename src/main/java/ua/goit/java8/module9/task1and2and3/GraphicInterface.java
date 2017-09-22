package ua.goit.java8.module9.task1and2and3;

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
    private Label labelCount = new Label();
    private TextField textMaxResults = new TextField();
    private WebView webview = new WebView();
    private Button view = new Button();
    private Button next = new Button();
    private Image image = null;
    private ImageView imageView = new ImageView(image);

    private SearchResponse searchResponse;

    private DateUtils dateUtils = new DateUtils();
    private String videoId;
    private String url;
    private int currentResult = -1;
    private int maxResults = 3;     // по замовчуванню, максимальна кількість - 50.
    private int publishedAfter = -3650;  // по замовчуванню, кількість днів що пройшла з моменту публікації

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

        // текстове поле для пошуку відео за ключовим словом
        TextField keyWord = new TextField();
        keyWord.setTranslateX(100);
        keyWord.setTranslateY(20);
        keyWord.setPrefWidth(300);
        keyWord.setText("Mark Knopfler");
        root.getChildren().add(keyWord);

        Label labelMaxResults = new Label();
        labelMaxResults.setTranslateX(620);
        labelMaxResults.setTranslateY(20);
        labelMaxResults.setPrefWidth(150);
        labelMaxResults.setFont(Font.font(null, FontWeight.BOLD, 14));
        labelMaxResults.setText("Макс. результатів:");
        labelMaxResults.setVisible(false);
        root.getChildren().add(labelMaxResults);

        // максимальна кількість результатів
        textMaxResults.setTranslateX(750);
        textMaxResults.setTranslateY(20);
        textMaxResults.setPrefWidth(50);
        textMaxResults.setText("3");
        textMaxResults.setVisible(false);
        root.getChildren().add(textMaxResults);

        Label labelDaysPublished = new Label();
        labelDaysPublished.setTranslateX(620);
        labelDaysPublished.setTranslateY(50);
        labelDaysPublished.setPrefWidth(150);
        labelDaysPublished.setFont(Font.font(null, FontWeight.BOLD, 14));
        labelDaysPublished.setText("Кількість днів:");
        labelDaysPublished.setVisible(false);
        root.getChildren().add(labelDaysPublished);

        // кількість днів з моменту публікації
        TextField textDaysPublished = new TextField();
        textDaysPublished.setTranslateX(750);
        textDaysPublished.setTranslateY(50);
        textDaysPublished.setPrefWidth(50);
        textDaysPublished.setText("300");
        textDaysPublished.setVisible(false);
        root.getChildren().add(textDaysPublished);


        // кнопка пошуку
        Button search = new Button();
        search.setTranslateX(20);
        search.setTranslateY(20);
        search.setPrefWidth(60);
        search.setText("Search");
        search.setOnMouseClicked(event -> {
            new Thread(()->{
                Platform.runLater(()->{
                    view.setDisable(true);
                    next.setDisable(true);
                    clearLabels();
                    webview.getEngine().load(null);
                });

                // maxResults повинно бути в проміжку [1;50]
                maxResults = Math.min(Math.max(Integer.parseInt(textMaxResults.getText()),1),50);
                // publishedAfter повинно бути < 0
                publishedAfter = Math.min(-Integer.parseInt(textDaysPublished.getText()),-1);

                try {
                    searchResponse = getResponse(keyWord.getText(), maxResults, publishedAfter);
                    if (!checkNullResponse(searchResponse)){
                        showResult(searchResponse,0);
                    } else {
                        currentResult = -1;
                    }
                } catch (UnirestException e) {
                    e.printStackTrace();
                }

                if (url != null) {
                    new Thread(() -> {
                        try {
                            drawImage(new URL(url));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
                Platform.runLater(()->{
                    view.setDisable(false);
                    next.setDisable(false);
                });
            }).start();
        });
        root.getChildren().add(search);

        // кнопка пошуку advanced
        Button advanced = new Button();
        advanced.setTranslateX(520);
        advanced.setTranslateY(20);
        advanced.setPrefWidth(80);
        advanced.setText("Advanced");
        advanced.setOnMouseClicked(event -> {
            labelMaxResults.setVisible(true);
            labelDaysPublished.setVisible(true);
            textMaxResults.setVisible(true);
            textDaysPublished.setVisible(true);
        });
        root.getChildren().add(advanced);

        // область для трансляції відео
        webview.setTranslateX(100);
        webview.setTranslateY(100);
        webview.setPrefWidth(700);
        webview.setPrefHeight(500);
        root.getChildren().add(webview);

        // лічильник результатів
        labelCount.setTranslateX(20);
        labelCount.setTranslateY(680);
        labelCount.setPrefWidth(60);
        labelCount.setFont(Font.font(null, FontWeight.BOLD, 14));
        labelCount.setText("");
        root.getChildren().add(labelCount);

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

        // кнопка гортання результатів
        next.setTranslateX(20);
        next.setTranslateY(650);
        next.setPrefWidth(60);
        next.setText("Next");
        next.setDisable(true);
        next.setOnMouseClicked(event -> {
            new Thread(()->{
                Platform.runLater(()->{
                    view.setDisable(true);
                    clearLabels();
                    webview.getEngine().load(null);
                });

                showResult(searchResponse,getNextResultNumber(currentResult,searchResponse.items.size()));

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
            }).start();
        });
        root.getChildren().add(next);

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

    // отримання результатів пошуку
    private SearchResponse getResponse(String q, int maxResults, int publishedAfter) throws UnirestException {
        HttpResponse<SearchResponse> response = Unirest.get(SEARCH_LINK)
                .queryString("part", "snippet")
                .queryString("q", q)
                .queryString("maxResults", maxResults)
                .queryString("publishedAfter", dateUtils.getRFC3339DateString(publishedAfter))
                .queryString("type", "video")
                .queryString("key", MY_KEY)
                .asObject(SearchResponse.class);
        return response.getBody();
    }

    // вивід i-го результату пошуку на екран
    private void showResult(SearchResponse searchResponse, int i){
        final Search item = searchResponse.items.get(i);
        final int count = i + 1;
        final int total = searchResponse.items.size();
        Platform.runLater(()-> {
            labelTitleValue.setText("\"" + item.snippet.title + "\"");
            labelChannelTitleValue.setText("\"" + item.snippet.channelTitle + "\"");
            labelPublishedAtValue.setText("\"" + dateUtils.convertDateToString(item.snippet.publishedAt) + "\"");
            labelCount.setText(count + " (з " + total + ")");
            textMaxResults.setText(Integer.toString(maxResults));
        });
        videoId = item.id.videoId;
        url = item.snippet.thumbnails.medium.url;   // шукаєм зображення роздільної здатності medium
        currentResult = i;
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
        labelCount.setText("");
        imageView.setImage(null);
    }

    private int getNextResultNumber(int currentNumber, int maxMunber){
        return (currentNumber < maxMunber - 1)?currentNumber + 1:0;
    }

    private boolean checkNullResponse(SearchResponse searchResponse){
        if ((searchResponse == null) || (searchResponse.items.size() == 0)){
            Platform.runLater(() -> {
                next.setDisable(true);
                view.setDisable(true);
                imageView.setImage(null);
                labelTitleValue.setText("");
                labelChannelTitleValue.setText("");
                labelPublishedAtValue.setText("");
                labelCount.setText("0 рез.");
                webview.getEngine().load(null);
                url = null;
            });
            return true;
        } else {
            return false;
        }
    }

}