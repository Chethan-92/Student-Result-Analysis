import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * MainFxApp.java
 * Modern JavaFX Desktop GUI Application for Student Result Analysis.
 * Features:
 * - Unified Form for adding and updating students.
 * - Dynamic selection binding (clicking a table row enables Edit Mode).
 * - Real-time filtering / Search Bar.
 * - Color-coded grades and custom slate theme styling.
 */
public class MainFxApp extends Application {

    // Observable list to automatically bind with TableView
    private final ObservableList<Student> observableStudentList = FXCollections.observableArrayList();
    
    // Core controls
    private TableView<Student> tableView;
    private TextField rollField;
    private TextField nameField;
    private TextField englishField;
    private TextField chemistryField;
    private TextField physicsField;
    private TextField computerScienceField;
    private TextField mathematicsField;
    private TextField searchField;

    private Label formTitleLabel;
    private Label errorLabel;
    private Label countLabel;

    private Button addBtn;
    private Button saveBtn;
    private Button cancelBtn;
    private Button deleteBtn;

    // Tracker for the currently selected student in Edit Mode
    private Student selectedStudent = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Result Analysis - Dashboard");

        // Main layout container (Horizontal splitting)
        HBox mainLayout = new HBox(20);
        mainLayout.getStyleClass().add("main-container");
        mainLayout.setPadding(new Insets(20));

        // Form Section (Left)
        VBox formCard = createFormCard();
        HBox.setHgrow(formCard, Priority.NEVER);

        // List & Actions Section (Right)
        VBox listCard = createListCard();
        HBox.setHgrow(listCard, Priority.ALWAYS);

        mainLayout.getChildren().addAll(formCard, listCard);

        // Set up the scene
        Scene scene = new Scene(mainLayout, 1150, 680);

        // Load custom slate styling
        try {
            String cssPath = getClass().getResource("style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Could not load style.css. Ensure it is placed in the same directory. Details: " + e.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        // Load initial mock records for immediate visualization
        loadSampleData();
        updateStudentCount();
    }

    /**
     * Builds the left-hand form sidebar.
     */
    private VBox createFormCard() {
        VBox form = new VBox(10);
        form.getStyleClass().add("form-card");
        form.setPrefWidth(280);
        form.setPadding(new Insets(20));

        formTitleLabel = new Label("Add Student Record");
        formTitleLabel.getStyleClass().add("form-title");

        // Roll Number Field
        VBox rollBox = new VBox(3);
        Label rollLabel = new Label("Roll Number:");
        rollLabel.getStyleClass().add("input-label");
        rollField = new TextField();
        rollField.setPromptText("Enter Roll Number (e.g. MCA01)");
        rollField.getStyleClass().add("input-field");
        rollBox.getChildren().addAll(rollLabel, rollField);

        // Student Name Field
        VBox nameBox = new VBox(3);
        Label nameLabel = new Label("Student Name:");
        nameLabel.getStyleClass().add("input-label");
        nameField = new TextField();
        nameField.setPromptText("Enter Full Name");
        nameField.getStyleClass().add("input-field");
        nameBox.getChildren().addAll(nameLabel, nameField);

        // Subject Marks Section
        VBox marksBox = new VBox(6);
        Label marksTitle = new Label("Subject Marks (0 - 100):");
        marksTitle.getStyleClass().add("input-group-label");
        marksBox.getChildren().add(marksTitle);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        englishField = new TextField();
        chemistryField = new TextField();
        physicsField = new TextField();
        computerScienceField = new TextField();
        mathematicsField = new TextField();

        // Standardize fields, widths, and numeric restrictions
        TextField[] fields = {englishField, chemistryField, physicsField, computerScienceField, mathematicsField};
        String[] subjects = {"English:", "Chemistry:", "Physics:", "Computer Sci:", "Mathematics:"};

        for (int i = 0; i < fields.length; i++) {
            Label label = new Label(subjects[i]);
            label.getStyleClass().add("sub-marks-label");
            fields[i].setPromptText("0-100");
            fields[i].getStyleClass().add("marks-field");
            fields[i].setPrefWidth(70);
            makeNumericOnly(fields[i]);
            
            grid.add(label, 0, i);
            grid.add(fields[i], 1, i);
        }
        marksBox.getChildren().add(grid);

        // Status / Error message label
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-message");
        errorLabel.setWrapText(true);

        // Buttons Pane
        VBox buttonBox = new VBox(8);
        buttonBox.setAlignment(Pos.CENTER);

        addBtn = new Button("Add Student");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> handleAddStudent());

        saveBtn = new Button("Save Changes");
        saveBtn.getStyleClass().add("btn-warning");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setDisable(true); // Enabled in Edit Mode
        saveBtn.setOnAction(e -> handleUpdateStudent());

        cancelBtn = new Button("Cancel Edit");
        cancelBtn.getStyleClass().add("btn-clear");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setDisable(true);
        cancelBtn.setOnAction(e -> handleCancelEdit());

        buttonBox.getChildren().addAll(addBtn, saveBtn, cancelBtn);

        form.getChildren().addAll(formTitleLabel, rollBox, nameBox, marksBox, errorLabel, buttonBox);
        return form;
    }

