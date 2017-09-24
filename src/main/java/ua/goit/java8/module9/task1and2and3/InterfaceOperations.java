package ua.goit.java8.module9.task1and2and3;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import ua.goit.java8.module9.utils.DateUtils;
import ua.goit.java8.module9.youtube.entities.Search;
import ua.goit.java8.module9.youtube.entities.SearchResponse;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Taras on 22.09.2017.
 */

public class InterfaceOperations {
    private DateUtils dateUtils = new DateUtils();
    private static final String URL = "http://www.youtube.com/embed/";
    private static final String AUTO_PLAY = "?autoplay=1";
    private static final String SEARCH_LINK = "https://www.googleapis.com/youtube/v3/search";
    private static final String MY_KEY = "AIzaSyDwu_AH-9_PNHCKIiIzJ-uqXGwNWOfAURw";

    private SearchResponse searchResponse;

    private String url;
    private int currentResult = -1;
    private int maxResults = 3;     // по замовчуванню, максимальна кількість - 50.
    private int publishedAfter = -3650;  // по замовчуванню, кількість днів що пройшла з моменту публікації

    private Label labelTitleValue;
    private Label labelChannelTitleValue;
    private Label labelPublishedAtValue;
    private Label labelCount;
    private TextField keyWord;
    private TextField textMaxResults;
    private TextField textDaysPublished;
    private WebView webview;
    private Button view;
    private Button next;
    private Image image;
    private ImageView imageView;
    private String videoId;

    public InterfaceOperations(GraphicInterface graphicInterface){
        this.labelTitleValue = graphicInterface.getLabelTitleValue();
        this.labelChannelTitleValue = graphicInterface.getLabelChannelTitleValue();
        this.labelPublishedAtValue = graphicInterface.getLabelPublishedAtValue();
        this.labelCount = graphicInterface.getLabelCount();
        this.textMaxResults = graphicInterface.getTextMaxResults();
        this.keyWord = graphicInterface.getKeyWord();
        this.textDaysPublished = graphicInterface.getTextDaysPublished();
        this.webview = graphicInterface.getWebview();
        this.view = graphicInterface.getView();
        this.next = graphicInterface.getNext();
        this.image = graphicInterface.getImage();
        this.imageView = graphicInterface.getImageView();
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
            textDaysPublished.setText(Integer.toString(-publishedAfter));
        });
        videoId = item.id.videoId;
        url = item.snippet.thumbnails.medium.url;   // шукаєм зображення роздільної здатності medium
        currentResult = i;

        if (url != null) {
            new Thread(() -> {
                try {
                    drawImage(new URL(url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // вивід зображення
    private void drawImage(URL url) throws IOException {
        image = new Image(url.openStream());
        Platform.runLater(() -> {
            imageView.setImage(image);
        });
    }

    // очистка полів на екрані
    private void clearFields(){
        labelTitleValue.setText("");
        labelChannelTitleValue.setText("");
        labelPublishedAtValue.setText("");
        labelCount.setText("");
        imageView.setImage(null);
    }

    // отримати наступний номер результату
    private int getNextResultNumber(int currentNumber, int maxNumber){
        return (currentNumber < maxNumber - 1)?currentNumber + 1:0;
    }

    // перевірка чи результат не null
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
            });
            url = null;
            videoId = null;
            return true;
        } else {
            return false;
        }
    }

    // перевірка чи стічка є числом
    private boolean isNumber(String string) {
        if (string == null) return false;
        return string.matches("^-?\\d+$");
    }

    // перевірка чи число є невід'ємне
    private boolean isNegative(Long n){
        return ((n < 0)?true:false);
    }

    // остаточна перевірка чи стрічка є невід'ємним числом
    private boolean checkString(String string){
        return ((isNumber(string)&&(!isNegative(Long.parseLong(string))))?true:false);
    }

    // запуск відео
    public void showVideo(){
        if (videoId != null){
            webview.getEngine().load(URL + videoId + AUTO_PLAY);
        }
    }

    // демонструє наступний результат пошуку
    public void showNext(){
        Platform.runLater(()->{
            view.setDisable(true);
            clearFields();
            webview.getEngine().load(null);
        });

        if (!checkNullResponse(searchResponse)) {
            showResult(searchResponse, getNextResultNumber(currentResult, searchResponse.items.size()));
            new Thread(()->{
                try {
                    drawImage(new URL(url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Platform.runLater(()->{
            view.setDisable(false);
        });
    }

    // пошук відео
    public void searchVideo(){
        Platform.runLater(()->{
            view.setDisable(true);
            next.setDisable(true);
            clearFields();
            webview.getEngine().load(null);
        });

        // перевірка чи в полях введено числа
        // якщо так, то здійснюєм пошук; якщо ні, пошук не здійснюєм і виводимо попередження
        if ((checkString(textMaxResults.getText())) && (checkString(textDaysPublished.getText()))){
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
        } else {
            if (!checkString(textMaxResults.getText())){
                textMaxResults.setText("!!!");
            }
            if (!checkString(textDaysPublished.getText())){
                textDaysPublished.setText("!!!");
            }
        }


        Platform.runLater(()->{
            view.setDisable(currentResult == -1);
            next.setDisable(currentResult == -1);
        });
    }
}
