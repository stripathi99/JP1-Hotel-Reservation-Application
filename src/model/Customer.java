package model;

import java.util.regex.Pattern;

public class Customer {

  private String firstName;
  private String lastName;
  private String email;

  private final String emailRegEx = "^(.+)@(.+).com$";
  private final Pattern emailPattern = Pattern.compile(emailRegEx);

  public Customer(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;

    if (!isValidEmail(email)) {
      throw new IllegalArgumentException("Invalid Email Address.");
    } else {
      this.email = email;
    }
  }

  private boolean isValidEmail(final String email) {
    return emailPattern.matcher(email).matches();
  }

  @Override
  public String toString() {
    return "Customer{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}