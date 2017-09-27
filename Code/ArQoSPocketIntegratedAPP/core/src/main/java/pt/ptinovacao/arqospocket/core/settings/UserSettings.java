package pt.ptinovacao.arqospocket.core.settings;

/**
 * Created by pedro on 13/04/2017.
 */

public class UserSettings {

    private Boolean dashboard;

    private Boolean anomaly;

    private Boolean anomalyHistoric;

    private Boolean tests;

    private Boolean testsHistoric;

    private Boolean settings;

    private Boolean theArqosPocket;

    private String language;

    private Boolean managementSystemType;

    private Boolean managementSystemInManual;

    public Boolean getDashboard() {
        return dashboard;
    }

    public void setDashboard(Boolean dashboard) {
        this.dashboard = dashboard;
    }

    public Boolean getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(Boolean anomaly) {
        this.anomaly = anomaly;
    }

    public Boolean getAnomalyHistoric() {
        return anomalyHistoric;
    }

    public void setAnomalyHistoric(Boolean anomalyHistoric) {
        this.anomalyHistoric = anomalyHistoric;
    }

    public Boolean getTests() {
        return tests;
    }

    public void setTests(Boolean tests) {
        this.tests = tests;
    }

    public Boolean getTestsHistoric() {
        return testsHistoric;
    }

    public void setTestsHistoric(Boolean testsHistoric) {
        this.testsHistoric = testsHistoric;
    }

    public Boolean getSettings() {
        return settings;
    }

    public void setSettings(Boolean settings) {
        this.settings = settings;
    }

    public Boolean getTheArqosPocket() {
        return theArqosPocket;
    }

    public void setTheArqosPocket(Boolean theArqosPocket) {
        this.theArqosPocket = theArqosPocket;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getManagementSystemType() {
        return managementSystemType;
    }

    public void setManagementSystemType(Boolean managementSystemType) {
        this.managementSystemType = managementSystemType;
    }

    public Boolean getManagementSystemInManual() {
        return managementSystemInManual;
    }

    public void setManagementSystemInManual(Boolean managementSystemInManual) {
        this.managementSystemInManual = managementSystemInManual;
    }

    @Override
    public String toString() {
        return "UserSettings{" + ", dashboard=" + dashboard + ", anomaly=" + anomaly
                + ", anomalyHistoric=" + anomalyHistoric + ", tests=" + tests + ", testsHistoric=" + testsHistoric +
                ", settings=" + settings + ", theArqosPocket=" + theArqosPocket +
                ", automaticallyRunTests=" + ", language='" + language + '\'' + '}';
    }
}
