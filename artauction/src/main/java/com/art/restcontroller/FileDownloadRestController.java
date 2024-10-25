package com.art.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.art.dao.AttachmentDao;
import com.art.service.AttachmentService;

@RestController
@RequestMapping("/attach")
public class FileDownloadRestController {
	
//	@Autowired
//	private AttachmentDao attachmentDao;
	
	@Autowired AttachmentService attachmentService;
	
	@GetMapping("/")
	public ResponseEntity<ByteArrayResource> download(
				@RequestParam int attachmentNo) throws IOException {
		return attachmentService.find(attachmentNo);
	}
	
}
