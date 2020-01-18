package sample.Global;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class FileBrowserGlobal {
    private static String explorerBasePath;
    private static String currentProcedure;

    public static String getCurrentProcedure() {
        return currentProcedure;
    }

    public static void setCurrentProcedure(String currentProcedure) {
        FileBrowserGlobal.currentProcedure = currentProcedure;
    }

    public static String getExplorerBasePath() {
        return explorerBasePath;
    }

    public static void setExplorerBasePath(String explorerBasePath) {
        FileBrowserGlobal.explorerBasePath = explorerBasePath;
    }

    public static void openFile(File file) {
        Desktop desktop = null;

        try {
            if (Desktop.isDesktopSupported()){
                desktop = Desktop.getDesktop();
            }
            if (desktop != null) {
                desktop.open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
