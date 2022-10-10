package api;

import java.util.Collection;
import java.util.Date;
import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

public class HotelResource {

  private static HotelResource hotelResource_instance = null;
  private static final CustomerService customerService = CustomerService.getInstance();
  private static final ReservationService reservationService = ReservationService.getInstance();

  public static HotelResource getInstance() {
    if (hotelResource_instance == null) {
      hotelResource_instance = new HotelResource();
    }
    return hotelResource_instance;
  }

  public Customer getCustomer(final String email) {
    return customerService.getCustomer(email);
  }

  public void createACustomer(final String email, final String firstName, final String lastName) {
    customerService.addCustomer(email, firstName, lastName);
  }

  public IRoom getARoom(final String roomNumber) {
    return reservationService.getARoom(roomNumber);
  }

  public Reservation bookARoom(final String customerEmail, final IRoom room,
      final Date checkIn, final Date checkOut) {
    return reservationService.reserveARoom(getCustomer(customerEmail), room, checkIn, checkOut);
  }

  public Collection<Reservation> getCustomersReservations(final String customerEmail) {
    return reservationService.getCustomersReservation(getCustomer(customerEmail));
  }

  public Collection<IRoom> findARoom(final Date checkIn, final Date checkOut) {
    return reservationService.findRooms(checkIn, checkOut);
  }

  public Collection<IRoom> findAlternateRoom(final Date checkIn, final Date checkOut) {
    return reservationService.findRoomsForAlternateDate(checkIn, checkOut);
  }
}