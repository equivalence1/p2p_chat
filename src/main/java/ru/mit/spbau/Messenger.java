package ru.mit.spbau;

import ru.mit.spbau.control.GeneralController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Our main app class
 */
public final class Messenger extends Application {

    private GeneralController controller;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ru.mit.spbau.Messenger");
        controller = new GeneralController(primaryStage);
        controller.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        /*
        Actually, in case of stop we should close all connection and stop all tasks
        But in our case, as we dont need to save any state on disk or preform some
        notifications, we can just exit.
         */
        System.exit(0);
    }

}
