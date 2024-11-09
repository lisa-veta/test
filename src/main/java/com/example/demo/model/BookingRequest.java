package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class BookingRequest {
     String firstname;
     String lastname;
     int totalprice;
     boolean depositpaid;
     BookingDates bookingdates;
     String additionalneeds;

    public BookingRequest(String firstname, String lastname, int totalprice, boolean depositpaid, LocalDate checkin, LocalDate checkout, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = new BookingDates(checkin, checkout);
        this.additionalneeds = additionalneeds;
    }
}


@Getter
@Setter
class BookingDates {
     LocalDate checkin;
     LocalDate checkout;

    public BookingDates(LocalDate checkin, LocalDate checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }
}

