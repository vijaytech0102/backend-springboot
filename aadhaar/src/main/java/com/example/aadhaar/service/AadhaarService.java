package com.example.aadhaar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aadhaar.dto.AadhaarDTO;
import com.example.aadhaar.model.Aadhaar;
import com.example.aadhaar.repo.AadhaarRepository;

@Service
public class AadhaarService {
    
    @Autowired
    private AadhaarRepository aadhaarRepository;
    
    // Create - Save Aadhaar
    public AadhaarDTO saveAadhaar(Aadhaar aadhaar) {
        Aadhaar savedAadhaar = aadhaarRepository.save(aadhaar);
        return convertToDTO(savedAadhaar);
    }
    
    // Read - Get all Aadhaar records
    public List<Aadhaar> getAllAadhaar() {
        return aadhaarRepository.findAll();
    }
    
    // Read - Get Aadhaar by ID
    public AadhaarDTO getAadhaarById(Long id) {
        Optional<Aadhaar> aadhaar = aadhaarRepository.findById(id);
        return aadhaar.map(this::convertToDTO).orElse(null);
    }
    
    // Update - Update Aadhaar
    public AadhaarDTO updateAadhaar(Long id, Aadhaar aadhaarDetails) {
        Optional<Aadhaar> existingAadhaar = aadhaarRepository.findById(id);
        
        if (existingAadhaar.isPresent()) {
            Aadhaar aadhaar = existingAadhaar.get();
            
            if (aadhaarDetails.getAadhaarNumber() != null) {
                aadhaar.setAadhaarNumber(aadhaarDetails.getAadhaarNumber());
            }
            if (aadhaarDetails.getAddress() != null) {
                aadhaar.setAddress(aadhaarDetails.getAddress());
            }
            if (aadhaarDetails.getDob() != null) {
                aadhaar.setDob(aadhaarDetails.getDob());
            }
            
            Aadhaar updatedAadhaar = aadhaarRepository.save(aadhaar);
            return convertToDTO(updatedAadhaar);
        }
        
        return null;
    }
    
    // Delete - Delete Aadhaar
    public boolean deleteAadhaar(Long id) {
        if (aadhaarRepository.existsById(id)) {
            aadhaarRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Helper method to convert Aadhaar entity to DTO
    private AadhaarDTO convertToDTO(Aadhaar aadhaar) {
        return new AadhaarDTO(
            aadhaar.getId(),
            aadhaar.getAadhaarNumber(),
            aadhaar.getAddress(),
            aadhaar.getDob()
        );
    }
}
