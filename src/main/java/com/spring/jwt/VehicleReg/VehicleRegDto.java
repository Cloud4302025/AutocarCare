package com.spring.jwt.VehicleReg;

import com.spring.jwt.entity.VehicleReg;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleRegDto {
    private Integer vehicleRegId;
    private Integer appointmentId;

    private String vehicleNumber;
    private String vehicleBrand;
    private String vehicleModelName;
    private String vehicleVariant;
    private String engineNumber;
    private String chasisNumber;
    private String fuelType;
    private String numberPlateColour;
    private Integer kmsDriven;

    private String email;

    private Integer ManufactureYear;

    private Integer advancePayment;

    private Integer customerId;
    private String customerName;
    private String customerAddress;
    private String customerMobileNumber;
    private String customerAadharNo;
    private String customerGstin;

    private String superwiser;
    private String technician;
    private String worker;
    private String status;
    private Integer userId;
    private LocalDate date;

    private String vehicleInspection;
    private String jobCard;
    private String insuranceStatus;
    private LocalDate insuredFrom;
    private LocalDate insuredTo;
    public VehicleRegDto() {
    }

    public VehicleRegDto(VehicleReg vehicleReg) {
        this.kmsDriven=vehicleReg.getKmsDriven();
        this.insuredTo = vehicleReg.getInsuredTo();
        this.insuredFrom = vehicleReg.getInsuredFrom();
        this.insuranceStatus = vehicleReg.getInsuranceStatus();
        this.jobCard = vehicleReg.getJobCard();
        this.vehicleInspection = vehicleReg.getVehicleInspection();
        this.vehicleRegId = vehicleReg.getVehicleRegId();
        this.appointmentId = vehicleReg.getAppointmentId();
        this.fuelType = vehicleReg.getFuelType();
        this.vehicleNumber = vehicleReg.getVehicleNumber();
        this.vehicleBrand = vehicleReg.getVehicleBrand();
        this.vehicleModelName = vehicleReg.getVehicleModelName();
        this.vehicleVariant = vehicleReg.getVehicleVariant();
        this.engineNumber = vehicleReg.getEngineNumber();
        this.chasisNumber = vehicleReg.getChasisNumber();
        this.numberPlateColour = vehicleReg.getNumberPlateColour();
        this.customerId = vehicleReg.getCustomerId();
        this.customerName = vehicleReg.getCustomerName();
        this.customerAddress = vehicleReg.getCustomerAddress();
        this.customerMobileNumber = vehicleReg.getCustomerMobileNumber();
        this.customerAadharNo = vehicleReg.getCustomerAadharNo();
        this.customerGstin = vehicleReg.getCustomerGstin();
        this.superwiser = vehicleReg.getSuperwiser();
        this.technician = vehicleReg.getTechnician();
        this.worker = vehicleReg.getWorker();
        this.advancePayment=vehicleReg.getAdvancePayment();
        this.ManufactureYear=vehicleReg.getManufactureYear();
        this.status = vehicleReg.getStatus();
        this.userId = vehicleReg.getUserId();
        this.date = vehicleReg.getDate();
        this.email=vehicleReg.getEmail();
    }
}
