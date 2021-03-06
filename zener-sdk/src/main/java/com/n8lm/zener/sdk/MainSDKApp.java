package com.n8lm.zener.sdk;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainSDKApp extends Application {

    private Stage primaryStage;
    private MainWindow root;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        root = new MainWindow();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Zener SDK");
        this.primaryStage.setX(bounds.getMinX());
        this.primaryStage.setY(bounds.getMinY());
        this.primaryStage.setWidth(bounds.getWidth());
        this.primaryStage.setHeight(bounds.getHeight());
        this.primaryStage.setMaximized(true);
        this.primaryStage.setScene(new Scene(root));
        this.primaryStage.show();
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //event.consume();
                //Main.this.primaryStage.setFullScreen(true);
            }
        });

        //root.setOpacity(0.5f);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
