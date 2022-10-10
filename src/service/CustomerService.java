package service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.Customer;

public class CustomerService {

  private static CustomerService customerService_instance = null;
  private final Map<String, Customer> customerMap = new HashMap<>();

  public static CustomerService getInstance() {
    if (customerService_instance == null) {
      customerService_instance = new CustomerService();
    }
    return customerService_instance;
  }

  public void addCustomer(final String email, final String firstName, final String lastName) {
    customerMap.put(email, new Customer(firstName, lastName, email));
  }

  public Customer getCustomer(final String customerEmail) {
    return customerMap.get(customerEmail);
  }

  public Collection<Customer> getAllCustomers() {
    return customerMap.values();
  }
}