package sample.Controller.FileBrowser;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;

public class TilesViewItem extends VBox {

    public File file;
    private String filePath;

    TilesViewItem(File file, String fileName) {
        super();
        this.file = file;
        this.filePath = file.getAbsolutePath();

        super.getChildren().addAll(new FileIcon(file).getImageView(), new Label(fileName));
        super.setAlignment(Pos.CENTER);
        super.setMaxSize(120,80);
        super.setPrefSize(120, 100);
    }

    @Override public String toString(){
        return filePath;
    }
}
