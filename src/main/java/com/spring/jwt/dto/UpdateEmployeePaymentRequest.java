package com.spring.jwt.dto;


public class UpdateEmployeePaymentRequest {

    private Integer salary;
    private Integer advancePayment;

    // Getters and Setters
    public Integer getSalary() {
        return salary;
    }
    public void setSalary(Integer salary) {
        this.salary = salary;
    }
    public Integer getAdvancePayment() {
        return advancePayment;
    }
    public void setAdvancePayment(Integer advancePayment) {
        this.advancePayment = advancePayment;
    }
}
