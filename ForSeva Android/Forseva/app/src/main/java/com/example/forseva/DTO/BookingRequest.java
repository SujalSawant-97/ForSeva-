package com.example.forseva.DTO;

public class BookingRequest {
    private String workerId;
    private String workerNmae;
    private String customerName;
    private String serviceName;        // e.g., "Plumbing"
    private String serviceDescription; // e.g., "Tap Repair"
    private String scheduledAt;        // ISO format: "2026-01-16T14:30:00"
    private double price;              // Hourly rate or fixed price
    private String city;
    private String state;
    private String zipcode;
    private double lat;
    private double longi;

    public BookingRequest(String workerId, String workerNmae, String customerName, String serviceName, String serviceDescription, String scheduledAt, double price, String city, String state, String zipcode, double lat, double longi) {
        this.workerId = workerId;
        this.workerNmae = workerNmae;
        this.customerName = customerName;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.scheduledAt = scheduledAt;
        this.price = price;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.lat = lat;
        this.longi = longi;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerNmae() {
        return workerNmae;
    }

    public void setWorkerNmae(String workerNmae) {
        this.workerNmae = workerNmae;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }
}
