package com.spring.jwt.vender;

import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/vendor")
@RequiredArgsConstructor
@RestController
public class VendorController {
    public final VendorService vendorService;


    @PostMapping("/registerVendor")
    public ResponseEntity<BaseResponseDTO> registerVendor(@Valid @RequestBody VendorDto vendorDto){
        return ResponseEntity.ok(vendorService.register(vendorDto));
    }

    @GetMapping("/findById")
    public ResponseEntity<?> getUser(@RequestParam Integer vendorId){
       return ResponseEntity.ok(vendorService.getUserById(vendorId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        return vendorService.getAllVendors();
    }


    @PatchMapping("/update/{vendorId}")
    public ResponseEntity<VendorDto> patchUpdateVendor(@PathVariable Integer vendorId,
                                                       @RequestBody VendorDto vendorDto) {

        return vendorService.UpdateVendor(vendorId, vendorDto);
    }

    @DeleteMapping("delete/{vendorId}")
    public ResponseEntity<BaseResponseDTO> deleteVendor(@PathVariable Integer vendorId) {
        vendorService.deleteVendorById(vendorId);
        BaseResponseDTO response = new BaseResponseDTO("200", "Vendor deleted successfully");
        return ResponseEntity.ok(response);
    }


}
