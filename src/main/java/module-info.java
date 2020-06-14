module adsd.app.zorgapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens adsd.app.zorgapp to javafx.fxml;
    exports adsd.app.zorgapp;
}