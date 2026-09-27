/**
 * Student.java
 * Model class representing a Student's academic details and results.
 */
public class Student {
    // Private data members
    private String name;
    private String rollNumber;
    private double english;
    private double chemistry;
    private double physics;
    private double computerScience;
    private double mathematics;
    
    // Automatically calculated attributes
    private double total;
    private double percentage;
    private String grade;

    /**
     * Constructor to initialize Student details.
     */
    public Student(String name, String rollNumber, double english, double chemistry, 
                   double physics, double computerScience, double mathematics) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.english = english;
        this.chemistry = chemistry;
        this.physics = physics;
        this.computerScience = computerScience;
        this.mathematics = mathematics;
        
        // Calculate the results automatically on initialization
        calculateResult();
    }

    /**
     * Helper method to calculate total, percentage, and grade.
     */
    public void calculateResult() {
        this.total = calculateTotal();
        this.percentage = calculatePercentage();
        this.grade = calculateGrade();
    }

    /**
     * Calculates the sum of marks obtained in the five subjects.
     * Total is out of 500.
     */
    public double calculateTotal() {
        return english + chemistry + physics + computerScience + mathematics;
    }

    /**
     * Calculates the percentage based on the total marks.
     */
    public double calculatePercentage() {
        return (calculateTotal() / 500.0) * 100.0;
    }

    /**
     * Calculates the grade based on the percentage score.
     */
    public String calculateGrade() {
        double pct = calculatePercentage();
        if (pct >= 90) {
            return "Grade A+";
        } else if (pct >= 80) {
            return "Grade A";
        } else if (pct >= 70) {
            return "Grade B";
        } else if (pct >= 60) {
            return "Grade C";
        } else if (pct >= 50) {
            return "Grade D";
        } else {
            return "Fail";
        }
    }

    /**
     * Displays the student's result in a neatly formatted block.
     */
    public void displayResult() {
        System.out.println("==================================================");
        System.out.println("                 STUDENT RESULT                   ");
        System.out.println("==================================================");
        System.out.printf("Name             : %s\n", name);
        System.out.printf("Roll Number      : %s\n", rollNumber);
        System.out.println("--------------------------------------------------");
        System.out.printf("English          : %.2f / 100\n", english);
        System.out.printf("Chemistry        : %.2f / 100\n", chemistry);
        System.out.printf("Physics          : %.2f / 100\n", physics);
        System.out.printf("Computer Science : %.2f / 100\n", computerScience);
        System.out.printf("Mathematics      : %.2f / 100\n", mathematics);
        System.out.println("--------------------------------------------------");
        System.out.printf("Total Marks      : %.2f / 500\n", total);
        System.out.printf("Percentage       : %.2f%%\n", percentage);
        System.out.printf("Grade            : %s\n", grade);
        System.out.println("==================================================");
    }

    // --- Getter and Setter Methods ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double getEnglish() {
        return english;
    }

    public void setEnglish(double english) {
        this.english = english;
        calculateResult(); // Recalculate results after changes
    }

    public double getChemistry() {
        return chemistry;
    }

    public void setChemistry(double chemistry) {
        this.chemistry = chemistry;
        calculateResult(); // Recalculate results after changes
    }

    public double getPhysics() {
        return physics;
    }

    public void setPhysics(double physics) {
        this.physics = physics;
        calculateResult(); // Recalculate results after changes
    }

    public double getComputerScience() {
        return computerScience;
    }

    public void setComputerScience(double computerScience) {
        this.computerScience = computerScience;
        calculateResult(); // Recalculate results after changes
    }

    public double getMathematics() {
        return mathematics;
    }

    public void setMathematics(double mathematics) {
        this.mathematics = mathematics;
        calculateResult(); // Recalculate results after changes
    }

    public double getTotal() {
        return total;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getGrade() {
        return grade;
    }
}
