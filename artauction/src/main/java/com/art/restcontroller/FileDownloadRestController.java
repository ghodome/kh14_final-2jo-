package com.art.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.art.dao.AttachmentDao;
import com.art.service.AttachmentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/attach")
public class FileDownloadRestController {
	
	@Autowired
	private AttachmentDao attachmentDao;
	
	@Autowired 
	private AttachmentService attachmentService;
	
	@GetMapping("/download/{attachmentNo}")
	public ResponseEntity<ByteArrayResource> download(
				@PathVariable int attachmentNo) throws IOException {
//		log.info("attachmentNo={}",attachmentNo);
		return attachmentService.find(attachmentNo);
	}
	
}
