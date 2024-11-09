package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
@AllArgsConstructor
@NoArgsConstructor
class BookingDates {
     LocalDate checkin;
     LocalDate checkout;
}

