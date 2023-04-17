module propositionslive {
    requires javafx.graphics;
    requires org.junit.jupiter.api;
    requires javafx.controls;
    requires javafx.fxml;
    exports com.propositions;
    opens com.propositions to javafx.fxml;
}