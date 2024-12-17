package com.pedrooliveira.rangolist.exception;

import com.pedrooliveira.rangolist.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidationsTest {
  @InjectMocks
  private Validations validations;

  private Address address;

  @BeforeEach
  void config(){
    address = new Address();
    address.setStreet("Stree Test");
    address.setCity("City Test");
    address.setState("State Test");
    address.setZipcode("00000-00");
  }

  @DisplayName("Should validate if name is valid")
  @Test
  public void testIsNameValid() {
    assertTrue(validations.isNameValid("Name"));
  }

  @DisplayName("Should validate if name is invalid")
  @Test
  public void testIsNameInvalid() {
    assertThrows(HandleValidationField.class, () -> validations.isNameValid(null));
  }

  @DisplayName("Should validate that name contains least 2 characters")
  @Test
  public void testNameCountValid() {
    assertTrue(validations.isNameCount("name"));
  }

  @DisplayName("Should validate that name contains less than 2 characters")
  @Test
  public void testNameCountInvalid() {
    assertThrows(HandleValidationField.class, () -> validations.isNameCount("n"));
  }

  @DisplayName("Should validat open hours is valid")
  @Test
  public void testValidateOpenHours() {
    assertTrue(validations.isOpeningHoursValid("22:00 - 21:00"));
  }

  @DisplayName("Should validate that open hours is not null")
  @Test
  public void testOpenHoursIsNotNull() {
    assertThrows(HandleValidationField.class, () -> validations.isOpeningHoursValid(null));
  }

  @DisplayName("Should validate address")
  @Test
  public void testAddressIsValid() {
    assertTrue(validations.isAddressValid(address));
  }

  @DisplayName("Should throw exception when street is null")
  @Test
  public void testStreetIsNull() {
    address.setStreet(null);
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("Street is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when street is empty")
  @Test
  public void testStreetEmpty() {
    address.setStreet("");
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("Street is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when street is blank")
  @Test
  public void testStreetBlank() {
    address.setStreet("");
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("Street is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when city is null")
  @Test
  public void testCityNull() {
    address.setCity(null);
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("City is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when city is empty")
  @Test
  public void testCityEmpty() {
    address.setCity("");
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("City is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when state is null")
  @Test
  public void testStateNull() {
    address.setState(null);
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("State is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when state is empty")
  @Test
  public void testStateEmpty() {
    address.setState("");
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("State is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when zipcode is null")
  @Test
  public void testZipCodeNull() {
    address.setZipcode(null);
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("Zipcode is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when zipcode is empty")
  @Test
  public void testZipCodeEmpty() {
    address.setZipcode("");
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isAddressValid(address));
    assertEquals("Zipcode is mandatory!", exception.getMessage());
  }

  @DisplayName("Should validate price is not null")
  @Test
  public void testValidatePriceNotNull() {
    assertTrue(validations.isPriceValid(9.99));
  }

  @DisplayName("Should throw exception when price is null")
  @Test
  public void testPriceNull() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPriceValid(null));
    assertEquals("Price is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when price is not a number")
  @Test
  public void testPriceNotNumber() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPriceValid(Double.NaN));
    assertEquals("Price needs to be a number", exception.getMessage());
  }

  @DisplayName("Should validate promo days")
  @Test
  public void testPromoDays() {
    assertTrue(validations.isPromoDaysValid("Monday - Friday"));
  }

  @DisplayName("Should throw exception when promo day is null")
  @Test
  public void testPromoDaysNull() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPromoDaysValid(null));
    assertEquals("Promotion days is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when promo day is blank")
  @Test
  public void testPromoDaysBlank() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPromoDaysValid(""));
    assertEquals("Promotion days is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when promo day is empty")
  @Test
  public void testPromoDaysEmpty() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPromoDaysValid(" "));
    assertEquals("Promotion days is mandatory!", exception.getMessage());
  }

  @DisplayName("Should validate PromoHours")
  @Test
  public void testPromoHours() {
    assertTrue(validations.isPromoHoursValid("18:00 - 22:00"));
  }

  @DisplayName("Should throw exception when promo hours is null")
  @Test
  public void testPromoHoursNull() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPromoHoursValid(null));
    assertEquals("Promotion hours is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when promo hours is blank")
  @Test
  public void testPromoHoursBlank() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPromoHoursValid(""));
    assertEquals("Promotion hours is mandatory!", exception.getMessage());
  }

  @DisplayName("Should throw exception when promo hours is empty")
  @Test
  public void testPromoHoursEmpty() {
    HandleValidationField exception = assertThrows(HandleValidationField.class, () -> validations.isPromoHoursValid(" "));
    assertEquals("Promotion hours is mandatory!", exception.getMessage());
  }
}
