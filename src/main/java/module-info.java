module com.sev4ikwasd.tablewithaccept {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.saxsys.mvvmfx;

    opens com.sev4ikwasd.tablewithaccept.ui to javafx.fxml, de.saxsys.mvvmfx;

    exports com.sev4ikwasd.tablewithaccept.ui;
    exports com.sev4ikwasd.tablewithaccept.model;
}