package com.spring.jwt.VehicleReg.Servcie;


import com.spring.jwt.entity.VehicleReg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ADDService createService(ADDService service) {
        return serviceRepository.save(service);
    }

    public ADDService getServiceById(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id " + id));
    }

    public List<ADDService> getAllServices() {
        return serviceRepository.findAll();
    }

    @Transactional
    public ADDService updateServicePartial(Integer id, Map<String, Object> updates) {
        ADDService service = getServiceById(id);

        if (updates.containsKey("serviceName")) {
            service.setServiceName((String) updates.get("serviceName"));
        }
        if (updates.containsKey("serviceRate")) {
              service.setServiceRate((Integer) updates.get("serviceRate"));
        }
        if (updates.containsKey("totalGst")) {
            service.setTotalGst((Integer) updates.get("totalGst"));
        }

        return serviceRepository.save(service);
    }

    public List<ADDService> searchByServiceName(String serviceName) {
        return serviceRepository.findByServiceName(serviceName);
    }

    @Transactional
    public void deleteService(Integer id) {
        ADDService service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id " + id));
        serviceRepository.deleteById(id);
    }
}
