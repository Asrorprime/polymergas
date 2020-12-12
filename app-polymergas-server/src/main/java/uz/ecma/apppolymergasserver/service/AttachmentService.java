package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.ecma.apppolymergasserver.entity.Attachment;
import uz.ecma.apppolymergasserver.entity.AttachmentContent;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.payload.ResUploadFile;
import uz.ecma.apppolymergasserver.repository.AttachmentContentRepository;
import uz.ecma.apppolymergasserver.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Transactional
    public ApiResponseModel uploadFile(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> itr = request.getFileNames();
        String id = "";
        MultipartFile file;
        List<ResUploadFile> resUploadFiles = new ArrayList<>();
        id = request.getParameter("type");
        while (itr.hasNext()) {
            file = request.getFile(itr.next());
            Attachment attachment = attachmentRepository.save(new Attachment(file.getOriginalFilename(), file.getContentType(), file.getSize()));
            try {
                attachmentContentRepository.save(new AttachmentContent(attachment, file.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            resUploadFiles.add(new ResUploadFile(attachment.getId(),
                    attachment.getName(),
                    ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/").path(attachment.getId().toString()).toUriString(),
                    attachment.getContentType(),
                    attachment.getSize()));
        }

        return new ApiResponseModel(true, "", resUploadFiles);
    }

    public HttpEntity<?> getAttachmentContent(UUID attachmentId, HttpServletResponse response) {
        Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow(() -> new ResourceNotFoundException("Attachment", "id", attachmentId));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachment(attachment).orElseThrow(() -> new ResourceNotFoundException("Attachment content", "attachment id", attachmentId));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                .body(attachmentContent.getContent());
    }

    public ApiResponse deleteFiles(List<UUID> list) {
        for (UUID uuid : list) {
            attachmentContentRepository.deleteByAttachmentId(uuid);
            attachmentRepository.deleteById(uuid);
        }
        return new ApiResponse("", true);
    }

}
