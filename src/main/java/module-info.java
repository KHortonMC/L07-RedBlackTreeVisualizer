module cs113.treeVisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens cs113.treeVisualizer to javafx.fxml;
    exports cs113.treeVisualizer;
}