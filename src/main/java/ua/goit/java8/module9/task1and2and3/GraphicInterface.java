package ua.goit.java8.module9.task1and2and3;

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

/**
 * Created by Taras on 21.09.2017.
 */

public class GraphicInterface {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 825;
    private Stage primaryStage;
    private Pane root = new Pane();

    private Label labelTitleValue = new Label();
    private Label labelChannelTitleValue = new Label();
    private Label labelPublishedAtValue = new Label();
    private Label labelCount = new Label();
    private TextField keyWord = new TextField();
    private TextField textMaxResults = new TextField();
    private TextField textDaysPublished = new TextField();
    private Button view = new Button();
    private Button next = new Button();
    private Image image = null;
    private ImageView imageView = new ImageView(image);
    private WebView webview = new WebView();


    public GraphicInterface(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void draw() {

        InterfaceOperations interfaceOperations = new InterfaceOperations(this);

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setTitle("YouTube Search");

        // текстове поле для пошуку відео за ключовим словом
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
        textDaysPublished.setTranslateX(750);
        textDaysPublished.setTranslateY(50);
        textDaysPublished.setPrefWidth(50);
        textDaysPublished.setText("300");
        textDaysPublished.setVisible(false);
        root.getChildren().add(textDaysPublished);


        // кнопка пошуку відео
        Button search = new Button();
        search.setTranslateX(20);
        search.setTranslateY(20);
        search.setPrefWidth(60);
        search.setText("Search");
        search.setOnMouseClicked(event -> {
            new Thread(interfaceOperations::searchVideo).start();
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
            new Thread(interfaceOperations::showNext).start();
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
                    interfaceOperations.showVideo();
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

    public Label getLabelTitleValue(){return labelTitleValue;}
    public Label getLabelChannelTitleValue(){return labelChannelTitleValue;}
    public Label getLabelPublishedAtValue(){return labelPublishedAtValue;}
    public Label getLabelCount(){return labelCount;}
    public TextField getKeyWord(){return keyWord;}
    public TextField getTextMaxResults(){return textMaxResults;}
    public TextField getTextDaysPublished(){return textDaysPublished;}
    public Button getView(){return view;}
    public Button getNext(){return next;}
    public Image getImage(){return image;}
    public ImageView getImageView(){return imageView;}
    public WebView getWebview(){return webview;}

}