package com.example.forseva.DTO;

public class BookingModel {
    private String  id;
    private String serviceName;
    private String bookingDate;
    private String status; // PENDING, CONFIRMED, COMPLETED, CANCELLED
    private double totalPrice;

    public String   getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
