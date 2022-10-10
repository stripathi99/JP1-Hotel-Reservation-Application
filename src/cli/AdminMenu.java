package cli;

import api.AdminResource;
import java.util.Collections;
import java.util.Scanner;
import model.Room;
import model.RoomType;

public class AdminMenu {

  private static final AdminResource adminResource = AdminResource.getInstance();

  public static void adminMenu() {
    boolean flag = true;
    try (Scanner sc = new Scanner(System.in)) {
      while (flag) {
        try {
          displayAdminMenu();
          int choice = sc.nextInt();
          switch (choice) {
            case 1 -> {
              System.out.println("1. See all Customers");
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

  private static void addRoom() {
    System.out.println("Adding a room.");
    final Scanner sc = new Scanner(System.in);

    System.out.println("Enter room number:");
    final String roomNumber = sc.nextLine();

    System.out.println("Enter room price (per night) in $:");
    final Double roomPrice = Double.parseDouble(sc.nextLine());

    System.out.println("Enter room type: 1 for single bed, 2 for double bed:");
    final RoomType roomType = RoomType.valueOfLabel(sc.nextLine());

    final Room room = new Room(roomNumber, roomPrice, roomType);
    adminResource.addRoom(Collections.singletonList(room));

    System.out.println("Room added successfully.");

    System.out.println("Would you like to add another room? y/n\n");
    String choice = sc.nextLine();

    if ("y".equals(choice)) {
      addRoom();
    } else if ("n".equals(choice)) {
      adminMenu();
    } else {
      System.out.println("Thanks for using the Hotel Reservation CLI application");
      System.out.println("Exiting.");
      sc.close();
    }
  }

  private static void displayAdminMenu() {
    System.out.print("\nAdmin Menu\n" +
        "--------------------------------------------\n" +
        "1. See all Customers\n" +
        "2. See all Rooms\n" +
        "3. See all Reservations\n" +
        "4. Add a Room\n" +
        "5. Back to Main Menu\n" +
        "--------------------------------------------\n" +
        "Please select a number for the menu option:\n");
  }
}
