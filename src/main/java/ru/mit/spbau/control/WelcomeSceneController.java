package ru.mit.spbau.control;

import org.jetbrains.annotations.NotNull;
import ru.mit.spbau.view.WelcomeSceneView;

/**
 * Controller for {@link WelcomeSceneView}
 */
public final class WelcomeSceneController extends SceneController {

    private WelcomeSceneView view;

    public WelcomeSceneController(@NotNull GeneralController controller) {
        super(controller);
    }

    @Override
    public void startControl() {
        view = new WelcomeSceneView(this);
        controller.getSceneManager().showNextScene(view);
    }

    @Override
    public void stopControl() {
        // this can not be invoked
    }

    public void signIn(String name) {
        if (name.equals("")) {
            view.notifyError("User name can not be empty string");
        } else {
            controller.getUserInfo().setUserName(name);
            final ModeChooseSceneController nextSceneController = new ModeChooseSceneController(controller);
            nextSceneController.startControl();
        }
    }

}
