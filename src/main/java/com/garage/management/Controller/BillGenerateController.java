//package com.garage.management.Controller;
//
//import com.garage.management.DTO.RecentServiceDTO;
//import com.garage.management.DTO.ServiceRecordDTO;
//import com.garage.management.Entity.ServiceRecord;
//import com.garage.management.Repository.ServiceRecordRepository;
//import com.garage.management.Service.BillGenerateService;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//
//@RestController
//@RequestMapping("/api/invoice")
//public class BillGenerateController {
//
//    private final BillGenerateService invoiceService;
//    private final ServiceRecordRepository recordRepo;
//
//    public BillGenerateController(BillGenerateService invoiceService, ServiceRecordRepository recordRepo) {
//        this.invoiceService = invoiceService;
//        this.recordRepo = recordRepo;
//    }
//
//    @GetMapping("/download/{id}")
//    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
//
//
//        ServiceRecordDTO  record = recordRepo.findDTOById(id);
//        if (record == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        byte[] pdf = invoiceService.generateInvoice(record);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invoice-" + id + ".pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(pdf);
//    }
//}
