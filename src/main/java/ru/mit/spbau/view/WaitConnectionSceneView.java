package ru.mit.spbau.view;

import ru.mit.spbau.control.WaitSceneController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Screen with progress bar when you try to connect to remote host
 * or accept connection on local host
 */
public final class WaitConnectionSceneView implements SceneBuilder {

    @NotNull private final WaitSceneController controller;

    private BorderPane pane;
    private Text logArea;
    private VBox center;

    public WaitConnectionSceneView(@NotNull WaitSceneController controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scene build() {
        createPane();

        center = new VBox();
        center.setAlignment(Pos.CENTER);
        createProgress();
        createLog();
        addBack();
        pane.setCenter(center);

        final double sceneWidth = controller.getCurrentWidth();
        final double sceneHeight = controller.getCurrentHeight();

        return new Scene(pane, sceneWidth, sceneHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveInHistory() {
        return false;
    }

    /**
     * Show message to user
     * @param color color of message
     * @param msg content of message
     */
    public void log(Color color, String msg) {
        logArea.setFill(color);
        logArea.setText(msg);
    }

    private void createPane() {
        pane = new BorderPane();
    }

    private void createProgress() {
        final HBox hb = new HBox();
        final ProgressIndicator pin = new ProgressIndicator();

        pin.setProgress(-1);
        hb.getChildren().add(pin);
        hb.setAlignment(Pos.CENTER);

        center.getChildren().add(hb);
    }

    private void createLog() {
        final HBox hb = new HBox();
        logArea = new Text();

        hb.getChildren().add(logArea);
        hb.setAlignment(Pos.CENTER);

        center.getChildren().add(hb);
    }

    private void addBack() {
        final HBox hb = new HBox();
        final Button back = new Button("back");

        back.setOnAction((actionEvent) -> controller.back());
        back.defaultButtonProperty().bind(back.focusedProperty());
        hb.setAlignment(Pos.TOP_RIGHT);
        hb.getChildren().add(back);

        pane.setTop(hb);
    }

}
