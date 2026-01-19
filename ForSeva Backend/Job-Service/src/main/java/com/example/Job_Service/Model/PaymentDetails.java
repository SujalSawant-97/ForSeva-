package com.example.Job_Service.Model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PaymentDetails {
    private Double amount;
    private String paymentStatus; // pending, paid, failed
}
