package com.spring.jwt.SparePartTransaction.SaveInvoiceData.Service;

import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationLabourLineDTO;
import com.spring.jwt.SparePartTransaction.SaveInvoiceData.DTO.QuotationPartLineDTO;

import java.util.List;
import java.util.Map;

public interface QuotationService {

    QuotationDTO createQuotation(QuotationDTO quotationDTO);

    QuotationDTO addPartLines(Long quotationId, List<QuotationPartLineDTO> partLines);

    QuotationDTO addLabourLines(Long quotationId, List<QuotationLabourLineDTO> labourLines);

    QuotationDTO getQuotationById(Long quotationId);

    List<QuotationDTO> getAllQuotations();

    QuotationDTO updateQuotation(Long quotationId, Map<String, Object> updates);

    void deleteQuotation(Long id);
}
