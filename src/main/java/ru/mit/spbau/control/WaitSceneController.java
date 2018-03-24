package ru.mit.spbau.control;

import javafx.scene.paint.Color;
import ru.mit.spbau.model.WaitTask;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.WaitConnectionSceneView;

/**
 * Controller for {@link WaitConnectionSceneView}
 */
public final class WaitSceneController extends SceneController {

    @NotNull private final WaitTask task;
    private WaitConnectionSceneView view;

    public WaitSceneController(@NotNull GeneralController controller, @NotNull WaitTask task) {
        super(controller);
        this.task= task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startControl() {
        view = new WaitConnectionSceneView(this);
        controller.getSceneManager().showNextScene(view);

        task.registerLog(this::showInfo);
        task.registerNotifyFail(this::notifyConnectionFail);
        task.registerNotifySuccess(this::notifyConnectionSuccess);
        task.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopControl() {
        /*
        this will close connection and implicitly invoke {@link WaitSceneController#notifyConnectionFail()}
         */
        task.stop();
    }

    /**
     * notify that connection was established successfully
     */
    public void notifyConnectionSuccess() {
        runOnPlatform(() -> view.log(Color.GREEN, "connection established, going to chat"));
        try {
            // just tell it to user so he can notice it
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        runOnPlatform(this::goToChat);
    }

    /**
     * notify that connection establishing failed
     */
    public void notifyConnectionFail() {
        runOnPlatform(() -> view.log(Color.FIREBRICK, "failed to establish connection"));
        try {
            // just tell it to user so he can notice it
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        runOnPlatform(() -> controller.getSceneManager().back());
    }

    /**
     * show some info under progress bar in blue color
     * @param info info to show
     */
    public void showInfo(String info) {
        runOnPlatform(() -> view.log(Color.BLUE, info));
    }

    /**
     * action for `back` button
     */
    public void back() {
        stopControl();
        controller.getSceneManager().back();
    }

    private void goToChat() {
        final ChatSceneController chatSceneController = new ChatSceneController(controller, task.getConnection());
        chatSceneController.startControl();
    }

}
