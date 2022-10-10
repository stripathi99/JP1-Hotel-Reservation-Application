package model;

import java.util.Objects;

public class Room implements IRoom {

  private final String roomNumber;
  private final Double price;
  private final RoomType roomType;

  public Room(String roomNumber, Double price, RoomType roomType) {
    this.roomNumber = roomNumber;
    this.price = price;
    this.roomType = roomType;
  }

  @Override
  public String getRoomNumber() {
    return this.roomNumber;
  }

  @Override
  public String getRoomPrice() {
    return this.price.toString();
  }

  @Override
  public String getRoomType() {
    return this.roomType.name();
  }

  @Override
  public boolean isFree() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Room room = (Room) o;

    if (!getRoomNumber().equals(room.getRoomNumber())) {
      return false;
    }
    if (!getRoomPrice().equals(room.getRoomPrice())) {
      return false;
    }
    return Objects.equals(getRoomType(), room.getRoomType());
  }

  @Override
  public int hashCode() {
    int result = getRoomNumber().hashCode();
    result = 31 * result + getRoomPrice().hashCode();
    result = 31 * result + getRoomType().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Room{" +
        "roomNumber='" + getRoomNumber() + '\'' +
        ", price=" + getRoomPrice() +
        ", roomType=" + getRoomType() +
        '}';
  }
}
