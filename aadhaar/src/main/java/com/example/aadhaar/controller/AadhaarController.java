package com.example.aadhaar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.aadhaar.dto.AadhaarDTO;
import com.example.aadhaar.model.Aadhaar;
import com.example.aadhaar.service.AadhaarService;

@RestController
@RequestMapping("/aadhaar")
public class AadhaarController {
    
    @Autowired
    private AadhaarService aadhaarService;
    
    // Create - Post Aadhaar
    @PostMapping
    public AadhaarDTO createAadhaar(@RequestBody Aadhaar aadhaar) {
        return aadhaarService.saveAadhaar(aadhaar);
    }
    
    // Read - Get all Aadhaar records
    @GetMapping
    public List<Aadhaar> getAllAadhaar() {
        return aadhaarService.getAllAadhaar();
    }
    
    // Read - Get Aadhaar by ID
    @GetMapping("/{id}")
    public AadhaarDTO getAadhaarById(@PathVariable Long id) {
        return aadhaarService.getAadhaarById(id);
    }
    
    // Update - Update Aadhaar
    @PutMapping("/{id}")
    public AadhaarDTO updateAadhaar(@PathVariable Long id, @RequestBody Aadhaar aadhaarDetails) {
        return aadhaarService.updateAadhaar(id, aadhaarDetails);
    }
    
    // Delete - Delete Aadhaar
    @DeleteMapping("/{id}")
    public String deleteAadhaar(@PathVariable Long id) {
        boolean isDeleted = aadhaarService.deleteAadhaar(id);
        if (isDeleted) {
            return "Aadhaar record deleted successfully";
        }
        return "Aadhaar record not found";
    }
}
