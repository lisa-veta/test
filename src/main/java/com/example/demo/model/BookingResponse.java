package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonSerialize
@Getter
@Setter
public class BookingResponse {
    private int bookingId;
    private BookingRequest bookingRequest;
    public BookingResponse(int bookingId, BookingRequest bookingRequest) {
        this.bookingId = bookingId;
        this.bookingRequest = bookingRequest;
    }
}
