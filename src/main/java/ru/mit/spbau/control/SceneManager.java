package ru.mit.spbau.control;

import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.SceneBuilder;

/**
 * This class controls which scene should be shown now.
 *
 * If user clicked on some button and we should change scene,
 * than current scene should call {@link SceneManagerImpl#showNextScene(SceneBuilder)}
 * passing it the next scene builder.
 *
 * If user clicked on `back` button, corresponding controller should
 * call {@link SceneManagerImpl#back()} method.
 *
 * (scene is a javafx analogue of screen)
 */
public interface SceneManager {

    /**
     * Shows next screen which will be build using passed builder.
     *
     * @param nextSceneBuilder next scene builder
     */
    void showNextScene(@NotNull SceneBuilder nextSceneBuilder);

    /**
     * Show scene before current
     */
    void back();

    /**
     * Returns current scene.
     * It may be helpful in order to obtain current window sizes.
     *
     * @return current scene
     */
    Scene getCurrentScene();

}
