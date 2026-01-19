package com.example.Job_Service.Dto;

import lombok.Data;

@Data
public class BookingDTO {
    private String id;
    private String serviceName;
    private String bookingDate;
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private double totalPrice;

    public BookingDTO( String id,String serviceName, String bookingDate, String status, double totalPrice) {
        this.id = id;
        this.serviceName = serviceName;
        this.bookingDate = bookingDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
