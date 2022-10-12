package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.Customer;
import model.IRoom;
import model.Reservation;

public class ReservationService {

  private static ReservationService reservationService_instance = null;
  private final Map<String, IRoom> roomMap = new HashMap<>();
  private final Map<String, Collection<Reservation>> reservationMap = new HashMap<>();

  public static ReservationService getInstance() {
    if (reservationService_instance == null) {
      reservationService_instance = new ReservationService();
    }
    return reservationService_instance;
  }

  public void addRoom(IRoom room) {
    roomMap.put(room.getRoomNumber(), room);
  }

  public IRoom getARoom(final String roomId) {
    return roomMap.get(roomId);
  }

  public Collection<IRoom> getAllRooms() {
    return roomMap.values();
  }

  public Reservation reserveARoom(final Customer customer, final IRoom room,
      final Date checkInDate, final Date checkOutDate) {

    final Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
    Collection<Reservation> customersReservation = getCustomersReservation(customer);
    if (customersReservation == null) {
      customersReservation = new ArrayList<>();
    }

    customersReservation.add(reservation);
    reservationMap.put(customer.getEmail(), customersReservation);
    return reservation;
  }

  public Collection<IRoom> findRooms(final Date checkInDate, final Date checkOutDate) {
    return getAvailableRooms(checkInDate, checkOutDate);
  }

  // add 7 days to check in & check-out days and then look for the available rooms
  public Collection<IRoom> findRoomsForAlternateDate(final Date checkInDate,
      final Date checkOutDate) {
    return getAvailableRooms(suggestAlternateDate(checkInDate), suggestAlternateDate(checkOutDate));
  }

  public Collection<Reservation> getCustomersReservation(final Customer customer) {
    return reservationMap.get(customer.getEmail());
  }

  public void printAllReservation() {
    Collection<Reservation> reservations = getAllReservations();
    if (reservations.isEmpty()) {
      System.out.println("Currently, there are no reservations available.");
      System.out.println("Make some reservations first!");
    } else {
      reservations.forEach(System.out::println);
    }
  }

  private Date suggestAlternateDate(final Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, 7); // default is 7 days
    return calendar.getTime();
  }

  private Collection<IRoom> getAvailableRooms(final Date checkInDate, final Date checkOutDate) {
    return roomMap
        .values()
        .stream()
        .filter(iRoom -> isRoomAvailable(checkInDate, checkOutDate))
        .collect(Collectors.toList());
  }

  private boolean isRoomAvailable(final Date checkInDate, final Date checkOutDate) {
    return Stream.ofNullable(getAllReservations())
        .flatMap(Collection::stream)
        .allMatch(
            reservation -> checkInDate.after(reservation.getCheckOutDate()) && checkOutDate.before(
                reservation.getCheckInDate()));
  }

  private Collection<Reservation> getAllReservations() {
    return reservationMap
        .values()
        .stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}