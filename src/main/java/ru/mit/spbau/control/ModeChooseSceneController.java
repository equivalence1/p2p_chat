package ru.mit.spbau.control;

import ru.mit.spbau.model.HostConnectionTask;
import ru.mit.spbau.model.WaitTask;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.ModeChooseSceneView;

/**
 * Controller for {@link ModeChooseSceneView}
 */
public final class ModeChooseSceneController extends SceneController {

    public ModeChooseSceneController(@NotNull GeneralController controller) {
        super(controller);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startControl() {
        final ModeChooseSceneView view = new ModeChooseSceneView(this);
        controller.getSceneManager().showNextScene(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopControl() {

    }

    /**
     * action for `host` button
     */
    public void host() {
        final WaitTask task = new HostConnectionTask();
        final WaitSceneController waitSceneController = new WaitSceneController(controller, task);
        waitSceneController.startControl();
    }

    /**
     * action for `connect` button
     */
    public void client() {
        final ConnectToSceneController connectToSceneController = new ConnectToSceneController(controller);
        connectToSceneController.startControl();
    }

    /**
     * action for `back` button
     */
    public void back() {
        controller.getSceneManager().back();
    }

}
