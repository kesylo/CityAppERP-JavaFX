package sample.animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;


public class Shaker {
    private TranslateTransition translateTransition;

    // a node is a ui elm: btn, textview

    public Shaker(Node node) {
        translateTransition = new TranslateTransition(Duration.millis(100), node);
        translateTransition.setFromX(0f);
        translateTransition.setByX(20f);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

    }

    public void shake() {
        translateTransition.playFromStart();
    }
}
