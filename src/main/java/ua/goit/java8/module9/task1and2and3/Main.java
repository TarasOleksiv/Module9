package ua.goit.java8.module9.task1and2and3;

import javafx.application.Application;
import javafx.stage.Stage;
import ua.goit.java8.module9.utils.MyObjectMapper;

/**
 * Created by Taras on 21.09.2017.
 */
public class Main extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MyObjectMapper myObjectMapper = new MyObjectMapper();
        GraphicInterface graphicInterface = new GraphicInterface(primaryStage);
    }
}