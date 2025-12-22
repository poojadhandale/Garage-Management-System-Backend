//package com.garage.management.Service;
//
//import com.garage.management.DTO.RecentServiceDTO;
//import com.garage.management.DTO.ServiceItemDTO;
//import com.garage.management.DTO.ServiceRecordDTO;
//import com.garage.management.Entity.ServiceItem;
//import com.garage.management.Entity.ServiceRecord;
//import com.garage.management.Repository.ServiceRecordRepository;
//import com.garage.management.Security.DocumentGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Service
//public class BillGenerateService {
//
//    @Autowired
//    private ServiceRecordRepository recordRepo;
//
//    @Autowired
//    private final TemplateEngine templateEngine;
//
//    public BillGenerateService(TemplateEngine templateEngine) {
//        this.templateEngine = templateEngine;
//    }
//
//    public byte[] generateInvoice(ServiceRecordDTO record) {
//
//        Context ctx = new Context();
//
//
//        ctx.setVariable("invoice", record);
//        ctx.setVariable("customer", record.getCustomer());
//        ctx.setVariable("items", record.getStocks());
//        ctx.setVariable("total", record.getTotalCost());
//        ctx.setVariable("gstPercent", 18);
//
//        double gstAmount = record.getTotalCost() * 0.18;
//        ctx.setVariable("gstAmount", gstAmount);
//        ctx.setVariable("grandTotal", record.getTotalCost() + gstAmount);
//
//        // Render Thymeleaf HTML
//        String htmlContent = templateEngine.process("invoice", ctx);
//
//        return DocumentGenerator.generatePdfFromHtml(htmlContent);
//    }
//
//    public ServiceRecordDTO getRecordDTO(Long id,ServiceRecord record) {
//        ServiceRecord items = recordRepo.getRecordWithItems(id);
//        return toDTO(items, (List<ServiceItem>) record);
//    }
//
//    private ServiceRecordDTO toDTO(ServiceRecord record, List<ServiceItem> items) {
//
//        ServiceRecordDTO dto = new ServiceRecordDTO();
//        dto.setId(record.getId());
//        dto.setCustomer(null);
//        dto.setServiceDate(record.getServiceDate());
//        dto.setTotalCost(record.getTotalCost());
//        dto.setRemarks(record.getRemarks());
//
//        List<ServiceItemDTO> itemDTOs = new ArrayList<>();
//        for (ServiceItem it : items) {
//            ServiceItemDTO obj = new ServiceItemDTO();
//            obj.setId(it.getId());
//            obj.setStockId(it.getStock().getId());
//            obj.setItemName(it.getStock().getItemName());
//            obj.setQuantityUsed(it.getQuantityUsed());
//            obj.setPrice(it.getStock().getPrice());
//            itemDTOs.add(obj);
//        }
//
//        dto.setStocks(itemDTOs);
//        return dto;
//    }
//
//}
