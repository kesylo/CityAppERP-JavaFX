package sample.Controller.FileBrowser;

//region Imports
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import sample.Global.FileBrowserGlobal;
import sample.Global.Global;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
//endregion

public class FileBrowserController implements Initializable {
    //region UI
    @FXML
    private Label txtHeading;

    @FXML
    private ScrollPane tilesScroll;

    @FXML
    private TilePane tilePane;

    @FXML
    private TextField filePath;

    @FXML
    private JFXButton btnBack;
    //endregion

    private String rootPath;
    private String limitPath;

    @FXML
    void logOut() {
        URL location = getClass().getResource("/sample/View/login.fxml");
        Global.logOut(location, btnBack);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtHeading.setText(FileBrowserGlobal.getCurrentProcedure());
        rootPath = FileBrowserGlobal.getExplorerBasePath();
        limitPath = FileBrowserGlobal.getExplorerBasePath();
        filePath.setText(rootPath);
        makeTilesView();

        tilePane.setVgap(5);
        tilePane.setHgap(10);
        tilePane.setPrefColumns(5);
        tilePane.setTileAlignment(Pos.CENTER);
        tilesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tilesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tilesScroll.setFitToHeight(true);
        tilesScroll.setFitToWidth(true);
        tilesScroll.setContent(tilePane);


        btnBack.setOnAction(event -> {
            if (rootPath.equals(limitPath)){
                Global.showInfoMessage("Limite de droits!",
                        "Votre compte ne vous permet pas d'accéder aux fichiers parents de ce répertoire.");
            }else {
                // go back
                File file = new File(rootPath);
                rootPath = file.getParentFile().getAbsolutePath();
                filePath.setText(rootPath);
                makeTilesView();
            }
        });
    }

    private void makeTilesView(){
        File file = new File(rootPath);

        if(!file.isFile()){
            File[] listOfFiles;
            listOfFiles = file.listFiles();

            tilePane.getChildren().clear();

            if(listOfFiles != null){
                for(File f: listOfFiles){
                    String filename = f.getName();
                    TilesViewItem tilesViewItem = new TilesViewItem(f, filename);

                    // handle click events in explorer
                    tilesViewItem.setOnMouseClicked(event -> {

                        File currentClickedFile = tilesViewItem.file;

                        if(event.getClickCount() == 2){
                            // check if file or dir
                            if (currentClickedFile.isFile()){
                                // it's a file
                                // open with corresponding app
                                FileBrowserGlobal.openFile(currentClickedFile);
                            }else {
                                // it's a dir
                                rootPath = tilesViewItem.toString();
                                filePath.setText(rootPath);
                                makeTilesView();
                            }

                        }
                    });
                    tilePane.getChildren().add(tilesViewItem);
                }
            }
        }
    }
}
