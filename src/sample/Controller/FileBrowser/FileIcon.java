package sample.Controller.FileBrowser;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;

class FileIcon {

    private File file;

    FileIcon(File f) {
        this.file = f;
    }

    ImageView getImageView(){
        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file);

        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView iv = new ImageView(fxImage);
        iv.setPreserveRatio(true);
        //iv.setSmooth(true);
        iv.setFitWidth(30);
        return iv;
    }
}