    /**
     * Builds the right-hand dashboard with search bar, TableView, and action bar.
     */
    private VBox createListCard() {
        VBox container = new VBox(12);
        container.getStyleClass().add("table-card");
        container.setPadding(new Insets(20));

        // Top bar with search and stats
        HBox topBar = new HBox(12);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Student Database Dashboard");
        title.getStyleClass().add("table-title");

        searchField = new TextField();
        searchField.setPromptText("🔍 Search by Name or Roll No...");
        searchField.getStyleClass().add("input-field");
        searchField.setPrefWidth(220);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        countLabel = new Label("Total: 0 students");
        countLabel.getStyleClass().add("count-label");

        topBar.getChildren().addAll(title, searchField, spacer, countLabel);

        // TableView Configuration
        tableView = new TableView<>();
        tableView.getStyleClass().add("modern-table");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, String> rollCol = new TableColumn<>("Roll No");
        rollCol.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        rollCol.setPrefWidth(85);

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(130);

        TableColumn<Student, Double> engCol = new TableColumn<>("English");
        engCol.setCellValueFactory(new PropertyValueFactory<>("english"));
        engCol.setPrefWidth(70);

        TableColumn<Student, Double> chemCol = new TableColumn<>("Chemistry");
        chemCol.setCellValueFactory(new PropertyValueFactory<>("chemistry"));
        chemCol.setPrefWidth(70);

        TableColumn<Student, Double> phyCol = new TableColumn<>("Physics");
        phyCol.setCellValueFactory(new PropertyValueFactory<>("physics"));
        phyCol.setPrefWidth(70);

        TableColumn<Student, Double> csCol = new TableColumn<>("CompSci");
        csCol.setCellValueFactory(new PropertyValueFactory<>("computerScience"));
        csCol.setPrefWidth(70);

        TableColumn<Student, Double> mathCol = new TableColumn<>("Mathematics");
        mathCol.setCellValueFactory(new PropertyValueFactory<>("mathematics"));
        mathCol.setPrefWidth(70);

        TableColumn<Student, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalCol.setPrefWidth(75);
        totalCol.setCellFactory(col -> new TableCell<Student, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f", item));
                }
            }
        });

        TableColumn<Student, Double> pctCol = new TableColumn<>("Percentage");
        pctCol.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        pctCol.setPrefWidth(90);
        pctCol.setCellFactory(col -> new TableCell<Student, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f%%", item));
                }
            }
        });

        TableColumn<Student, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeCol.setPrefWidth(85);
        gradeCol.setCellFactory(col -> new TableCell<Student, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Color code grades
                    if (item.contains("A+")) {
                        setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;"); // Emerald Green
                    } else if (item.contains("A")) {
                        setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;"); // Mint Green
                    } else if (item.equals("Fail")) {
                        setStyle("-fx-text-fill: #f43f5e; -fx-font-weight: bold;"); // Rose Red
                    } else {
                        setStyle("-fx-text-fill: #60a5fa;"); // Blue
                    }
                }
            }
        });

        tableView.getColumns().addAll(rollCol, nameCol, engCol, chemCol, phyCol, csCol, mathCol, totalCol, pctCol, gradeCol);

        // Bind data with filter support
        FilteredList<Student> filteredData = new FilteredList<>(observableStudentList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerFilter = newValue.toLowerCase().trim();
                if (student.getName().toLowerCase().contains(lowerFilter)) {
                    return true;
                }
                if (student.getRollNumber().toLowerCase().contains(lowerFilter)) {
                    return true;
                }
                return false;
            });
            updateStudentCount();
        });

        SortedList<Student> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Selection Listener (Enters edit mode when row is selected)
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                enterEditMode(newSelection);
            }
        });

        // Bottom Actions Bar
        HBox bottomBar = new HBox(12);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        deleteBtn = new Button("Delete Student");
        deleteBtn.getStyleClass().add("btn-danger");
        deleteBtn.setDisable(true); // Enabled only when row is selected
        deleteBtn.setOnAction(e -> handleDeleteStudent());

        Button sortBtn = new Button("Sort by Percentage");
        sortBtn.getStyleClass().add("btn-clear");
        sortBtn.setOnAction(e -> handleSortByPercentage());

        Button topperBtn = new Button("🏆 Identify Topper");
        topperBtn.getStyleClass().add("btn-secondary");
        topperBtn.setOnAction(e -> handleShowTopper());

        bottomBar.getChildren().addAll(deleteBtn, sortBtn, topperBtn);

        container.getChildren().addAll(topBar, tableView, bottomBar);
        return container;
    }

    // --- Action Handlers ---

    /**
     * Handles adding a new student.
     */
    private void handleAddStudent() {
        errorLabel.setText("");
        errorLabel.setStyle("-fx-text-fill: #f43f5e;");

        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();

        if (roll.isEmpty()) {
            errorLabel.setText("Roll Number cannot be empty!");
            return;
        }
        if (name.isEmpty()) {
            errorLabel.setText("Student Name cannot be empty!");
            return;
        }

        // Duplicate roll check
        if (rollExists(roll)) {
            errorLabel.setText("Roll Number '" + roll + "' already exists!");
            return;
        }

        try {
            double eng = parseMark(englishField, "English");
            double chem = parseMark(chemistryField, "Chemistry");
            double phy = parseMark(physicsField, "Physics");
            double cs = parseMark(computerScienceField, "Computer Science");
            double math = parseMark(mathematicsField, "Mathematics");

            Student student = new Student(name, roll, eng, chem, phy, cs, math);
            observableStudentList.add(student);
            
            clearForm();
            updateStudentCount();
            
            errorLabel.setStyle("-fx-text-fill: #10b981;");
            errorLabel.setText("Student added successfully!");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    /**
     * Handles saving updates for a selected student.
     */
    private void handleUpdateStudent() {
        if (selectedStudent == null) return;
        errorLabel.setText("");
        errorLabel.setStyle("-fx-text-fill: #f43f5e;");

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText("Student Name cannot be empty!");
            return;
        }

        try {
            double eng = parseMark(englishField, "English");
            double chem = parseMark(chemistryField, "Chemistry");
            double phy = parseMark(physicsField, "Physics");
            double cs = parseMark(computerScienceField, "Computer Science");
            double math = parseMark(mathematicsField, "Mathematics");

            // Update parameters
            selectedStudent.setName(name);
            selectedStudent.setEnglish(eng);
            selectedStudent.setChemistry(chem);
            selectedStudent.setPhysics(phy);
            selectedStudent.setComputerScience(cs);
            selectedStudent.setMathematics(math);
            
            // Force TableView to refresh selected item visually
            tableView.refresh();
            
            handleCancelEdit();
            
            errorLabel.setStyle("-fx-text-fill: #10b981;");
            errorLabel.setText("Student updated successfully!");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    /**
     * Handles deleting a selected student with confirmation.
     */
    private void handleDeleteStudent() {
        Student selection = tableView.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Student Profile");
        alert.setContentText("Are you sure you want to delete this student?\n\nName: " 
                + selection.getName() + "\nRoll No: " + selection.getRollNumber());

        // Style the alert
        DialogPane pane = alert.getDialogPane();
        try {
            pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            pane.getStyleClass().add("topper-dialog");
        } catch (Exception e) {
            // fallback
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            observableStudentList.remove(selection);
            handleCancelEdit();
            updateStudentCount();
            errorLabel.setStyle("-fx-text-fill: #10b981;");
            errorLabel.setText("Student record deleted.");
        }
    }

    /**
     * Toggles the UI state into Edit Mode.
     */
    private void enterEditMode(Student student) {
        selectedStudent = student;
        formTitleLabel.setText("Update Student Profile");
        
        rollField.setText(student.getRollNumber());
        rollField.setDisable(true); // unique key cannot change

        nameField.setText(student.getName());
        englishField.setText(String.valueOf(student.getEnglish()));
        chemistryField.setText(String.valueOf(student.getChemistry()));
        physicsField.setText(String.valueOf(student.getPhysics()));
        computerScienceField.setText(String.valueOf(student.getComputerScience()));
        mathematicsField.setText(String.valueOf(student.getMathematics()));

        addBtn.setDisable(true);
        saveBtn.setDisable(false);
        cancelBtn.setDisable(false);
        deleteBtn.setDisable(false);
        errorLabel.setText("");
    }

    /**
     * Resets form inputs and exits Edit Mode.
     */
    private void handleCancelEdit() {
        selectedStudent = null;
        formTitleLabel.setText("Add Student Record");
        
        rollField.setDisable(false);
        clearForm();
        
        addBtn.setDisable(false);
        saveBtn.setDisable(true);
        cancelBtn.setDisable(true);
        deleteBtn.setDisable(true);
        tableView.getSelectionModel().clearSelection();
    }

    /**
     * Sorts the student list descending by percentage score.
     */
    private void handleSortByPercentage() {
        observableStudentList.sort((s1, s2) -> Double.compare(s2.getPercentage(), s1.getPercentage()));
        errorLabel.setStyle("-fx-text-fill: #10b981;");
        errorLabel.setText("Sorted database descending by percentage.");
    }

    /**
     * Finds the topper and displays details in a beautiful alert dialog.
     */
    private void handleShowTopper() {
        if (observableStudentList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Student Records");
            alert.setHeaderText(null);
            alert.setContentText("Please add students to find the topper.");
            
            DialogPane pane = alert.getDialogPane();
            try { pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); pane.getStyleClass().add("topper-dialog"); } catch(Exception e){}
            alert.showAndWait();
            return;
        }

        Student topper = observableStudentList.get(0);
        for (Student s : observableStudentList) {
            if (s.getPercentage() > topper.getPercentage()) {
                topper = s;
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Topper Identification");
        alert.setHeaderText("🏆 TOP PERFORMER IDENTIFIED! 🏆");
        
        String details = String.format(
                "Name             : %s\n" +
                "Roll Number      : %s\n\n" +
                "English          : %.1f / 100\n" +
                "Chemistry        : %.1f / 100\n" +
                "Physics          : %.1f / 100\n" +
                "Computer Science : %.1f / 100\n" +
                "Mathematics      : %.1f / 100\n" +
                "-----------------------------------\n" +
                "Total Marks      : %.1f / 500\n" +
                "Percentage       : %.2f%%\n" +
                "Overall Grade    : %s",
                topper.getName(), topper.getRollNumber(),
                topper.getEnglish(), topper.getChemistry(), topper.getPhysics(), topper.getComputerScience(), topper.getMathematics(),
                topper.getTotal(), topper.getPercentage(), topper.getGrade()
        );
        alert.setContentText(details);

        DialogPane pane = alert.getDialogPane();
        try {
            pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            pane.getStyleClass().add("topper-dialog");
        } catch (Exception e) {
            // fallback
        }
        alert.showAndWait();
    }

    // --- Helper Methods ---

    private void clearForm() {
        rollField.clear();
        nameField.clear();
        englishField.clear();
        chemistryField.clear();
        physicsField.clear();
        computerScienceField.clear();
        mathematicsField.clear();
        errorLabel.setText("");
    }

    private void updateStudentCount() {
        countLabel.setText("Total: " + observableStudentList.size() + " students");
    }

    private boolean rollExists(String roll) {
        for (Student s : observableStudentList) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                return true;
            }
        }
        return false;
    }

    private double parseMark(TextField field, String subjectName) {
        String val = field.getText().trim();
        if (val.isEmpty()) {
            throw new IllegalArgumentException("Enter marks for " + subjectName + "!");
        }
        try {
            double m = Double.parseDouble(val);
            if (m < 0 || m > 100) {
                throw new IllegalArgumentException(subjectName + " marks must be between 0 and 100!");
            }
            return m;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format in " + subjectName + "!");
        }
    }

    private void makeNumericOnly(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldValue);
            }
        });
    }

    private void loadSampleData() {
        observableStudentList.add(new Student("Alice Smith", "MCA01", 90.0, 92.5, 88.0, 95.0, 91.0));
        observableStudentList.add(new Student("Bob Johnson", "MCA02", 72.0, 68.0, 80.0, 75.0, 64.0));
        observableStudentList.add(new Student("Charlie Brown", "MCA03", 94.0, 96.0, 98.0, 95.0, 99.0));
        observableStudentList.add(new Student("David Miller", "MCA04", 45.0, 48.0, 52.0, 42.0, 50.0));
    }
}
