package com.smart.shopping.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.shopping.entity.Document;
import com.smart.shopping.service.DocumentService;

@RestController
@RequestMapping("/doc")
public class DocumentController {
	
	@Autowired
	private DocumentService docService;
	
	@PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestBody Document document) throws IOException {
		return docService.saveDocument(document);
    }
	
	@GetMapping("/all/images")
    public ResponseEntity<?> getImages() {
		return docService.getAllImages();
    }
}
