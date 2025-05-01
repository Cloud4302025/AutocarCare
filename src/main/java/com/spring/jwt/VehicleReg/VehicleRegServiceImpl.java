package com.spring.jwt.VehicleReg;

import com.spring.jwt.Appointment.AppointmentRepository;
import com.spring.jwt.entity.Appointment;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.VehicleReg;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

@Service
public class VehicleRegServiceImpl implements VehicleRegService {

    @Autowired
    private VehicleRegRepository vehicleRegRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public VehicleRegDto getVehicleRegById(Integer vehicleRegId) {
        VehicleReg vehicleReg = vehicleRegRepository.findById(vehicleRegId).orElseThrow(() -> new RuntimeException("VehicleReg not found"));
        return new VehicleRegDto(vehicleReg);
    }

    @Override
    public List<VehicleRegDto> getAllVehicleRegs() {
        List<VehicleReg> vehicleRegs = vehicleRegRepository.findAll();
        return vehicleRegs.stream().map(VehicleRegDto::new).collect(Collectors.toList());
    }

    @Override
    public VehicleRegDto createVehicleReg(VehicleRegDto vehicleRegDto) {

        VehicleReg vehicleReg = new VehicleReg();
        copyDtoToEntity(vehicleRegDto, vehicleReg);
        User user = userRepository.findByEmail(vehicleRegDto.getEmail());

        if (user == null) {
            user = new User();
            user.setEmail(vehicleRegDto.getEmail());
            user.setAddress(vehicleRegDto.getCustomerAddress());
            user.setMobileNumber(Long.valueOf(vehicleRegDto.getCustomerMobileNumber()));
            user.setFirstName(vehicleRegDto.getCustomerName());
            user.setPassword(passwordEncoder.encode("*#Aht%43fcc"));
            Role defaultRole = roleRepository.findByName("USER");
            if (defaultRole == null) {
                defaultRole = new Role("USER");
                defaultRole = roleRepository.save(defaultRole);
            }
            user.setRoles(new HashSet<>(Arrays.asList(defaultRole)));

            userRepository.save(user);
        }
        vehicleReg.setUserId(user.getId());
        vehicleReg = vehicleRegRepository.save(vehicleReg);
        return new VehicleRegDto(vehicleReg);
    }

    @Override
    public VehicleRegDto updateVehicleReg(Integer vehicleRegId, VehicleRegDto vehicleRegDto) {
        VehicleReg vehicleReg = vehicleRegRepository.findById(vehicleRegId)
                .orElseThrow(() -> new RuntimeException("VehicleReg not found"));

        if (vehicleRegDto.getVehicleNumber() != null) {
            vehicleReg.setVehicleNumber(vehicleRegDto.getVehicleNumber());
        }
        if (vehicleRegDto.getVehicleBrand() != null) {
            vehicleReg.setVehicleBrand(vehicleRegDto.getVehicleBrand());
        }
        if (vehicleRegDto.getVehicleModelName() != null) {
            vehicleReg.setVehicleModelName(vehicleRegDto.getVehicleModelName());
        }
        if (vehicleRegDto.getManufactureYear() != null) {
            vehicleReg.setManufactureYear(vehicleRegDto.getManufactureYear());
        }
        if (vehicleRegDto.getEmail() != null) {
            vehicleReg.setEmail(vehicleRegDto.getEmail());
        }
        if (vehicleRegDto.getAdvancePayment() != null) {
            vehicleReg.setAdvancePayment(vehicleRegDto.getAdvancePayment());
        }
        if (vehicleRegDto.getVehicleVariant() != null) {
            vehicleReg.setVehicleVariant(vehicleRegDto.getVehicleVariant());
        }
        if (vehicleRegDto.getEngineNumber() != null) {
            vehicleReg.setEngineNumber(vehicleRegDto.getEngineNumber());
        }
        if (vehicleRegDto.getChasisNumber() != null) {
            vehicleReg.setChasisNumber(vehicleRegDto.getChasisNumber());
        }
        if (vehicleRegDto.getNumberPlateColour() != null) {
            vehicleReg.setNumberPlateColour(vehicleRegDto.getNumberPlateColour());
        }
        if (vehicleRegDto.getKmsDriven() != null) {
            vehicleReg.setKmsDriven(vehicleRegDto.getKmsDriven());
        }
        if (vehicleRegDto.getCustomerId() != null) {
            vehicleReg.setCustomerId(vehicleRegDto.getCustomerId());
        }
        if (vehicleRegDto.getCustomerName() != null) {
            vehicleReg.setCustomerName(vehicleRegDto.getCustomerName());
        }
        if (vehicleRegDto.getCustomerAddress() != null) {
            vehicleReg.setCustomerAddress(vehicleRegDto.getCustomerAddress());
        }
        if (vehicleRegDto.getCustomerMobileNumber() != null) {
            vehicleReg.setCustomerMobileNumber(vehicleRegDto.getCustomerMobileNumber());
        }
        if (vehicleRegDto.getCustomerAadharNo() != null) {
            vehicleReg.setCustomerAadharNo(vehicleRegDto.getCustomerAadharNo());
        }
        if (vehicleRegDto.getCustomerGstin() != null) {
            vehicleReg.setCustomerGstin(vehicleRegDto.getCustomerGstin());
        }
        if (vehicleRegDto.getSuperwiser() != null) {
            vehicleReg.setSuperwiser(vehicleRegDto.getSuperwiser());
        }
        if (vehicleRegDto.getTechnician() != null) {
            vehicleReg.setTechnician(vehicleRegDto.getTechnician());
        }
        if (vehicleRegDto.getWorker() != null) {
            vehicleReg.setWorker(vehicleRegDto.getWorker());
        }
        if (vehicleRegDto.getStatus() != null) {
            vehicleReg.setStatus(vehicleRegDto.getStatus());
        }
        if (vehicleRegDto.getUserId() != null) {
            vehicleReg.setUserId(vehicleRegDto.getUserId());
        }
        if (vehicleRegDto.getDate() != null) {
            vehicleReg.setDate(vehicleRegDto.getDate());
        }
        if (vehicleRegDto.getVehicleInspection() != null) {
            vehicleReg.setVehicleInspection(vehicleRegDto.getVehicleInspection());
        }
        if (vehicleRegDto.getJobCard() != null) {
            vehicleReg.setJobCard(vehicleRegDto.getJobCard());
        }
        if (vehicleRegDto.getInsuranceStatus() != null) {
            vehicleReg.setInsuranceStatus(vehicleRegDto.getInsuranceStatus());
        }
        if (vehicleRegDto.getInsuredFrom() != null) {
            vehicleReg.setInsuredFrom(vehicleRegDto.getInsuredFrom());
        }
        if (vehicleRegDto.getInsuredTo() != null) {
            vehicleReg.setInsuredTo(vehicleRegDto.getInsuredTo());
        }

        vehicleReg = vehicleRegRepository.save(vehicleReg);
        return new VehicleRegDto(vehicleReg);
    }


