package ru.mit.spbau.control;

import ru.mit.spbau.common.UserInfo;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * This a `controller under controllers`
 * Starts the application (by delegating ru.mit.spbau.control
 * to welcome screen) and stores our ru.mit.spbau.common state
 * which is sceneManager + userInfo + primaryStage
 */
public final class GeneralController {

    @NotNull private final Stage primaryStage;
    @NotNull private final SceneManager sceneManager;
    @NotNull private final UserInfo userInfo;

    public GeneralController(@NotNull Stage primaryStage) {
        this.primaryStage = primaryStage;
        sceneManager = new SceneManagerImpl(this);
        userInfo = UserInfo.getUserInfo();
    }

    /**
     * Start GUI
     */
    public void start() {
        final WelcomeSceneController welcomeSceneController = new WelcomeSceneController(this);
        welcomeSceneController.startControl();
        primaryStage.show();
    }

    /**
     * get primary stage of application
     * @return primary stage
     */
    @NotNull
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * get our scene manager
     * @return scene manager
     */
    @NotNull
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * get current user info
     * @return user info
     */
    @NotNull
    public UserInfo getUserInfo() {
        return userInfo;
    }

}
