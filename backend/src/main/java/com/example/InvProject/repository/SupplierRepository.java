package com.example.InvProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.InvProject.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
}
