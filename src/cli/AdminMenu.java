package cli;

import api.AdminResource;
import api.HotelResource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import model.FreeRoom;
import model.IRoom;
import model.Room;
import model.RoomType;

public class AdminMenu {

  private static final AdminResource adminResource = AdminResource.getInstance();
  private static final HotelResource hotelResource = HotelResource.getInstance();

  public static void adminMenu() {
    boolean flag = true;
    try (Scanner sc = new Scanner(System.in)) {
      while (flag) {
        try {
          displayAdminMenu();
          int choice = sc.nextInt();
          switch (choice) {
            case 1 -> {
              displayAllCustomers();
              flag = false;
            }
            case 2 -> {
              displayAllRooms();
              flag = false;
            }
            case 3 -> {
              displayAllReservations();
              flag = false;
            }
            case 4 -> {
              addRoom();
              flag = false;
            }
            case 5 -> {
              MainMenu.start();
              flag = false;
            }
            case 6 -> {
              populateWithTestData();
              flag = false;
            }
            default -> {
              System.out.println("Please enter the choice between 1 and 5");
            }
          }
        } catch (Exception e) {
          System.out.println("Error: Invalid input.");
          flag = false;
          adminMenu();
        }
      }
    }
  }

  private static void populateWithTestData() {
    addTestCustomers();
    addTestRooms();
    adminMenu();
  }

  private static void addTestRooms() {
    List<IRoom> rooms = new ArrayList<>();
    rooms.add(new Room("101", 12.21, RoomType.SINGLE));
    rooms.add(new FreeRoom("102", RoomType.SINGLE));
    rooms.add(new Room("103", 21.12, RoomType.DOUBLE));
    rooms.add(new Room("104", 25.99, RoomType.DOUBLE));
    adminResource.addRoom(rooms);
  }

  private static void addTestCustomers() {
    hotelResource.createACustomer("bob@gmail.com", "bob", "well");
    hotelResource.createACustomer("john@gmail.com", "john", "tate");
    hotelResource.createACustomer("manny@gmail.com", "manny", "jane");
    hotelResource.createACustomer("sam@gmail.com", "sam", "rockwell");
  }

  private static void displayAllCustomers() {
    adminResource.getAllCustomers().forEach(System.out::println);
    adminMenu();
  }

  private static void displayAllReservations() {
    adminResource.displayAllReservations();
    adminMenu();
  }

  private static void displayAllRooms() {
    Stream.ofNullable(adminResource.getAllRooms())
        .flatMap(Collection::stream)
        .forEach(System.out::println);
    adminMenu();
  }

  private static void addRoom() {
    System.out.println("Adding a room.");
    final Scanner sc = new Scanner(System.in);

    System.out.println("Enter room number:");
    final String roomNumber = sc.nextLine();

    System.out.println("Enter room price (per night) in $:");
    final double roomPrice = Double.parseDouble(sc.nextLine());

    System.out.println("Enter room type: 1 for single bed, 2 for double bed:");
    final RoomType roomType = RoomType.valueOfLabel(sc.nextLine());

    final Room room;

    if (roomPrice == 0.0) {
      room = new FreeRoom(roomNumber, roomType);
    } else {
      room = new Room(roomNumber, roomPrice, roomType);
    }

    adminResource.addRoom(Collections.singletonList(room));

    System.out.println("Room added successfully.");

    System.out.println("Would you like to add another room? y/n\n");
    String choice = sc.nextLine();

    if ("y".equals(choice)) {
      addRoom();
    } else if ("n".equals(choice)) {
      adminMenu();
    } else {
      System.out.println("Error: Invalid input.");
      System.out.println("Exiting.");
      System.out.println("Thanks for using the Hotel Reservation CLI application");
      sc.close();
    }
  }

  private static void displayAdminMenu() {
    System.out.println("\nAdmin Menu\n" +
        "--------------------------------------------\n" +
        "1. See all Customers\n" +
        "2. See all Rooms\n" +
        "3. See all Reservations\n" +
        "4. Add a Room\n" +
        "5. Back to Main Menu\n" +
        "6. Add test data\n" +
        "--------------------------------------------\n" +
        "Please select a number for the menu option:\n");
  }
}
