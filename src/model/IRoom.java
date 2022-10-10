package model;

public interface IRoom {

  public String getRoomNumber();

  public String getRoomPrice();

  public RoomType getRoomType();

  public boolean isFree();
}