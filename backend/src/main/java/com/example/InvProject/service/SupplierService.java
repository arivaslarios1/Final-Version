package com.example.InvProject.service;

import com.example.InvProject.dto.SupplierRequest;
import com.example.InvProject.entity.Supplier;
import com.example.InvProject.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    public Supplier createSupplier(SupplierRequest request) {
        if (supplierRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email is already in use");
        }
        
        Supplier supplier = new Supplier();
        supplier.setName(request.name());
        supplier.setEmail(request.email());
        supplier.setPhoneNumber(request.phoneNumber());
        supplier.setContactPerson(request.contactPerson());
        
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, SupplierRequest request) {
        Supplier existingSupplier = getSupplierById(id);

        if (!existingSupplier.getEmail().equals(request.email()) && 
            supplierRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email is already in use");
        }

        existingSupplier.setName(request.name());
        existingSupplier.setEmail(request.email());
        existingSupplier.setPhoneNumber(request.phoneNumber());
        existingSupplier.setContactPerson(request.contactPerson());
        
        return supplierRepository.save(existingSupplier);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }
}