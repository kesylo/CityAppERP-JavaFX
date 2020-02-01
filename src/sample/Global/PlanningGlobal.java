package sample.Global;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Model.Planning;
import sample.Model.PlanningReport;

public class PlanningGlobal {

    private static ObservableList<Planning> planningsList = FXCollections.observableArrayList();
    private static ObservableList<PlanningReport> planningReportList = FXCollections.observableArrayList();

    public static ObservableList<PlanningReport> getPlanningReportList() {
        return planningReportList;
    }

    public static void setPlanningReportList(ObservableList<PlanningReport> planningReportList) {
        PlanningGlobal.planningReportList = planningReportList;
    }

    public static ObservableList<Planning> getPlanningsList() {
        return planningsList;
    }

    public static void setPlanningsList(ObservableList<Planning> planningsList) {
        PlanningGlobal.planningsList = planningsList;
    }
}
