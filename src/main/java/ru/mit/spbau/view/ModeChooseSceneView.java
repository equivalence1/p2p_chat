package ru.mit.spbau.view;

import ru.mit.spbau.control.ModeChooseSceneController;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Screen to choose mode -- host or connect to remote host
 */
public final class ModeChooseSceneView implements SceneBuilder {

    @NotNull private final ModeChooseSceneController controller;

    private BorderPane pane;
    private VBox center;

    public ModeChooseSceneView(@NotNull ModeChooseSceneController controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scene build() {
        createPane();
        addPaneContent();

        final double width = controller.getCurrentWidth();
        final double height = controller.getCurrentHeight();

        return new Scene(pane, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveInHistory() {
        return true;
    }

    private void createPane() {
        pane = new BorderPane();
    }

    private void addPaneContent() {
        center = new VBox();
        center.setSpacing(10d);

        addHeader();
        addHostBtn();
        addConnectBtn();
        pane.setCenter(center);

        addBack();
    }

    private void addHeader() {
        final Text sceneTitle = new Text("Choose your mode");

        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        center.getChildren().add(inHBox(sceneTitle, Pos.CENTER));
    }

    private void addHostBtn() {
        final Button btn = new Button("host");

        btn.setOnAction(this::host);
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btn.defaultButtonProperty().bind(btn.focusedProperty());

        center.getChildren().add(inHBox(btn, Pos.CENTER));
    }

    private void addConnectBtn() {
        final Button btn = new Button("connect");

        btn.setOnAction(this::connect);
        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btn.defaultButtonProperty().bind(btn.focusedProperty());

        center.getChildren().add(inHBox(btn, Pos.CENTER));
    }

    private void addBack() {
        final Button back = new Button("back");

        back.setOnAction((actionEvent) -> controller.back()); // do nothing for now

        pane.setTop(inHBox(back, Pos.TOP_RIGHT));
    }

    private HBox inHBox(Node n, Pos alignment) {
        final HBox box = new HBox();

        box.setAlignment(alignment);
        box.getChildren().add(n);

        return box;
    }

    private void host(ActionEvent actionEvent) {
        controller.host();
    }

    private void connect(ActionEvent actionEvent) {
        controller.client();
    }

}
