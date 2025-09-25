package com.spring.jwt.SparePartTransaction.SaveInvoiceData.Service;

import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationLabourLineDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationPartLineDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.Quotation;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.QuotationPartLine;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.QuotationlabourLine;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.Repository.QuotationRepository;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuotationServiceImpl implements QuotationService {

    @Autowired
    private QuotationRepository quotationRepository;

    @Override
    public QuotationDTO createQuotation(QuotationDTO quotationDTO) {
        Quotation quotation = new Quotation();
        quotation.setQuotationNumber(quotationDTO.getQuotationNumber());
        quotation.setQuotationDate(quotationDTO.getQuotationDate());
        quotation.setCustomerName(quotationDTO.getCustomerName());
        quotation.setCustomerAddress(quotationDTO.getCustomerAddress());
        quotation.setCustomerMobile(quotationDTO.getCustomerMobile());
        quotation.setVehicleNumber(quotationDTO.getVehicleNumber());
        quotation.setCustomerEmail(quotationDTO.getCustomerEmail());
        Quotation saved = quotationRepository.save(quotation);
        return convertToDTO(saved);
    }

    @Override
    public QuotationDTO addPartLines(Long quotationId, List<QuotationPartLineDTO> partLineDTOs) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id " + quotationId));
        for (QuotationPartLineDTO dto : partLineDTOs) {
            QuotationPartLine line = new QuotationPartLine();
            line.setLineNo(dto.getLineNo());
            line.setPartName(dto.getPartName());
            line.setQuantity(dto.getQuantity());
            line.setUnitPrice(dto.getUnitPrice());
            line.setDiscountPercent(dto.getDiscountPercent());
            line.setDiscountAmt(dto.getDiscountAmt());
            line.setFinalAmount(dto.getFinalAmount());
            line.setQuotation(quotation);
            quotation.getPartLines().add(line);
        }
        Quotation updated = quotationRepository.save(quotation);
        return convertToDTO(updated);
    }

    @Override
    public QuotationDTO addLabourLines(Long quotationId, List<QuotationLabourLineDTO> labourLineDTOs) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id " + quotationId));
        for (QuotationLabourLineDTO dto : labourLineDTOs) {
            QuotationlabourLine line = new QuotationlabourLine();
            line.setLineNo(dto.getLineNo());
            line.setName(dto.getName());
            line.setQuantity(dto.getQuantity());
            line.setUnitPrice(dto.getUnitPrice());
            line.setDiscountPercent(dto.getDiscountPercent());
            line.setDiscountAmt(dto.getDiscountAmt());
            line.setFinalAmount(dto.getFinalAmount());
            line.setQuotation(quotation);
            quotation.getLabourLines().add(line);
        }
        Quotation updated = quotationRepository.save(quotation);
        return convertToDTO(updated);
    }

    @Override
    public QuotationDTO getQuotationById(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id " + quotationId));
        return convertToDTO(quotation);
    }

    @Override
    public List<QuotationDTO> getAllQuotations() {
        List<Quotation> quotations = quotationRepository.findAll();
        return quotations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public QuotationDTO updateQuotation(Long quotationId, Map<String, Object> updates) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id " + quotationId));

        if (updates.containsKey("quotationNumber")) {
            quotation.setQuotationNumber((String) updates.get("quotationNumber"));
        }
        if (updates.containsKey("quotationDate")) {
            quotation.setQuotationDate(LocalDate.parse((String) updates.get("quotationDate")));
        }
        if (updates.containsKey("customerName")) {
            quotation.setCustomerName((String) updates.get("customerName"));
        }
        if (updates.containsKey("customerAddress")) {
            quotation.setCustomerAddress((String) updates.get("customerAddress"));
        }
        if (updates.containsKey("customerMobile")) {
            quotation.setCustomerMobile((String) updates.get("customerMobile"));
        }
        if (updates.containsKey("vehicleNumber")) {
            quotation.setVehicleNumber((String) updates.get("vehicleNumber"));
        }
        if (updates.containsKey("customerEmail")) {
            quotation.setCustomerEmail((String) updates.get("customerEmail"));
        }
        Quotation updated = quotationRepository.save(quotation);
        return convertToDTO(updated);
    }

    private QuotationDTO convertToDTO(Quotation quotation) {
        QuotationDTO dto = new QuotationDTO();
        dto.setId(quotation.getId());
        dto.setQuotationNumber(quotation.getQuotationNumber());
        dto.setQuotationDate(quotation.getQuotationDate());
        dto.setCustomerName(quotation.getCustomerName());
        dto.setCustomerAddress(quotation.getCustomerAddress());
        dto.setCustomerMobile(quotation.getCustomerMobile());
        dto.setVehicleNumber(quotation.getVehicleNumber());
        dto.setCustomerEmail(quotation.getCustomerEmail());

        List<QuotationPartLineDTO> partLineDTOs = quotation.getPartLines().stream().map(line -> {
            QuotationPartLineDTO d = new QuotationPartLineDTO();
            d.setId(line.getId());
            d.setLineNo(line.getLineNo());
            d.setPartName(line.getPartName());
            d.setQuantity(line.getQuantity());
            d.setUnitPrice(line.getUnitPrice());
            d.setDiscountPercent(line.getDiscountPercent());
            d.setDiscountAmt(line.getDiscountAmt());
            d.setFinalAmount(line.getFinalAmount());
            return d;
        }).collect(Collectors.toList());
        dto.setPartLines(partLineDTOs);

        List<QuotationLabourLineDTO> labourLineDTOs = quotation.getLabourLines().stream().map(line -> {
            QuotationLabourLineDTO d = new QuotationLabourLineDTO();
            d.setId(line.getId());
            d.setLineNo(line.getLineNo());
            d.setName(line.getName());
            d.setQuantity(line.getQuantity());
            d.setUnitPrice(line.getUnitPrice());
            d.setDiscountPercent(line.getDiscountPercent());
            d.setDiscountAmt(line.getDiscountAmt());
            d.setFinalAmount(line.getFinalAmount());
            return d;
        }).collect(Collectors.toList());
        dto.setLabourLines(labourLineDTOs);

        return dto;
    }

    @Override
    @Transactional
    public void deleteQuotation(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new IllegalArgumentException("No quotation with id " + quotationId));

        quotationRepository.delete(quotation);

    }
}
