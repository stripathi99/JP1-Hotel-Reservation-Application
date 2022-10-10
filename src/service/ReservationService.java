package service;

import java.util.ArrayList;
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
    return roomMap
        .values()
        .stream()
        .filter(iRoom -> isRoomAvailable(checkInDate, checkOutDate))
        .collect(Collectors.toList());
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

  private boolean isRoomAvailable(final Date checkInDate, final Date checkOutDate) {
    return Stream.ofNullable(getAllReservations())
        .flatMap(Collection::stream)
        .allMatch(
            reservation -> checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(
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