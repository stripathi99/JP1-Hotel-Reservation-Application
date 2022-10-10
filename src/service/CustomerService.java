package service;

import java.util.Collection;
import model.Customer;

public class CustomerService {

  private static CustomerService customerService_instance = null;

  public static CustomerService getInstance() {
    if (customerService_instance == null) {
      customerService_instance = new CustomerService();
    }
    return customerService_instance;
  }

  // TODO: add new customer to customer-list
  public void addCustomer(String email, String firstName, String lastName) {
  }

  // TODO: return customer from hash-map with email as key
  public Customer getCustomer(final String customerEmail) {
    return null;
  }

  // TODO: return customer-list
  public Collection<Customer> getAllCustomers() {
    return null;
  }
}