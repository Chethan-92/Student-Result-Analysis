# Student Result Analysis

A modern desktop GUI application designed to manage student records and perform academic performance analysis. Built using Java and JavaFX, it features a premium dark slate dashboard layout with full CRUD operations, live search filtering, and grade color-coding.

*Note: The project also includes a legacy command-line console version (`StudentResultAnalysis.java`) for text-only execution.*

---

## Features

- **Interactive Desktop GUI**: Built using JavaFX controls with a beautiful dark slate theme styling.
- **Unified Sidebar Form**: Seamlessly transition between adding new students and updating existing student details. Selecting a student in the table automatically loads their profile and enables Edit Mode.
- **Dynamic Search & Filter**: Filter student records in real time by typing names or roll numbers into the dashboard search bar.
- **Marks Range Validation**: Restricts input fields to decimals/numbers and enforces academic bounds (0 to 100).
- **Sort by Performance**: Sort the entire dashboard dynamically by percentage. Columns can also be clicked to sort by specific fields.
- **Identify Topper**: Instantly identify and display the top academic performer with a styled information modal.
- **Color-Coded Feedback**: Highlight excellent results (emerald green for Grade A/A+) and failures (rose red) visually in the table.

---

## Technologies Used

- **Programming Language**: Java (JDK 8 or above)
- **GUI Framework**: JavaFX
- **Styling**: Vanilla CSS (`style.css`)
- **Data Structures**: `ArrayList` & `ObservableList`

---

## Project Structure

```
student-result-analysis/
│
├── Student.java                # Represents the Student model class (data & calculations)
├── MainFxApp.java              # The entry point for the modern JavaFX desktop UI
├── style.css                   # Custom styles for the JavaFX UI elements
├── StudentResultAnalysis.java  # Legacy menu-driven console version manager class
├── run_app.bat                 # Automation build and launch script for Windows
└── README.md                   # Documentation file
```

---

## Explanation of Classes

### 1. `Student.java`
This class represents the model for a single student.
- **Private fields**: Stores `name`, `rollNumber`, `english`, `chemistry`, `physics`, `computerScience`, `mathematics`, `total`, `percentage`, and `grade`.
- **Constructor & Getters/Setters**: Setter methods automatically trigger recalculation of grades and averages, ensuring state consistency.
- **`calculateTotal()`**, `calculatePercentage()`, `calculateGrade()`: Handles all calculations internally.

### 2. `MainFxApp.java`
The main desktop interface controller.
- **JavaFX UI Components**: Configures layouts (`HBox`, `VBox`, `GridPane`), inputs (`TextField`), buttons, and data tables (`TableView`).
- **`FilteredList` & `SortedList`**: Binds the list with the search bar and TableView to support real-time filtering and sort-clicking.

---

## Grading System

| Percentage Range | Grade |
|---|---|
| Percentage $\ge$ 90 | **Grade A+** |
| Percentage $\ge$ 80 | **Grade A** |
| Percentage $\ge$ 70 | **Grade B** |
| Percentage $\ge$ 60 | **Grade C** |
| Percentage $\ge$ 50 | **Grade D** |
| Percentage $<$ 50 | **Fail** |

---

## Compilation and Execution

### Running the Desktop GUI (Recommended)
We have provided an automated Windows script **`run_app.bat`** to build and launch the GUI.

1. Open File Explorer and navigate to the project directory:
   `C:\Users\Cheth\.gemini\antigravity\scratch\student-result-analysis`
2. Double-click the **`run_app.bat`** script.
3. The script will automatically compile the source files, load the stylesheets, and launch the JavaFX UI.

Alternatively, compile and launch manually from the command line:
```bash
# Compile
javac -d bin --module-path C:\Users\Cheth\javafx-sdk-21.0.2\lib --add-modules javafx.controls *.java

# Copy Resources
copy /Y style.css bin\style.css

# Run
java -cp bin --module-path C:\Users\Cheth\javafx-sdk-21.0.2\lib --add-modules javafx.controls MainFxApp
```

### Running the Legacy Console CLI version
If you want to run the text-only console application:
```bash
# Compile
javac Student.java StudentResultAnalysis.java

# Run
java StudentResultAnalysis
```
