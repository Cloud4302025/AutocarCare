package com.spring.jwt.VehicleReg.Servcie;

import com.spring.jwt.Appointment.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping("/AddService")
    public ResponseEntity<ADDService> createService(@RequestBody ADDService service) {
        ADDService createdService = serviceService.createService(service);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<ADDService> getServiceById(@PathVariable Integer id) {
        ADDService service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ADDService>> getAllServices() {
        List<ADDService> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<ADDService> updateServicePartial(@PathVariable Integer id,
                                                           @RequestBody Map<String, Object> updates) {
        ADDService updatedService = serviceService.updateServicePartial(id, updates);
        return ResponseEntity.ok(updatedService);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ADDService>> searchServices(@RequestParam String serviceName) {
        List<ADDService> services = serviceService.searchByServiceName(serviceName);
        return ResponseEntity.ok(services);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok(Map.of("message", "Service with ID " + id + " deleted successfully"));
    }
}
