package cli;

import api.HotelResource;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import model.Customer;
import model.IRoom;
import model.Reservation;

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
              getCustomerReservations();
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

  private static void getCustomerReservations() {
    final Scanner sc = new Scanner(System.in);
    System.out.println("\nEnter email address:\n");
    final Customer customer = hotelResource.getCustomer(sc.nextLine());
    if (customer == null) {
      System.out.println("No customer found for this email address.");
      System.out.println("Please register first");
      start();
    } else {
      final Collection<Reservation> customerReservations = hotelResource.getCustomersReservations(
          customer.getEmail());
      if (customerReservations.isEmpty()) {
        System.out.println("No reservations found.");
        start();
      } else {
        System.out.println("\nHere are your reservations\n");
        customerReservations.forEach(System.out::println);
        start();
      }
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
      if (availableRooms.isEmpty()) {
        System.out.println(
            "\nHmm .. looks like no rooms are available for current check in/out dates.\n");
        System.out.println("\nLooking for rooms in alternate dates.\n");
        System.out.println("\nThe current default is +7 days of your check in/out.\n");
        availableRooms = hotelResource.findAlternateRoom(checkInDate, checkOutDate);
        checkInDate = hotelResource.findAlternateDate(checkInDate);
        checkOutDate = hotelResource.findAlternateDate(checkOutDate);
        System.out.println("new checkInDate: " + checkInDate);
        System.out.println("new checkOutDate: " + checkOutDate);
      }

      while (availableRooms.isEmpty()) {
        System.out.println(
            "Hmm .. looks like no rooms are available for current check in/out dates.");
        System.out.println("Looking for rooms in alternate dates.");
        System.out.println("The current default is +7 days of your check in/out.");

        availableRooms = hotelResource.findAlternateRoom(checkInDate, checkOutDate);
        checkInDate = hotelResource.findAlternateDate(checkInDate);
        checkOutDate = hotelResource.findAlternateDate(checkOutDate);

        System.out.println("new checkInDate: " + checkInDate);
        System.out.println("new checkOutDate: " + checkOutDate);
      }

      // show available rooms
      System.out.println("\nThe following rooms are available with us:\n");
      //availableRooms.forEach(System.out::println);
      availableRooms
          .stream()
          .filter(Objects::nonNull)
          .forEach(System.out::println);

      // proceed towards reservation
      makeReservation(availableRooms, checkInDate, checkOutDate);
      sc.close();
    } else {
      System.out.println("Error: checkOutDate cannot be before checkInDate");
      findAndReserveARoom();
    }
  }

  private static void makeReservation(final Collection<IRoom> rooms,
      final Date checkInDate, final Date checkOutDate) {
    final Scanner sc = new Scanner(System.in);
    System.out.println("\nWould you like to book a room? y/n\n");
    final String choice = sc.nextLine();
    if ("y".equals(choice)) {
      System.out.println("\nDo you have an account with us? y/n\n");
      final String choice1 = sc.nextLine();
      if ("y".equals(choice1)) {
        System.out.println("\nEnter email address:\n");
        //final String inputEmail = sc.nextLine();
        Customer customer = hotelResource.getCustomer(sc.nextLine());
        if (customer == null) {
          System.out.println("Customer not found.\nYou may need to create a new account.");
          start();
        } else {
          System.out.println(customer);
          System.out.println("\nEnter room number you like to book:\n");
          final String roomNumber = sc.nextLine();
          if (rooms.stream().anyMatch(iRoom -> iRoom.getRoomNumber().equals(roomNumber))) {
            IRoom room = hotelResource.getARoom(roomNumber);
            System.out.println("\nBooking the following room:\n");
            System.out.println(room);
            final Reservation reservation = hotelResource.bookARoom(customer.getEmail(), room,
                checkInDate, checkOutDate);
            System.out.println(reservation);
            start();
          } else {
            System.out.println("\nError: room number not available.\n");
            start();
          }
        }
      } else if ("n".equals(choice1)) {
        System.out.println("\nPlease, create an account.\n");
        start();
      }
    } else if ("n".equals(choice)) {
      start();
    } else {
      makeReservation(rooms, checkInDate, checkOutDate);
    }
  }

  private static Date getDate(final String[] dateString) {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]) - 1,
        Integer.parseInt(dateString[0]));
    return calendar.getTime();
  }

  private static void displayMainMenu() {
    System.out.println("""
        Welcome to the Hotel Reservation Application
        --------------------------------------------
        1. Find and reserve a room
        2. See my reservations
        3. Create an Account
        4. Admin
        5. Exit
        --------------------------------------------
        Please select a number for the menu option:
        """);
  }
}