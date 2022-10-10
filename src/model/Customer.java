package model;

import java.util.regex.Pattern;

public class Customer {

  private final String firstName;
  private final String lastName;
  private final String email;

  private final String emailRegEx = "^(.+)@(.+).com$";
  private final Pattern emailPattern = Pattern.compile(emailRegEx);

  public Customer(final String firstName, final String lastName, final String email) {
    if (!isValidEmail(email)) {
      throw new IllegalArgumentException("Invalid Email Address.");
    } else {
      this.email = email;
    }

    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
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