    @Override
    public void deleteVehicleReg(Integer vehicleRegId) {
        VehicleReg vehicleReg = vehicleRegRepository.findById(vehicleRegId)
                .orElseThrow(() -> new RuntimeException("VehicleReg not found"));
        vehicleRegRepository.delete(vehicleReg);
    }

    @Override
    public List<VehicleRegDto> getByStatus(String status) {
        List<String> statusList = Arrays.stream(status.split(","))
                .map(s -> s.trim().toLowerCase().replaceAll("\\s+", ""))
                .collect(Collectors.toList());

        List<VehicleReg> vehicleRegs = vehicleRegRepository.findByNormalizedStatusIn(statusList);

        if (vehicleRegs.isEmpty()) {
            throw new RuntimeException("No vehicle registrations found with status: " + status);
        }

        return vehicleRegs.stream()
                .map(VehicleRegDto::new)
                .collect(Collectors.toList());
    }



    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

    @Override
    public List<VehicleRegDto> getByDateRange(String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            List<VehicleReg> vehicleRegs = vehicleRegRepository.findByDateBetween(start, end);

            return vehicleRegs.stream()
                    .map(VehicleRegDto::new)
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Use 'yyyy-MM-dd'");
        }
    }

    @Override
    public VehicleRegDto getVehicleRegByAppointmentId(Integer appointmentId) {
        VehicleReg vehicleReg = (VehicleReg) vehicleRegRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Vehicle registration not found for appointment ID: " + appointmentId));
        return new VehicleRegDto(vehicleReg);
    }

    private void copyDtoToEntity(VehicleRegDto vehicleRegDto, VehicleReg vehicleReg) {
        vehicleReg.setVehicleRegId(vehicleRegDto.getVehicleRegId());
        vehicleReg.setAppointmentId(vehicleRegDto.getAppointmentId());
        vehicleReg.setKmsDriven(vehicleRegDto.getKmsDriven());

        vehicleReg.setVehicleNumber(vehicleRegDto.getVehicleNumber());
        vehicleReg.setVehicleBrand(vehicleRegDto.getVehicleBrand());
        vehicleReg.setVehicleModelName(vehicleRegDto.getVehicleModelName());
        vehicleReg.setVehicleVariant(vehicleRegDto.getVehicleVariant());
        vehicleReg.setEngineNumber(vehicleRegDto.getEngineNumber());
        vehicleReg.setChasisNumber(vehicleRegDto.getChasisNumber());
        vehicleReg.setNumberPlateColour(vehicleRegDto.getNumberPlateColour());
        vehicleReg.setManufactureYear(vehicleRegDto.getManufactureYear());
        vehicleReg.setAdvancePayment(vehicleRegDto.getAdvancePayment());

        vehicleReg.setEmail(vehicleRegDto.getEmail());
        vehicleReg.setCustomerId(vehicleRegDto.getCustomerId());
        vehicleReg.setCustomerName(vehicleRegDto.getCustomerName());
        vehicleReg.setCustomerAddress(vehicleRegDto.getCustomerAddress());
        vehicleReg.setCustomerMobileNumber(vehicleRegDto.getCustomerMobileNumber());
        vehicleReg.setCustomerAadharNo(vehicleRegDto.getCustomerAadharNo());
        vehicleReg.setCustomerGstin(vehicleRegDto.getCustomerGstin());
        vehicleReg.setJobCard(vehicleRegDto.getJobCard());
        vehicleReg.setVehicleInspection(vehicleRegDto.getVehicleInspection());
        vehicleReg.setInsuranceStatus(vehicleRegDto.getInsuranceStatus());
        vehicleReg.setInsuredFrom(vehicleRegDto.getInsuredFrom());
        vehicleReg.setInsuredTo(vehicleRegDto.getInsuredTo());
        vehicleReg.setSuperwiser(vehicleRegDto.getSuperwiser());
        vehicleReg.setTechnician(vehicleRegDto.getTechnician());
        vehicleReg.setWorker(vehicleRegDto.getWorker());
        vehicleReg.setStatus(vehicleRegDto.getStatus());
        vehicleReg.setUserId(vehicleRegDto.getUserId());
        vehicleReg.setDate(vehicleRegDto.getDate());
    }

    @Override
    public List<VehicleRegDto> searchVehicles(String query) {
        List<VehicleReg> bySearchQuery = vehicleRegRepository.findBySearchQuery(query);
        return bySearchQuery.stream()
                .map(VehicleRegDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleRegDto> getVehicleRegByVehicleNumber(String vehicleNumber) {
        List<VehicleReg> vehicleRegs = vehicleRegRepository.findByVehicleNumber(vehicleNumber);
        if (vehicleRegs == null || vehicleRegs.isEmpty()) {
            throw new RuntimeException("Vehicle not found with number: " + vehicleNumber);
        }

        return vehicleRegs.stream()
                .map(VehicleRegDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleRegDetailsDto getVehicleDetailsByNumber(String vehicleNumber) {
        return vehicleRegRepository.findTopByVehicleNumber(vehicleNumber);
    }

    public List<VehicleRegDto> getExpiredInsurances() {
        return vehicleRegRepository.findByInsuredToBefore(LocalDate.now())
                .stream()
                .map(VehicleRegDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleRegDto> getBySuperwiserAndWorkerAndTechnician(String startDate, String endDate, String technician, String superwiser, String worker) {
        try {
            // Initialize a DateTimeFormatter to parse date range
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = startDate != null && !startDate.isEmpty() ? LocalDate.parse(startDate, formatter) : null;
            LocalDate end = endDate != null && !endDate.isEmpty() ? LocalDate.parse(endDate, formatter) : null;

            // Filter vehicle registrations by date range, if provided
            List<VehicleReg> vehicleRegs = vehicleRegRepository.findAll();

            if (start != null && end != null) {
                vehicleRegs = vehicleRegs.stream()
                        .filter(vehicleReg -> !vehicleReg.getDate().isBefore(start) && !vehicleReg.getDate().isAfter(end))
                        .collect(Collectors.toList());
            }

            // Further filter by technician, superwiser, or worker if those are provided
            if (technician != null && !technician.isEmpty()) {
                vehicleRegs = vehicleRegs.stream()
                        .filter(vehicleReg -> technician.equalsIgnoreCase(vehicleReg.getTechnician()))
                        .collect(Collectors.toList());
            }

            if (superwiser != null && !superwiser.isEmpty()) {
                vehicleRegs = vehicleRegs.stream()
                        .filter(vehicleReg -> superwiser.equalsIgnoreCase(vehicleReg.getSuperwiser()))
                        .collect(Collectors.toList());
            }

            if (worker != null && !worker.isEmpty()) {
                vehicleRegs = vehicleRegs.stream()
                        .filter(vehicleReg -> worker.equalsIgnoreCase(vehicleReg.getWorker()))
                        .collect(Collectors.toList());
            }

            // If no records are found, throw an exception
            if (vehicleRegs.isEmpty()) {
                throw new RuntimeException("No vehicle registrations found matching the criteria.");
            }

            // Return the list of filtered VehicleRegDto objects
            return vehicleRegs.stream()
                    .map(VehicleRegDto::new)
                    .collect(Collectors.toList());

        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Use 'yyyy-MM-dd'.");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching vehicle registrations: " + e.getMessage());
        }
    }


    public List<VehicleRegDto> getActiveInsurances() {
        return vehicleRegRepository.findByInsuredToGreaterThanEqual(LocalDate.now())
                .stream()
                .map(VehicleRegDto::new)
                .collect(Collectors.toList());
    }


}