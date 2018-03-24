package ru.mit.spbau.control;

import ru.mit.spbau.common.UserInfo;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;

/**
 * Instances of this class ru.mit.spbau.control our screens
 */
public abstract class SceneController {

    @NotNull protected final GeneralController controller;

    protected SceneController(@NotNull GeneralController controller) {
        this.controller = controller;
    }

    /**
     * This method will be invoked as soon as user
     * wants to see corresponding screen.
     *
     * It should build screen and do all other stuff
     * it needs to (e.g. start some task in new thread)
     */
    public abstract void startControl();

    /**
     * This method will be invoked as soon as user
     * leaves this screen.
     */
    public abstract void stopControl();

    /**
     * get width of a current scene
     * It can be useful in order to save
     * same width through all the screens
     *
     * @return width of a current scene
     */
    public Double getCurrentWidth() {
        return controller.getPrimaryStage().getWidth();
    }

    /**
     * get height of a current scene
     * It can be useful in order to save
     * same height through all the screens
     *
     * @return height of a current scene
     */
    public Double getCurrentHeight() {
        return controller.getPrimaryStage().getHeight();
    }

    /**
     * Get all info which user provided us with
     *
     * @return user info
     */
    public UserInfo getUserInfo() {
        return controller.getUserInfo();
    }

    /**
     * In javafx only it's on UI thread can change GUI.
     * So, if some of my back-end threads wants to change UI
     * it should call this method.
     */
    protected void runOnPlatform(@NotNull Runnable runnable) {
        Platform.runLater(runnable);
    }

}
