package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {
    private Connection connection;
    private Scanner scanner;

    public Patients(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.print("Enter Patient Name ");
        String name = scanner.next();
        System.out.print("Enter Patient age ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient gender ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients(name, age, gender) values(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRaw = preparedStatement.executeUpdate();
            if (affectedRaw > 0) {
                System.out.println("data is success added... ");
            } else {
                System.out.println("failed to add data...");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void viewPatient() {
        String query = "select * from patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("+------------+------------------+--------+--------+");
            System.out.println("| patient id |   patient name   |  age   | gender |");
            System.out.println("+------------+------------------+--------+--------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-18s|%-8s|%-8s|\n", id, name, age, gender);
                System.out.println("+------------+------------------+--------+--------+");
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientbyid(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
