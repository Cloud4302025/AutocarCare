package com.spring.jwt.SparePartTransaction.SaveInvoiceData.Controller;

import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationLabourLineDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationPartLineDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.Service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @PostMapping("/add")
    public ResponseEntity<QuotationDTO> createQuotation(@RequestBody QuotationDTO quotationDTO) {
        QuotationDTO createdQuotation = quotationService.createQuotation(quotationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuotation);
    }

    @PostMapping("/{id}/parts")
    public ResponseEntity<QuotationDTO> addPartLines(@PathVariable("id") Long quotationId,
                                                     @RequestBody List<QuotationPartLineDTO> partLines) {
        QuotationDTO updatedQuotation = quotationService.addPartLines(quotationId, partLines);
        return ResponseEntity.ok(updatedQuotation);
    }

    @PostMapping("/{id}/labours")
    public ResponseEntity<QuotationDTO> addLabourLines(@PathVariable("id") Long quotationId,
                                                       @RequestBody List<QuotationLabourLineDTO> labourLines) {
        QuotationDTO updatedQuotation = quotationService.addLabourLines(quotationId, labourLines);
        return ResponseEntity.ok(updatedQuotation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationDTO> getQuotationById(@PathVariable("id") Long quotationId) {
        QuotationDTO quotationDTO = quotationService.getQuotationById(quotationId);
        return ResponseEntity.ok(quotationDTO);
    }

    @GetMapping
    public ResponseEntity<List<QuotationDTO>> getAllQuotations() {
        List<QuotationDTO> quotations = quotationService.getAllQuotations();
        return ResponseEntity.ok(quotations);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<QuotationDTO> updateQuotation(@PathVariable("id") Long quotationId,
                                                        @RequestBody Map<String, Object> updates) {
        QuotationDTO updatedQuotation = quotationService.updateQuotation(quotationId, updates);
        return ResponseEntity.ok(updatedQuotation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotation(@PathVariable("id") Long id) {
        quotationService.deleteQuotation(id);
        return ResponseEntity.noContent().build();
    }
}
