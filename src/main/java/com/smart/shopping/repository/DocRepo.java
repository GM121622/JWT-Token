package com.smart.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.shopping.entity.Document;

@Repository
public interface DocRepo extends JpaRepository<Document, Long> {

}
