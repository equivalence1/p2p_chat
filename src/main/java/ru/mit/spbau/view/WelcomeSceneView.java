package ru.mit.spbau.view;

import ru.mit.spbau.control.WelcomeSceneController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Initial screen which will prompt for user name
 */
public final class WelcomeSceneView implements SceneBuilder {

    private static final int SCENE_WIDTH = 400;
    private static final int SCENE_HIGH = 275;

    @NotNull private final WelcomeSceneController controller;

    private GridPane grid;

    private TextField userTextField;
    private Text logArea;
    private Button sigInBtn;

    public WelcomeSceneView(@NotNull WelcomeSceneController controller) {
        this.controller = controller;
    }

    /**
     * Show user and error in {@link WelcomeSceneView#logArea} area
     *
     * @param error message to show
     */
    public void notifyError(String error) {
        log(Color.FIREBRICK, error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveInHistory() {
        return true;
    }

    /**
     * Create initial welcome screen which will ask for user name.
     */
    @Override
    public Scene build() {
        createPane();
        addPaneContent();

        return new Scene(grid, SCENE_WIDTH, SCENE_HIGH);
    }

    private void log(Color color, String msg) {
        logArea.setFill(color);
        logArea.setText(msg);
    }

    private void createPane() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
    }

    private void addPaneContent() {
        addHeader();
        addUserForm();
        addSigIn();
        addLogArea();
    }

    private void addHeader() {
        final Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);
    }

    private void addUserForm() {
        final Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        userTextField = new TextField();
        userTextField.setPromptText("username");
        grid.add(userTextField, 1, 1);

        userTextField.setOnKeyPressed((key) ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                sigInBtn.fire();
            }
        });
    }

    private void addSigIn() {
        sigInBtn = new Button("Sign in");
        sigInBtn.setOnAction(this::signIn);
        sigInBtn.defaultButtonProperty().bind(sigInBtn.focusedProperty()); // make enter key act like mouse click

        final HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(sigInBtn);
        grid.add(hbBtn, 1, 4);
    }

    private void addLogArea() {
        logArea = new Text();
        grid.add(logArea, 1, 6);
    }

    private void signIn(ActionEvent actionEvent) {
        controller.signIn(userTextField.getText());
    }

}
