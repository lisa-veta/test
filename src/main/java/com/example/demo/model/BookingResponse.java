package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private int bookingId;
    private BookingRequest bookingRequest;
}
