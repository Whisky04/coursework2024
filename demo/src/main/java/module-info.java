module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires mysql.connector.j;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires org.kordamp.bootstrapfx.core;


    opens com.example.demo to javafx.fxml, javafx.graphics, org.hibernate.orm.core;
    exports com.example.demo to javafx.fxml, javafx.graphics, org.hibernate.orm.core;
    opens com.example.demo.fxControllers to javafx.fxml;
    exports com.example.demo.fxControllers to javafx.fxml;
    opens com.example.demo.fxControllers.tableParameters to javafx.fxml;
    exports com.example.demo.fxControllers.tableParameters to javafx.fxml;
    opens com.example.demo.model to javafx.fxml, org.hibernate.orm.core, jakarta.persistence, java.base;
    exports com.example.demo.model to javafx.fxml, org.hibernate.orm.core, jakarta.persistence, java.base;}