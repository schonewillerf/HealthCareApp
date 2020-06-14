module adsd.app.zorgapp
{
    requires javafx.controls;
    requires javafx.fxml;
    requires sqlite.jdbc;
    requires java.sql;

    opens adsd.app.zorgapp to javafx.fxml;
    opens adsd.app.zorgapp.controllers to javafx.fxml;
    opens adsd.app.zorgapp.models to javafx.base;
    exports adsd.app.zorgapp;
}