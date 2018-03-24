package ru.mit.spbau.view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import ru.mit.spbau.common.Proto;
import ru.mit.spbau.control.ChatSceneController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * Chat screen
 */
public final class ChatSceneView implements SceneBuilder {

    @NotNull private final ChatSceneController controller;

    private VBox center;
    private VBox messagesArea;
    private Text typingSatus;
    private ScrollPane pane;
    private TextField userInput;

    public ChatSceneView(@NotNull ChatSceneController controller) {
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scene build() {
        final BorderPane root = new BorderPane();

        center = new VBox(10);
        center.setAlignment(Pos.CENTER);
        center.setMaxHeight(Double.MAX_VALUE);

        createScrollPane();
        createWriteForm();
        createBack();

        typingSatus = new Text();

        root.setTop(typingSatus);
        root.setCenter(center);
        root.setBottom(userInput);

        final double width = controller.getCurrentWidth();
        final double height = controller.getCurrentHeight();

        return new Scene(root, width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveInHistory() {
        return false;
    }

    /**
     * display message which was send by us
     *
     * @param message message which was send
     */
    public void displayLocalMessage(@NotNull Proto.Message message) {
        displayMessage("-fx-background-color: #c4ffd7;", message); // light green color
    }

    /**
     * display message which we received
     *
     * @param message received message
     */
    public void displayRemoteMessage(@NotNull Proto.Message message) {
        displayMessage("-fx-background-color: #c4daff;", message); // light blue color
    }

    /**
     * Set status of second user typing
     * @param status true if other user is typing right now
     */
    public void setTypingStatus(boolean status) {
        if (status) {
            typingSatus.setText("User typing");
        } else {
            typingSatus.setText("");
        }
    }

    private void displayMessage(@NotNull String style, @NotNull Proto.Message message) {
        final double maxWidth = 400;

        final VBox messageBox = new VBox();

        final Label time = new Label();
        final Label name = new Label();
        final Label content = new Label();

        final Date date = new Date();
        date.setTime(message.getTime().getSeconds() * 1000);

        time.setText("(" + date.toString() + ") ");
        time.setWrapText(true);

        name.setText(message.getName() + ": ");
        name.setStyle("-fx-font-weight: bold;");
        name.setWrapText(true);

        content.setText(message.getContent());
        content.setWrapText(true);

        messageBox.getChildren().addAll(time, name, content);
        messageBox.setStyle(style);
        messageBox.setMaxWidth(maxWidth);

        messagesArea.getChildren().add(messageBox);
    }

    private void createScrollPane() {
        pane = new ScrollPane();
        messagesArea = new VBox(10);
        messagesArea.setMaxHeight(Double.MAX_VALUE);
        pane.setContent(messagesArea);
        pane.setMaxHeight(Double.MAX_VALUE);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.vvalueProperty().bind(messagesArea.heightProperty());

        center.getChildren().add(pane);
    }

    private void createWriteForm() {
        userInput = new TextField();
        userInput.setPromptText("Write message...");

        userInput.setOnKeyPressed((key) -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                controller.sendMessage(userInput.getText());
                userInput.setText("");
            } else {
                controller.notifyTyping();
            }
        });
    }

    private void createBack() {
        /*
            Pretend that I made it.
            Im too lazy for it (dont like to deal with GUI),
            so you need to close the app completely to start new chat.
         */
    }

}
