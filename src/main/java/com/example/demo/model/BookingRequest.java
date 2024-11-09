package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@JsonSerialize
@Getter
@Setter
public class BookingRequest {
    private String firstMame;
    private String lastName;
    private int totalPrice;
    private boolean depositPaid;
    private BookingDates bookingDates;
    private String additionalNeeds;

    public BookingRequest(String firstname, String lastname, int totalprice, boolean depositpaid, LocalDate checkin, LocalDate checkout, String additionalneeds) {
        this.firstMame = firstname;
        this.lastName = lastname;
        this.totalPrice = totalprice;
        this.depositPaid = depositpaid;
        this.bookingDates = new BookingDates(checkin, checkout);
        this.additionalNeeds = additionalneeds;
    }
}

@JsonSerialize
@Getter
@Setter
class BookingDates {
    private LocalDate checkin;
    private LocalDate checkout;

    public BookingDates(LocalDate checkin, LocalDate checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }
}

