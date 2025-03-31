package cs113.treeVisualizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TreeVisualizer extends Application {
    private RedBlackTree<Integer> rbTree = new RedBlackTree<>();
    private TextField valueInput;
    private Pane treePane;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // Main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Control section at top
        HBox controlPane = new HBox(10);
        controlPane.setPadding(new Insets(10));

        Label promptLabel = new Label("Enter value:");
        valueInput = new TextField();
        valueInput.setPrefWidth(60);

        Button insertButton = new Button("Insert");
        insertButton.setOnAction(e -> insertValue());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteValue());

        Button clearButton = new Button("Clear Tree");
        clearButton.setOnAction(e -> {
            rbTree = new RedBlackTree<>();
            displayTree();
            statusLabel.setText("Tree cleared");
        });

        Button test0 = new Button("Test 0");
        test0.setOnAction(e -> test0());

        Button test1 = new Button("Test 1");
        test1.setOnAction(e -> test1());

        Button test2 = new Button("Test 2");
        test2.setOnAction(e -> test2());

        Button test3 = new Button("Test 3");
        test3.setOnAction(e -> test3());

        controlPane.getChildren().addAll(promptLabel, valueInput, insertButton, deleteButton, clearButton, test0, test1, test2, test3);
        root.setTop(controlPane);

        // Tree visualization in center
        treePane = new Pane();
        treePane.setPrefSize(800, 500);
        root.setCenter(treePane);

        // Status label at bottom
        statusLabel = new Label("Red-Black Tree Visualizer Ready");
        root.setBottom(statusLabel);

        // Set scene and show
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Red-Black Tree Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void test0() {
        int[] tests = {17, 9, 19, 75};
        for (int test : tests) {
            rbTree.insert(test);
        }
        displayTree();
    }

    private void test1() {
        int[] tests = {17, 9, 19, 75, 81};
        for (int test : tests) {
            rbTree.insert(test);
        }
        displayTree();
    }

    private void test2() {
        int[] tests = {17, 9, 19, 75, 25};
        for (int test : tests) {
            rbTree.insert(test);
        }
        displayTree();
    }

    private void test3() {
        int[] tests = {17, 9, 19, 75, 25, 81, 83, 85};
        for (int test : tests) {
            rbTree.insert(test);
        }
        displayTree();
    }

    private void insertValue() {
        try {
            int value = Integer.parseInt(valueInput.getText());
            rbTree.insert(value);
            displayTree();
            statusLabel.setText("Inserted " + value);
            valueInput.clear();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid integer");
        }
    }

    private void deleteValue() {
        try {
            int value = Integer.parseInt(valueInput.getText());
            boolean success = rbTree.delete(value);
            displayTree();
            statusLabel.setText(success ? "Deleted " + value : "Value " + value + " not found");
            valueInput.clear();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid integer");
        }
    }

    private void displayTree() {
        treePane.getChildren().clear();

        if (rbTree.isEmpty()) {
            return;
        }

        // Calculate the initial horizontal position (center of the pane)
        double treeWidth = treePane.getWidth();

        // Get the height of the tree
        int height = rbTree.getHeight();

        // Minimum vertical distance between levels
        double verticalGap = treePane.getHeight() / (height + 1);

        // Draw the tree starting with the root node
        drawTree(rbTree.getRoot(), treeWidth / 2, 30, treeWidth / 4, verticalGap);
    }

    private void drawTree(RedBlackTree.Node node, double x, double y, double hGap, double vGap) {
        if (node == null) return;

        int nodeRadius = 15;

        // Draw node circle
        Circle circle = new Circle(x, y, nodeRadius);

        // Set color based on node color (red or black)
        circle.setFill(node.color == RedBlackTree.Color.RED ? Color.RED : Color.BLACK);
        circle.setStroke(Color.WHITE);

        // Add node value text
        Text text = new Text(x - 5, y + 5, node.element.toString());
        text.setFill(Color.WHITE);
        text.setFont(Font.font(12));

        treePane.getChildren().addAll(circle, text);

        // Draw left child if it exists
        if (node.left != null && node.left.element != null) {
            double childX = x - hGap;
            double childY = y + vGap;

            // Draw line to left child
            Line line = new Line(x, y + nodeRadius, childX, childY - nodeRadius);
            treePane.getChildren().add(line);

            // Recursively draw left subtree
            drawTree(node.left, childX, childY, hGap / 2, vGap);
        }

        // Draw right child if it exists
        if (node.right != null && node.right.element != null) {
            double childX = x + hGap;
            double childY = y + vGap;

            // Draw line to right child
            Line line = new Line(x, y + nodeRadius, childX, childY - nodeRadius);
            treePane.getChildren().add(line);

            // Recursively draw right subtree
            drawTree(node.right, childX, childY, hGap / 2, vGap);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}