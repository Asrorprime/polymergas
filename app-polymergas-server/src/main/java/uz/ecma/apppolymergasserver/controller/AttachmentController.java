package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @PostMapping
    public ApiResponseModel uploadFile(MultipartHttpServletRequest request) throws IOException {
        return attachmentService.uploadFile(request);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable UUID id, HttpServletResponse response) {
        return attachmentService.getAttachmentContent(id, response);
    }

    @PostMapping("/delete")
    public HttpEntity<?> deleteFiles(@RequestBody List<UUID> list) {
        return ResponseEntity.ok(attachmentService.deleteFiles(list));
    }
}
