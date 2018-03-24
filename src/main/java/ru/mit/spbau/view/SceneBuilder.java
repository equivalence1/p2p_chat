package ru.mit.spbau.view;

import ru.mit.spbau.control.SceneManager;
import javafx.scene.Scene;

public interface SceneBuilder {

    /**
     * create new screen
     *
     * @return new scene
     */
    Scene build();

    /**
     * Tells if we should save this scene in {@link SceneManager} history
     * and be able to reach this screen using `back` button
     *
     * @return true if screen should be saved in history, false otherwise
     */
    boolean saveInHistory();

}
