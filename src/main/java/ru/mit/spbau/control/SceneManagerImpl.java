package ru.mit.spbau.control;

import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.SceneBuilder;

import java.util.Stack;

/**
 * Our simple implementation of {@link SceneManager}
 */
public final class SceneManagerImpl implements SceneManager {

    @NotNull private final GeneralController controller;

    private Stack<SceneBuilder> builders;
    private SceneBuilder currentBuilder;

    public SceneManagerImpl(@NotNull GeneralController controller) {
        this.controller = controller;
        builders = new Stack<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showNextScene(@NotNull SceneBuilder nextSceneBuilder) {
        Scene nextScene = nextSceneBuilder.build();
        if (currentBuilder != null && currentBuilder.saveInHistory()) {
            builders.push(currentBuilder);
        }
        controller.getPrimaryStage().setScene(nextScene);
        currentBuilder = nextSceneBuilder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void back() {
        currentBuilder = builders.pop();
        controller.getPrimaryStage().setScene(currentBuilder.build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scene getCurrentScene() {
        return controller.getPrimaryStage().getScene();
    }
}
