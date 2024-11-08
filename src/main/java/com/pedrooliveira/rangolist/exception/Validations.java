package com.pedrooliveira.rangolist.exception;

import com.pedrooliveira.rangolist.model.Address;
import org.springframework.stereotype.Component;

@Component
public class Validations {
  public boolean isNameValid(String name) {
    if (name == null || name.isEmpty() || name.isBlank()) {
      throw new HandleValidationField("Name is mandatory!");
    }
    return true;
  }

  public boolean isNameCount(String name) {
    if (name.length() < 2) {
      throw new HandleValidationField("Name must contain at least 2 characters!");
    }
    return true;
  }

  public boolean isOpeningHoursValid(String openingHours) {
    if (openingHours == null || openingHours.isEmpty() || openingHours.isBlank()) {
      throw new HandleValidationField("Opening hours is mandatory!");
    }
    return true;
  }

  public boolean isAddressValid(Address address) {
    if (address.getStreet() == null || address.getStreet().isEmpty() || address.getStreet().isBlank()) {
      throw new HandleValidationField("Street is mandatory!");
    }

    if (address.getCity() == null || address.getCity().isEmpty() || address.getCity().isBlank()) {
      throw new HandleValidationField("City is mandatory!");
    }

    if (address.getState() == null || address.getState().isEmpty() || address.getState().isBlank()) {
      throw new HandleValidationField("State is mandatory!");
    }

    if (address.getZipcode() == null || address.getZipcode().isEmpty() || address.getZipcode().isBlank()) {
      throw new HandleValidationField("Zipcode is mandatory!");
    }
    return true;
  }

  public boolean isPriceValid(Double price) {
    if (price == null) {
      throw new HandleValidationField("Price is mandatory!");
    }

    if (price.isNaN()) {
      throw new HandleValidationField("Price needs to be a number");
    }
    return true;
  }

  public boolean isCategoryValid(String category) {
    if (category == null || category.isEmpty() || category.isBlank()) {
      throw new HandleValidationField("Category is mandatory!");
    }
    return true;
  }

  public boolean isDescriptionValid(String description) {
    if (description == null || description.isEmpty() || description.isBlank()) {
      throw new HandleValidationField("Description is mandatory!");
    }
    return true;
  }

  public boolean isPromoPriceValid(Double promoPrice) {
    if (promoPrice == null) {
      throw new HandleValidationField("Promotion price is mandatory!");
    }

    if (promoPrice.isNaN()) {
      throw new HandleValidationField("Promotion price needs to be a number");
    }
    return true;
  }

  public boolean isPromoDaysValid(String promoDays) {
    if (promoDays == null || promoDays.isEmpty() || promoDays.isBlank()) {
      throw new HandleValidationField("Promotion days is mandatory!");
    }
    return true;
  }

  public boolean isPromoHoursValid(String promoHours) {
    if (promoHours == null || promoHours.isEmpty() || promoHours.isBlank()) {
      throw new HandleValidationField("Promotion hours is mandatory!");
    }
    return true;
  }

}
