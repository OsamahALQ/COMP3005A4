package org.example;
import java.sql.*;
import java.sql.Date;
import java.util.Scanner;


public class Main {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/Assignment4" ;
    private static final String USER = "postgres" ;
    private static final String PASSWORD = "Liverpolosama0!" ;

    public static void main(String[] args) {
        try {
            Class.forName( "org.postgresql.Driver" );
            try (Connection conn = connect()) {
                if (conn != null) {
                    System.out.println( "Connected to PostgreSQL successfully!" );
                } else {
                    System.out.println( "Failed to establish connection." );
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("=================================");
            System.out.println("Choose an option:");
            System.out.println("1. Show all students");
            System.out.println("2. Create a student DB");
            System.out.println("3. Update a student's DB");
            System.out.println("4. Delete a Student's DB");
            System.out.println("5. Quit");
            System.out.print("Your option: ");
            String userInput = scanner.nextLine().trim();
            int choice;
            if(userInput.isEmpty()){
                System.out.println("=================================");
                System.out.println("You must pick a number between 1 - 5");
                continue;
            }
            try {
                choice = Integer.parseInt(userInput);
            }catch (NumberFormatException e){
                System.out.println("=================================");
                System.out.println("It must be an integer between 1 - 5");
                continue;
            }
            System.out.println("=================================");
            switch (choice){
                case 1:
                    getAllStudents();
                    break;
                case 2:
                    System.out.println("Enter Student's first name: ");
                    String fName = scanner.nextLine();
                    while(fName.trim().isEmpty() || !(fName.matches("^[a-zA-z]*$"))){
                        if(fName.trim().isEmpty()) {
                            System.out.println("First name should not be empty");
                        } else if (!(fName.matches("^[a-zA-z]*$"))) {
                            System.out.println("First name should be a string");
                        }
                        System.out.println("Enter Student's first name: ");
                        fName = scanner.nextLine();
                    }
                    System.out.println("Enter Student's last name: ");
                    String lName = scanner.nextLine();
                    while(lName.trim().isEmpty() || !(lName.matches("^[a-zA-Z]*$"))){
                        if(lName.trim().isEmpty()) {
                            System.out.println("Last name should not be empty");
                        } else if (!(lName.matches("^[a-zA-Z]*$"))) {
                            System.out.println("Last name should be a string");
                        }
                        System.out.println("Enter Student's Last name: ");
                        lName = scanner.nextLine();
                    }
                    String email;
                    boolean eEmail;
                    do {
                        eEmail = false;
                        System.out.println("Enter Student's email: ");
                        email = scanner.nextLine();
                        while(email.trim().isEmpty()){
                            System.out.println("Email should not be empty");
                            System.out.println("Enter Student's email: ");
                            email = scanner.nextLine();
                        }
                        while (!(email.contains("@")) || !(email.contains("."))){
                            System.out.println("Error it has to be a valid email");
                            System.out.println("Enter Student's email: ");
                            email = scanner.nextLine();
                        }
                        String check = "SELECT COUNT(*) FROM students WHERE email=?";
                        try(
                            Connection connection = connect();
                            PreparedStatement ps = connection.prepareStatement(check);
                        ) {
                            ps.setString(1, email);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next() && rs.getInt(1)>0 ){
                                System.out.println("The email is already registered. Try another email");
                                eEmail = true;
                            }
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                    } while (eEmail);
                    System.out.println("Enter Student's enrollment date (yyyy-mm-dd): ");
                    String date = scanner.nextLine();
                    while(date.trim().isEmpty() || !date.matches("^[1-9]\\d{0,3}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")){
                        if(date.trim().isEmpty()) {
                            System.out.println("Enrollment date should not be empty");
                        } else if (!date.matches("^[1-9]\\d{0,3}-(0[1-9]|1[0-2])-(0[1-9]|[12][40-9]|3[01])$")) {
                            System.out.println("Error, it must be in this format yyyy-mm-dd and a correct date");
                        }
                        System.out.println("Enter Student's enrollment date (yyyy-mm-dd): ");
                        date = scanner.nextLine();
                    }
                    addStudent(fName, lName, email, java.sql.Date.valueOf(date));
                    break;
                case 3:
                    int sID = -1;
                    boolean idN = false;
                    do{
                        System.out.println("Enter student's ID: ");
                        String user = scanner.nextLine();
                        try {
                            sID = Integer.parseInt(user);
                        }catch (NumberFormatException e){
                            System.out.println("The student ID must be integer. ");
                            continue;
                        }
                        String checking = "SELECT COUNT(*) FROM students WHERE student_id=?";
                        try(
                                Connection connection = connect();
                                PreparedStatement ps = connection.prepareStatement(checking);

                                ){
                            ps.setInt(1, sID);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next() && rs.getInt(1) > 0) {
                                idN = false;
                            } else{
                                System.out.println("There's no student with this ID, try again for an existed student ID.");
                                idN = true;
                            }

                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }while (idN);
                    String nEmail;
                    boolean eeemail;
                    do{
                        eeemail = false;
                        System.out.println("Enter Student's email: ");
                        nEmail = scanner.nextLine();
                        while(nEmail.trim().isEmpty()){
                            System.out.println("Email should not be empty");
                            System.out.println("Enter Student's email: ");
                            nEmail = scanner.nextLine();
                        }
                        while (!(nEmail.contains("@")) || !(nEmail.contains("."))){
                            System.out.println("Error it has to be a valid email");
                            System.out.println("Enter Student's email: ");
                            nEmail = scanner.nextLine();
                        }
                        String check = "SELECT COUNT(*) FROM students WHERE email=?";
                        try(
                                Connection connection = connect();
                                PreparedStatement ps = connection.prepareStatement(check);
                        ) {
                            ps.setString(1, nEmail);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next() && rs.getInt(1)>0 ){
                                System.out.println("The email is already registered. Try another email");
                                eeemail = true;
                            }
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                    } while (eeemail);
                    updateStudentEmail(sID, nEmail);
                    break;
                case 4:
                    int deleteID = -1;
                    boolean noID = false;
                    do{
                        System.out.println("Enter student's ID: ");
                        String user = scanner.nextLine();
                        try{
                            deleteID = Integer.parseInt(user);
                        }catch (NumberFormatException e){
                            System.out.println("The student ID must be an integer. ");
                            continue;
                        }
                        String checking = "SELECT COUNT(*) FROM students WHERE student_id=?";
                        try(
                                Connection connection = connect();
                                PreparedStatement ps = connection.prepareStatement(checking);

                        ){
                            ps.setInt(1, deleteID);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next() && rs.getInt(1)>0) {
                                noID = false;
                            }else{
                                System.out.println("There's no student with this ID, try again for an existed student ID.");
                                noID = true;
                            }

                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }while (noID);
                    deleteStudent(deleteID);
                    break;
                case 5:
                    System.out.println("Thanks for your time, goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. choice between 1 - 5");
            }
        }
    }

    private static Connection connect() throws SQLException{
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
    public static void getAllStudents(){
        try (
                Connection connection = connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT  * FROM students")
        ){
            while (resultSet.next()){
                System.out.printf("id: %d, name: %s %s, email: %s, enrollment_date: %s%n",
                        resultSet.getInt("student_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("enrollment_date"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addStudent(String first_name, String last_name, String email, Date enrollment_date){
        String sql = "INSERT INTO students(first_name, last_name, email, enrollment_date) VALUES (?,?,?,?)";
        try (
                Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement(sql);

        ) {
            ps.setString(1, first_name);
            ps.setString(2, last_name);
            ps.setString(3, email);
            ps.setDate(4, enrollment_date);

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void updateStudentEmail(int student_id, String new_email){
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";
        try (
                Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setString(1, new_email);
            ps.setInt(2, student_id);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void deleteStudent(int student_id){
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (
                Connection connection = connect();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, student_id);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}