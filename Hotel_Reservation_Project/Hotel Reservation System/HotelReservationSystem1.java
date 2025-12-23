import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem1 {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Scanner s1 = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n===== HOTEL MANAGEMENT SYSTEM =====");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                int choice = s1.nextInt();

                switch (choice) {
                    case 1 -> reserveRoom(connection, s1);
                    case 2 -> viewReservation(connection);
                    case 3 -> getRoomNumber(connection, s1);
                    case 4 -> updateReservation(connection, s1);
                    case 5 -> deleteReservation(connection, s1);
                    case 0 -> {
                        exit();
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }

        } catch (SQLException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    // ================= METHODS =================

    private static void reserveRoom(Connection connection, Scanner s1) {
        String query = "INSERT INTO reservation (guest_name, room_number, contact_number) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            System.out.print("Enter guest name: ");
            String guestName = s1.next();

            System.out.print("Enter room number: ");
            int roomNumber = s1.nextInt();

            System.out.print("Enter contact number: ");
            String contactNumber = s1.next();

            ps.setString(1, guestName);
            ps.setInt(2, roomNumber);
            ps.setString(3, contactNumber);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0 ? "Reservation Successful!" : "Reservation Failed!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection connection) {
        String query = "SELECT * FROM reservation";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== CURRENT RESERVATIONS =====");

            while (rs.next()) {
                System.out.printf(
                        "| %-3d | %-15s | %-5d | %-12s | %-20s |\n",
                        rs.getInt("reservation_id"),
                        rs.getString("guest_name"),
                        rs.getInt("room_number"),
                        rs.getString("contact_number"),
                        rs.getTimestamp("reservation_date")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getRoomNumber(Connection connection, Scanner s1) {
        String query = "SELECT room_number FROM reservation WHERE reservation_id = ? AND guest_name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            System.out.print("Enter Reservation ID: ");
            int id = s1.nextInt();

            System.out.print("Enter Guest Name: ");
            String guestName = s1.next();

            ps.setInt(1, id);
            ps.setString(2, guestName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Room Number: " + rs.getInt("room_number"));
            } else {
                System.out.println("Reservation not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner s1) {
        String query = "UPDATE reservation SET guest_name=?, room_number=?, contact_number=? WHERE reservation_id=?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            System.out.print("Enter Reservation ID: ");
            int id = s1.nextInt();

            System.out.print("Enter new guest name: ");
            String guestName = s1.next();

            System.out.print("Enter new room number: ");
            int roomNumber = s1.nextInt();

            System.out.print("Enter new contact number: ");
            String contactNumber = s1.next();

            ps.setString(1, guestName);
            ps.setInt(2, roomNumber);
            ps.setString(3, contactNumber);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Reservation Updated Successfully!" : "Update Failed!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection connection, Scanner s1) {
        String query = "DELETE FROM reservation WHERE reservation_id=?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            System.out.print("Enter Reservation ID: ");
            int id = s1.nextInt();

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Reservation Deleted Successfully!" : "Delete Failed!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting");
        for (int i = 0; i < 5; i++) {
            System.out.print(".");
            Thread.sleep(400);
        }
        System.out.println("\nThank you for using Hotel Reservation System!");
    }
}
