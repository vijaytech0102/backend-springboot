package com.example.aadhaar.dto;

public class AadhaarDTO {
    
    private Long id;
    private String aadhaarNumber;
    private String address;
    private String dob;
    
    public AadhaarDTO() {
    }
    
    public AadhaarDTO(Long id, String aadhaarNumber, String address, String dob) {
        this.id = id;
        this.aadhaarNumber = aadhaarNumber;
        this.address = address;
        this.dob = dob;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAadhaarNumber() {
        return aadhaarNumber;
    }
    
    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDob() {
        return dob;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
}
