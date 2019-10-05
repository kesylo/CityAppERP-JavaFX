package sample.Controller;

import com.jfoenix.controls.JFXSpinner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.function.ToIntFunction;

public class DialogController<P> {

    private Task animationWorker;
    private Task<Integer> taskWorker;



    //private final ProgressIndicator progressIndicator = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
    private final JFXSpinner progressIndicator = new JFXSpinner(JFXSpinner.INDETERMINATE_PROGRESS);
    private final Stage dialog = new Stage(StageStyle.UNDECORATED);
    private final Label label = new Label();
    private final Group root = new Group();
    private final Scene scene = new Scene(root, 330, 120, Color.web("#212121"));
    private final BorderPane mainPane = new BorderPane();
    private final VBox vbox = new VBox();

    /** Placing a listener on this list allows to get notified BY the result when the task has finished. */
    private ObservableList<Integer> resultNotificationList= FXCollections.observableArrayList();

    private Integer resultValue;

    /**
     *
     */
    public DialogController(Window owner, String label) {
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setResizable(false);
        this.label.setText(label);
    }

    /**
     *
     */
    public void exec(P parameter, ToIntFunction func) {
        setupDialog();
        setupAnimationThread();
        setupWorkerThread(parameter, func);
    }

    /**
     *
     */
    private void setupDialog() {
        root.getChildren().add(mainPane);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinSize(330, 120);
        vbox.getChildren().addAll(label,progressIndicator);
        mainPane.setTop(vbox);
        dialog.setScene(scene);


        dialog.setOnHiding(event -> { /* Gets notified when task ended, but BEFORE
                     result value is attributed. Using the observable list above is
                     recommended. */ });

        dialog.show();
    }

    /**
     *
     */
    private void setupAnimationThread() {

        animationWorker = new Task() {
            @Override
            protected Object call() {
                /*
                This is activated when we have a "progressing" indicator.
                This thread is used to advance progress every XXX milliseconds.
                In case of an INDETERMINATE_PROGRESS indicator, it's not of use.
                for (int i=1;i<=100;i++) {
                    Thread.sleep(500);
                    updateMessage();
                    updateProgress(i,100);
                }
                */
                return true;
            }
        };

        progressIndicator.setProgress(0);
        progressIndicator.progressProperty().unbind();
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Poppins-Regular", 18));
        progressIndicator.progressProperty().bind(animationWorker.progressProperty());

        animationWorker.messageProperty().addListener((observable, oldValue, newValue) -> {
            // Do something when the animation value ticker has changed
        });

        new Thread(animationWorker).start();
    }

    /**
     *
     */
    private void setupWorkerThread(P parameter, ToIntFunction<P> func) {

        taskWorker = new Task<Integer>() {
            @Override
            public Integer call() {
                return func.applyAsInt(parameter);
            }
        };

        EventHandler<WorkerStateEvent> eh = event -> {
            animationWorker.cancel(true);
            progressIndicator.progressProperty().unbind();
            dialog.close();

            try {
                resultValue = taskWorker.get();
                resultNotificationList.add(resultValue);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        taskWorker.setOnSucceeded(eh);
        taskWorker.setOnFailed(eh);

        new Thread(taskWorker).start();
    }

}
