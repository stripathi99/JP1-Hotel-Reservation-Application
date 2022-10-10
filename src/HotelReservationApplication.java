import model.Customer;

public class HotelReservationApplication {

  public static void main(String[] args) {
    Customer customer1 = new Customer("Jeff", "Manny", "jeff@gmail.com");
    System.out.println(customer1);

    Customer customer2 = new Customer("Manny", "Jeff", "@gmail.com");
    System.out.println(customer2);
  }
}