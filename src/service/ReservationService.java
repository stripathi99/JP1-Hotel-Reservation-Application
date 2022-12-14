package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

  public Date suggestAlternateDate(final Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, 7); // default is 7 days
    return calendar.getTime();
  }

  Collection<IRoom> getAvailableRooms(final Date checkInDate, final Date checkOutDate) {
    final Collection<Reservation> reservations = getAllReservations();
    final List<IRoom> notAvaiableRooms = reservations
        .stream()
        .filter(reservation -> isRoomBooked(reservation, checkInDate, checkOutDate))
        .map(Reservation::getRoom)
        .collect(Collectors.toList());

    return roomMap
        .values()
        .stream()
        .filter(iRoom ->
            notAvaiableRooms
                .stream()
                .noneMatch(bookedRoom -> bookedRoom.equals(iRoom)))
        .collect(Collectors.toList());
  }

  private boolean isRoomBooked(final Reservation reservation, final Date checkInDate,
      final Date checkOutDate) {
    return checkInDate.before(reservation.getCheckOutDate())
        && checkOutDate.after(reservation.getCheckInDate());
  }

  private Collection<Reservation> getAllReservations() {
    return reservationMap
        .values()
        .stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}