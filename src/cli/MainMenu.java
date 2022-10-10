package cli;

import api.HotelResource;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import model.IRoom;

public class MainMenu {

  private static final HotelResource hotelResource = HotelResource.getInstance();

  public static void start() {
    boolean flag = true;
    try (Scanner sc = new Scanner(System.in)) {
      while (flag) {
        try {
          displayMainMenu();
          int choice = sc.nextInt();
          switch (choice) {
            case 1 -> {
              findAndReserveARoom();
              flag = false;
            }
            case 2 -> {
              System.out.println("2");
              flag = false;
            }
            case 3 -> {
              registerCustomer();
              flag = false;
            }
            case 4 -> {
              AdminMenu.adminMenu();
              flag = false;
            }
            case 5 -> {
              System.out.println("Thanks for using the Hotel Reservation CLI application");
              System.out.println("Exiting.");
              flag = false;
            }
            default -> {
              System.out.println("Please enter the choice between 1 and 5");
            }
          }
        } catch (Exception e) {
          System.out.println("Error: Invalid input.");
          flag = false;
          start();
        }
      }
    }
  }

  private static void registerCustomer() {
    System.out.println("Registering the customer");
    final Scanner sc = new Scanner(System.in);

    try (sc) {
      System.out.println("Enter first-name");
      final String firstName = sc.nextLine();
      System.out.println("Enter last-name");
      final String lastName = sc.nextLine();
      System.out.println("Enter email-name");
      final String email = sc.nextLine();
      hotelResource.createACustomer(email, firstName, lastName);
      System.out.println("Customer successfully registered.");
      start();
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println("Please try again.");
    }
  }

  private static void findAndReserveARoom() {
    final Scanner sc = new Scanner(System.in);

    System.out.println("Find and Reserve room");
    System.out.println("Enter Check-In Date mm/dd/yyyy example 31/12/2020");

    String[] dateString = sc.next().split("/");
    Date checkInDate = getDate(dateString);

    System.out.println("Enter Check-Out Date mm/dd/yyyy example 31/12/2020");
    dateString = sc.next().split("/");
    Date checkOutDate = getDate(dateString);

    if (checkOutDate.after(checkInDate)) {
      System.out.println("checkInDate: " + checkInDate);
      System.out.println("checkOutDate: " + checkOutDate);

      Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
//      if(availableRooms.isEmpty()) {
//        availableRooms = hotelResource.findAlternateRoom(checkInDate, checkOutDate);
//      }

      // show available rooms
      availableRooms.forEach(System.out::println);

      // proceed towards reservation
      makeReservation(sc, availableRooms, checkInDate, checkOutDate);

    } else {
      System.out.println("Error: checkOutDate cannot be before checkInDate");
      findAndReserveARoom();
    }
  }

  private static void makeReservation(final Scanner sc, final Collection<IRoom> rooms,
      final Date checkInDate, final Date checkOutDate) {
    System.out.println("\nWould you like to book a room? y/n\n");
    final String choice = sc.nextLine();
  }

  private static Date getDate(final String[] dateString) {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]) - 1,
        Integer.parseInt(dateString[0]));
    return calendar.getTime();
  }

  private static void displayMainMenu() {
    System.out.println("\nWelcome to the Hotel Reservation Application\n" +
        "--------------------------------------------\n" +
        "1. Find and reserve a room\n" +
        "2. See my reservations\n" +
        "3. Create an Account\n" +
        "4. Admin\n" +
        "5. Exit\n" +
        "--------------------------------------------\n" +
        "Please select a number for the menu option:\n");
  }
}