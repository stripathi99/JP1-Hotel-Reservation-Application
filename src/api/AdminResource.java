package api;

import java.util.Collection;
import java.util.List;
import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

public class AdminResource {

  private static AdminResource adminResource_instance = null;
  private final CustomerService customerService = CustomerService.getInstance();
  private final ReservationService reservationService = ReservationService.getInstance();

  public static AdminResource getInstance() {
    if (adminResource_instance == null) {
      adminResource_instance = new AdminResource();
    }
    return adminResource_instance;
  }

  public Customer getCustomer(final String email) {
    return customerService.getCustomer(email);
  }

  public void addRoom(final List<IRoom> rooms) {
    rooms.forEach(reservationService::addRoom);
  }

  public Collection<IRoom> getAllRooms() {
    return reservationService.getAllRooms();
  }

  public Collection<Customer> getAllCustomers() {
    return customerService.getAllCustomers();
  }

  public void displayAllReservations() {
    reservationService.printAllReservation();
  }
}