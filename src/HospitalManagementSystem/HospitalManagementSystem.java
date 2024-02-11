package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Ashu@123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patients patients = new Patients(connection, scanner);
            Docters docters = new Docters(connection);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM...");
                System.out.println("1 Add patient");
                System.out.println("2 View patients");
                System.out.println("3 View doctors");
                System.out.println("4 Book appointments");
                System.out.println("5 Exit");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> patients.addPatient();
                    case 2 -> patients.viewPatient();
                    case 3 -> docters.viewDoctors();
                    case 4 -> bookAppointment(patients, docters, connection, scanner);
                    case 5 -> {
                        return;
                    }
                    default ->
                        System.out.println("Enter valid choice ");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patients patients, Docters docters, Connection connection, Scanner scanner) {
        System.out.println("Enter patient id...");
        int patientId = scanner.nextInt();
        System.out.println("Enter doctor id...");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date yyyy-mm-dd");
        String appointmentDate = scanner.next();

        if (patients.getPatientbyid(patientId) && docters.getDocterbyid(doctorId)) {
            if (checkDoctorsAvailability(doctorId, appointmentDate, connection)) {
                String appintmentquery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) values(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appintmentquery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rawAffected = preparedStatement.executeUpdate();
                    if (rawAffected > 0) {
                        System.out.println("Appointment booked successfully");
                    } else {
                        System.out.println("failed to book appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkDoctorsAvailability(int doctorId, String appointmentData, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentData);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
