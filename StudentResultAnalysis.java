import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * StudentResultAnalysis.java
 * Manager class that implements the menu-driven application for student result analysis.
 */
public class StudentResultAnalysis {
    // List to store Student objects
    private static final ArrayList<Student> students = new ArrayList<>();
    // Scanner object for console input
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = readMenuChoice();
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    updateStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    displayStudentResult();
                    break;
                case 5:
                    displayAllStudents();
                    break;
                case 6:
                    sortStudentsByPercentage();
                    break;
                case 7:
                    findTopper();
                    break;
                case 8:
                    System.out.println("\nExiting Student Result Analysis. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Error: Invalid choice. Please select an option from 1 to 8.");
            }
        }
    }

    /**
     * Prints the interactive user menu options.
     */
    private static void displayMenu() {
        System.out.println("\n==================================================");
        System.out.println("            STUDENT RESULT ANALYSIS SYSTEM        ");
        System.out.println("==================================================");
        System.out.println("1. Add Student");
        System.out.println("2. Update Student");
        System.out.println("3. Delete Student");
        System.out.println("4. Display Student Result");
        System.out.println("5. Display All Students");
        System.out.println("6. Display Students Sorted by Percentage");
        System.out.println("7. Find Topper");
        System.out.println("8. Exit");
        System.out.println("==================================================");
    }

    // --- Core Operations ---

    /**
     * Functional Requirement 1: Add Student.
     * Collects and validates student details and adds them to the list.
     */
    public static void addStudent() {
        System.out.println("\n--- Add New Student ---");
        
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Enter Student Name: ");
            name = scanner.nextLine().trim();
        }

        System.out.print("Enter Roll Number: ");
        String rollNumber = scanner.nextLine().trim();
        while (rollNumber.isEmpty()) {
            System.out.print("Roll Number cannot be empty. Enter Roll Number: ");
            rollNumber = scanner.nextLine().trim();
        }

        // Check for duplicate roll numbers
        if (findStudentByRollNumber(rollNumber) != null) {
            System.out.println("Error: A student with Roll Number '" + rollNumber + "' already exists.");
            return;
        }

        // Prompt and validate marks for all 5 subjects
        double english = readValidDouble("Enter English Marks (0-100): ", 0, 100);
        double chemistry = readValidDouble("Enter Chemistry Marks (0-100): ", 0, 100);
        double physics = readValidDouble("Enter Physics Marks (0-100): ", 0, 100);
        double computerScience = readValidDouble("Enter Computer Science Marks (0-100): ", 0, 100);
        double mathematics = readValidDouble("Enter Mathematics Marks (0-100): ", 0, 100);

        // Create student and add to ArrayList
        Student student = new Student(name, rollNumber, english, chemistry, physics, computerScience, mathematics);
        students.add(student);
        System.out.println("Success: Student added successfully!");
    }

    /**
     * Functional Requirement 2: Update Student.
     * Finds student by roll number and allows editing their properties.
     */
    public static void updateStudent() {
        System.out.println("\n--- Update Student Details ---");
        System.out.print("Enter Student Roll Number to Update: ");
        String rollNumber = scanner.nextLine().trim();

        Student student = findStudentByRollNumber(rollNumber);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Display current details before updating
        System.out.println("\nCurrent details:");
        System.out.println("Name: " + student.getName() + " | Roll Number: " + student.getRollNumber());
        System.out.printf("Marks -> English: %.2f, Chemistry: %.2f, Physics: %.2f, Computer Science: %.2f, Mathematics: %.2f\n\n",
                student.getEnglish(), student.getChemistry(), student.getPhysics(), 
                student.getComputerScience(), student.getMathematics());
        System.out.println("(Press Enter to keep current values)");

        // Read updated values
        String newName = readUpdatedString("Enter new Name (" + student.getName() + "): ", student.getName());
        double newEnglish = readUpdatedDouble("Enter new English Marks (" + student.getEnglish() + "): ", student.getEnglish(), 0, 100);
        double newChemistry = readUpdatedDouble("Enter new Chemistry Marks (" + student.getChemistry() + "): ", student.getChemistry(), 0, 100);
        double newPhysics = readUpdatedDouble("Enter new Physics Marks (" + student.getPhysics() + "): ", student.getPhysics(), 0, 100);
        double newCS = readUpdatedDouble("Enter new Computer Science Marks (" + student.getComputerScience() + "): ", student.getComputerScience(), 0, 100);
        double newMath = readUpdatedDouble("Enter new Mathematics Marks (" + student.getMathematics() + "): ", student.getMathematics(), 0, 100);

        // Save modifications (setting marks triggers auto-recalculations)
        student.setName(newName);
        student.setEnglish(newEnglish);
        student.setChemistry(newChemistry);
        student.setPhysics(newPhysics);
        student.setComputerScience(newCS);
        student.setMathematics(newMath);

        System.out.println("Success: Student details updated successfully!");
    }

    /**
     * Functional Requirement 3: Delete Student.
     * Finds student by roll number and deletes them after confirmation.
     */
    public static void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        System.out.print("Enter Student Roll Number to Delete: ");
        String rollNumber = scanner.nextLine().trim();

        Student student = findStudentByRollNumber(rollNumber);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Display current details for identification
        System.out.println("\nFound Student:");
        System.out.println("Name: " + student.getName() + " | Roll Number: " + student.getRollNumber());
        System.out.printf("Total Marks: %.2f / 500 | Percentage: %.2f%% | Grade: %s\n\n", 
                student.getTotal(), student.getPercentage(), student.getGrade());

        System.out.print("Are you sure you want to delete this student? (Y/N): ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("Y")) {
            students.remove(student);
            System.out.println("Success: Student deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Functional Requirement 4: Display Student Result.
     * Finds student by roll number and prints their complete result.
     */
    public static void displayStudentResult() {
        System.out.println("\n--- Search Student Result ---");
        System.out.print("Enter Roll Number: ");
        String rollNumber = scanner.nextLine().trim();

        Student student = findStudentByRollNumber(rollNumber);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        student.displayResult();
    }

    /**
     * Functional Requirement 5: Display All Students.
     * Displays results of all students currently stored in the system.
     */
    public static void displayAllStudents() {
        System.out.println("\n--- Display All Students ---");
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.println("==========================================================================================================");
        System.out.printf("%-12s | %-20s | %-7s | %-7s | %-7s | %-7s | %-7s | %-8s | %-10s | %-8s\n", 
                "Roll No", "Name", "English", "Chem", "Physics", "CompSci", "Maths", "Total", "Percentage", "Grade");
        System.out.println("==========================================================================================================");
        for (Student s : students) {
            System.out.printf("%-12s | %-20s | %-7.1f | %-7.1f | %-7.1f | %-7.1f | %-7.1f | %-8.1f | %-9.2f%% | %-8s\n",
                    s.getRollNumber(), s.getName(), s.getEnglish(), s.getChemistry(), s.getPhysics(), 
                    s.getComputerScience(), s.getMathematics(), s.getTotal(), s.getPercentage(), s.getGrade());
        }
        System.out.println("==========================================================================================================");
    }

    /**
     * Functional Requirement 6: Display Students Sorted by Percentage.
     * Sorts student records from highest to lowest percentage and displays ranking.
     */
    public static void sortStudentsByPercentage() {
        System.out.println("\n--- Sort Students by Percentage ---");
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        // Create a copy of the student list to keep the original insert order intact
        ArrayList<Student> sortedList = new ArrayList<>(students);
        
        // Sort descending by percentage
        sortedList.sort((s1, s2) -> Double.compare(s2.getPercentage(), s1.getPercentage()));

        System.out.println("===============================================================================");
        System.out.printf("%-6s | %-12s | %-25s | %-8s | %-10s | %-8s\n", 
                "Rank", "Roll No", "Name", "Total", "Percentage", "Grade");
        System.out.println("===============================================================================");
        int rank = 1;
        for (Student s : sortedList) {
            System.out.printf("%-6d | %-12s | %-25s | %-8.1f | %-9.2f%% | %-8s\n",
                    rank++, s.getRollNumber(), s.getName(), s.getTotal(), s.getPercentage(), s.getGrade());
        }
        System.out.println("===============================================================================");
    }

    /**
     * Functional Requirement 7: Find Topper.
     * Finds and displays details of the student with the highest percentage.
     */
    public static void findTopper() {
        System.out.println("\n--- Find Topper ---");
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        Student topper = students.get(0);
        for (Student s : students) {
            if (s.getPercentage() > topper.getPercentage()) {
                topper = s;
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.println("                 TOPPER DETAILS                   ");
        System.out.println("--------------------------------------------------");
        System.out.println("Name        : " + topper.getName());
        System.out.println("Roll Number : " + topper.getRollNumber());
        System.out.printf("Total Marks : %.2f / 500\n", topper.getTotal());
        System.out.printf("Percentage  : %.2f%%\n", topper.getPercentage());
        System.out.println("Grade       : " + topper.getGrade());
        System.out.println("--------------------------------------------------");
    }

    /**
     * Helper method to find a student by roll number.
     * Returns the Student object if found, otherwise null.
     */
    public static Student findStudentByRollNumber(String rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(rollNumber)) {
                return s;
            }
        }
        return null;
    }

    // --- Helper Validation Functions ---

    /**
     * Safely reads an integer option from the menu list.
     */
    private static int readMenuChoice() {
        while (true) {
            System.out.print("Enter your choice (1-8): ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid choice. Please enter a valid menu number (1-8).");
            }
        }
    }

    /**
     * Safely reads a double value within a specified range.
     */
    private static double readValidDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Error: Marks must be between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    /**
     * Safely reads a string value during update.
     * Returns the default value if input is empty.
     */
    private static String readUpdatedString(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    /**
     * Safely reads a double value during update.
     * Returns the default value if input is empty, otherwise validates range.
     */
    private static double readUpdatedDouble(String prompt, double defaultValue, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Error: Marks must be between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number or press Enter.");
            }
        }
    }
}
