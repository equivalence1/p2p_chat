package ru.mit.spbau.control;

import javafx.scene.paint.Color;
import ru.mit.spbau.model.RemoteConnectionTask;
import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.ChatSceneView;
import ru.mit.spbau.view.ConnectToSceneView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for {@link ChatSceneView}.
 */
public final class ConnectToSceneController extends SceneController {

    @NotNull private static final Logger LOGGER = Logger.getLogger(ConnectToSceneController.class.getName());

    private ConnectToSceneView view;

    public ConnectToSceneController(@NotNull GeneralController controller) {
        super(controller);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startControl() {
        view = new ConnectToSceneView(this);
        controller.getSceneManager().showNextScene(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopControl() {

    }

    /**
     * action for `back` button.
     * goes to previous screen in history
     */
    public void back() {
        controller.getSceneManager().back();
    }

    /**
     * action for `connect` button
     * @param hostString host which user entered
     * @param portString port which user entered
     */
    public void connect(@NotNull String hostString, @NotNull String portString) {
        if (hostString.equals("")) {
            hostString = "127.0.0.1";
        }

        final int port;
        try {
            port = Integer.parseInt(portString);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "bad port", e);
            view.log(Color.FIREBRICK, "port is not valid");
            return;
        }

        controller.getUserInfo().setHost(hostString);
        controller.getUserInfo().setPort(port);

        final RemoteConnectionTask waitTask = new RemoteConnectionTask(controller.getUserInfo().getHost(),
                controller.getUserInfo().getPort());
        final WaitSceneController waitSceneController = new WaitSceneController(controller, waitTask);
        waitSceneController.startControl();
    }

}
