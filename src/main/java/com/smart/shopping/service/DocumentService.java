package com.smart.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smart.shopping.config.HTTPStatusCodes;
import com.smart.shopping.config.ShoppingCodes;
import com.smart.shopping.entity.Document;
import com.smart.shopping.repository.DocRepo;
import com.smart.shopping.utility.ResponseEntityBuilder;

@Service
public class DocumentService {
	
	private ResponseEntityBuilder responseEntityBuilder = new ResponseEntityBuilder();
	
	@Autowired
	private DocRepo docRepo;  
	
	public ResponseEntity<?> saveDocument(Document document) {
		if(document==null)
		{
			return responseEntityBuilder.createFailureResponse(HTTPStatusCodes.BAD_REQUEST.getHTTPCode(),
					"null information",
					new String[] { ShoppingCodes.VALIDATION_EXCEPTION.getShoppingCodes() });
		}
		 docRepo.save(document);
		 return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
					"Document saved Successfully",
					new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() },document);
	}

	public ResponseEntity<?> getAllImages() {
		 return responseEntityBuilder.createSuccessResponse(HTTPStatusCodes.OK.getHTTPCode(),
					"Document saved Successfully",
					new String[] { ShoppingCodes.TRANSACTION_SUCCESSFUL.getShoppingCodes() },docRepo.findAll());
	}
}
