package com.smart.shopping.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String documentName;
    @Lob
    private String  documentImage;
    private String documentValue;
    private String moreDetails;
}
