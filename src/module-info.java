module propositionslive {
    requires javafx.graphics;

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires atlantafx.base;
    exports com.app;

    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;
    requires org.junit.jupiter.engine;
    opens com.app to javafx.fxml;
}