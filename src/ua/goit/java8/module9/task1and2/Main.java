package ua.goit.java8.module9.task1and2;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Taras on 21.09.2017.
 */
public class Main extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GraphicInterface graphicInterface = new GraphicInterface(primaryStage);
    }
}