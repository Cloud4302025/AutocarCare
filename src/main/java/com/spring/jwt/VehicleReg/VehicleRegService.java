package com.spring.jwt.VehicleReg;

import com.spring.jwt.entity.VehicleReg;

import java.util.List;

public interface VehicleRegService {
    VehicleRegDto getVehicleRegById(Integer vehicleRegId);

    List<VehicleRegDto> getAllVehicleRegs();

    VehicleRegDto createVehicleReg(VehicleRegDto vehicleRegDto);

    VehicleRegDto updateVehicleReg(Integer vehicleRegId, VehicleRegDto vehicleRegDto);

    void deleteVehicleReg(Integer vehicleRegId);

    public List<VehicleRegDto> getByStatus(String status);

    public List<VehicleRegDto> getByDateRange(String startDate, String endDate);

    VehicleRegDto getVehicleRegByAppointmentId(Integer appointmentId);

    List<VehicleRegDto> searchVehicles(String query);

     List<VehicleRegDto> getVehicleRegByVehicleNumber(String vehicleNumber);

    VehicleRegDetailsDto getVehicleDetailsByNumber(String vehicleNumber);

    List<VehicleRegDto> getActiveInsurances();

    List<VehicleRegDto> getExpiredInsurances();


    List<VehicleRegDto> getBySuperwiserAndWorkerAndTechnician(String startDate, String endDate,String technician,String superwiser,String worker);
}