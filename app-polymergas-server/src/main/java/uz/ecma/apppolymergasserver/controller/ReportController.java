package uz.ecma.apppolymergasserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.ecma.apppolymergasserver.payload.ReqDashboard;
import uz.ecma.apppolymergasserver.service.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    @PostMapping
    public HttpEntity<?> getDashboard(@RequestBody ReqDashboard reqDashboard){
        return ResponseEntity.ok(reportService.getDashboard(reqDashboard));
    }

}
