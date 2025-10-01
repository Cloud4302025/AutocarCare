package com.spring.jwt.Appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/getAll")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // To get All Appoinments
    @GetMapping("/getById" )
    public ResponseEntity<AppointmentDto> getAppointmentById(@RequestParam Integer id) {
        try {
            AppointmentDto appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointment);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto savedAppointment = appointmentService.saveAppointment(appointmentDto);
        return ResponseEntity.ok(savedAppointment);
    }


    @DeleteMapping("/Deleted")
    public ResponseEntity<Void> deleteAppointment(@RequestParam Integer id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getByStatus")
    public ResponseEntity<ResponseDto<List<AppointmentDto>>> getByStatus(@RequestParam String status) {
        List<AppointmentDto> appointments = appointmentService.getByStatus(status);
        return ResponseEntity.ok(ResponseDto.success("Appointments retrieved successfully", appointments));
    }


    @GetMapping("/getByUserId")
    public ResponseEntity<ResponseDto<List<AppointmentDto>>> getByUserId(@RequestParam Integer userId) {
        List<AppointmentDto> appointments = appointmentService.getByUserId(userId);
        return ResponseEntity.ok(ResponseDto.success("Appointments retrieved successfully", appointments));
    }


    @GetMapping("/getByUserIdAndStatus")
    public ResponseEntity<ResponseDto<List<AppointmentDto>>> getByUserIdAndStatus(
            @RequestParam Integer userId, @RequestParam String status) {
        List<AppointmentDto> appointments = appointmentService.getByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(ResponseDto.success("Appointments retrieved successfully", appointments));
    }
}
