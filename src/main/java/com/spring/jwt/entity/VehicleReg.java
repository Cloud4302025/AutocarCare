package com.spring.jwt.entity;

import com.spring.jwt.VehicleReg.VehicleRegDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class VehicleReg {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer vehicleRegId;

    @Column
    private Integer appointmentId;

    @Column(length = 45)
    private String vehicleNumber;

    @Column(length = 45)
    private String vehicleBrand;

    @Column(length = 45)
    private String vehicleModelName;

    @Column
    private Integer kmsDriven;

    private String fuelType;

    @Column(length = 45)
    private String vehicleVariant;

    private Integer manufactureYear;

    private Integer advancePayment;

    @Column(length = 45)
    private String engineNumber;

    @Column(length = 45)
    private String chasisNumber;

    @Column(length = 45)
    private String numberPlateColour;

    @Column
    private Integer customerId;

    @Column(length = 45)
    private String customerName;

    @Column(length = 45)
    private String customerAddress;

    @Column(length = 45)
    private String customerMobileNumber;

    @Column(length = 45)
    private String customerAadharNo;

    @Column(length = 45)
    private String customerGstin;

    @Column(length = 45)
    private String superwiser;

    @Column(length = 45)
    private String technician;

    @Column(length = 45)
    private String worker;

    @Column
    private String status;

    @Column
    private Integer userId;

    private String email;

    @Column
    private String vehicleInspection;

    @Column
    private String jobCard;

    @Column
    private String insuranceStatus;

    @Column
    private LocalDate insuredFrom;

    @Column
    private LocalDate insuredTo;

    @Column
    private LocalDate date;

    public VehicleReg() {
    }


    public VehicleReg(VehicleRegDto vehicleRegDto) {
        this.kmsDriven=vehicleRegDto.getKmsDriven();
        this.insuredTo = vehicleRegDto.getInsuredTo();
        this.insuredFrom = vehicleRegDto.getInsuredFrom();
        this.insuranceStatus = vehicleRegDto.getInsuranceStatus();
        this.jobCard = vehicleRegDto.getJobCard();
        this.vehicleInspection = vehicleRegDto.getVehicleInspection();
        this.vehicleRegId = vehicleRegDto.getVehicleRegId();
        this.appointmentId = vehicleRegDto.getAppointmentId();
        this.vehicleNumber = vehicleRegDto.getVehicleNumber();
        this.vehicleBrand = vehicleRegDto.getVehicleBrand();
        this.vehicleModelName = vehicleRegDto.getVehicleModelName();
        this.vehicleVariant = vehicleRegDto.getVehicleVariant();
        this.engineNumber = vehicleRegDto.getEngineNumber();
        this.chasisNumber = vehicleRegDto.getChasisNumber();
        this.numberPlateColour = vehicleRegDto.getNumberPlateColour();
        this.customerId = vehicleRegDto.getCustomerId();
        this.advancePayment =vehicleRegDto.getAdvancePayment();
        this.manufactureYear =vehicleRegDto.getManufactureYear();
        this.customerName = vehicleRegDto.getCustomerName();
        this.customerAddress = vehicleRegDto.getCustomerAddress();
        this.customerMobileNumber = vehicleRegDto.getCustomerMobileNumber();
        this.customerAadharNo = vehicleRegDto.getCustomerAadharNo();
        this.customerGstin = vehicleRegDto.getCustomerGstin();
        this.superwiser = vehicleRegDto.getSuperwiser();
        this.technician = vehicleRegDto.getTechnician();
        this.worker = vehicleRegDto.getWorker();
        this.status = vehicleRegDto.getStatus();
        this.userId = vehicleRegDto.getUserId();
        this.date = vehicleRegDto.getDate();
        this.email=vehicleRegDto.getEmail();
    }
}
