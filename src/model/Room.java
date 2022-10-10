package model;

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
  public String toString() {
    return "Room {" +
        "roomNumber='" + roomNumber + '\'' +
        ", price=" + price +
        ", roomType=" + roomType +
        '}';
  }
}
