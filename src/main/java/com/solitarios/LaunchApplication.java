package com.solitarios;

import com.solitarios.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LaunchApplication extends Application {
    private double minWidth = 300;
    private double minHeight = 420;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMinHeight(minHeight);
        stage.setMinWidth(minWidth);

//        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                System.out.println("height newValue = " + newValue);
//                stage.setWidth(newValue.doubleValue() / 1.4);
//            }
//        });
//



        FXMLLoader fxmlLoader = new FXMLLoader(LaunchApplication.class.getResource("view/main-view.fxml"));
        fxmlLoader.setControllerFactory(t -> new MainController(stage));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CalculatorFx");
        stage.setScene(scene);
        stage.show();
    }
}
