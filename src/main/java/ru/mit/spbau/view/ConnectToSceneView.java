package ru.mit.spbau.view;

import ru.mit.spbau.control.ConnectToSceneController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Screen with connection info
 */
public final class ConnectToSceneView implements SceneBuilder {

    @NotNull private final ConnectToSceneController controller;

    private BorderPane pane;
    private TextField hostForm;
    private TextField portForm;
    private Text logArea;
    private Button connectBtn;
    private VBox center;

    public ConnectToSceneView(@NotNull ConnectToSceneController controller) {
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

    /**
     * print message to log area
     * it's mainly needed to log from ru.mit.spbau.model
     *
     * @param color color which to use to print message
     * @param msg content of message
     */
    public void log(Color color, String msg) {
        logArea.setFill(color);
        logArea.setText(msg);
    }

    private void createPane() {
        pane = new BorderPane();
        pane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void addPaneContent() {
        center = new VBox();
        center.setSpacing(10d);
        addHeader();
        addHostForm();
        addPortForm();
        addConnectBtn();
        addLogArea();
        pane.setCenter(center);
        addBack();
    }

    private void addHeader() {
        final Text sceneTitle = new Text("Connect");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        center.getChildren().add(inHBox(Pos.CENTER, sceneTitle));
    }

    private void addHostForm() {
        final Label hostLabel = new Label("Remote host:");

        hostForm = new TextField();
        hostForm.setPromptText("127.0.0.1");
        hostForm.setOnKeyPressed((key) ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                portForm.requestFocus();
            }
        });
        hostForm.requestFocus();

        center.getChildren().add(inHBox(Pos.CENTER, hostLabel, hostForm));
    }

    private void addPortForm() {
        final Label portLabel = new Label("Remote port:");

        portForm = new TextField();
        portForm.setPromptText("1234");
        portForm.setOnKeyPressed((key) ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                connectBtn.fire();
            }
        });

        center.getChildren().add(inHBox(Pos.CENTER, portLabel, portForm));
    }

    private void addConnectBtn() {
        connectBtn = new Button("connect");
        connectBtn.setOnAction(this::connect);
        connectBtn.defaultButtonProperty().bind(connectBtn.focusedProperty());

        center.getChildren().add(inHBox(Pos.BOTTOM_RIGHT, connectBtn));
    }

    private void addLogArea() {
        logArea = new Text();

        center.getChildren().add(inHBox(Pos.CENTER, logArea));
    }

    private void addBack() {
        final Button back = new Button("back");
        back.setOnAction((actionEvent) -> controller.back());
        back.defaultButtonProperty().bind(back.focusedProperty());

        pane.setTop(inHBox(Pos.TOP_RIGHT, back));
    }

    private HBox inHBox(Pos alignment, Node... nodes) {
        final HBox box = new HBox(10);

        box.setAlignment(alignment);
        for (Node n : nodes) {
            box.getChildren().add(n);
        }

        return box;
    }

    private void connect(ActionEvent actionEvent) {
        controller.connect(hostForm.getText(), portForm.getText());
    }

}
