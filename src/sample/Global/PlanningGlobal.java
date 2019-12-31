package sample.Global;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Planning;
import sample.Model.Report;
import sample.Model.User;

public class PlanningGlobal {

    private static ObservableList<Planning> planningsList = FXCollections.observableArrayList();
    private static ObservableList<Report> reportList = FXCollections.observableArrayList();

    public static ObservableList<Report> getReportList() {
        return reportList;
    }

    public static void setReportList(ObservableList<Report> reportList) {
        PlanningGlobal.reportList = reportList;
    }

    public static ObservableList<Planning> getPlanningsList() {
        return planningsList;
    }

    public static void setPlanningsList(ObservableList<Planning> planningsList) {
        PlanningGlobal.planningsList = planningsList;
    }
}
