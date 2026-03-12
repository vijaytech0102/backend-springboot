package com.example.aadhaar.dto;

public class UserDTO {
    
    private Long id;
    private String name;
    private String email;
    private String phone;
    private AadhaarDTO aadhaar;
    
    public UserDTO() {
    }
    
    public UserDTO(Long id, String name, String email, String phone, AadhaarDTO aadhaar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.aadhaar = aadhaar;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public AadhaarDTO getAadhaar() {
        return aadhaar;
    }
    
    public void setAadhaar(AadhaarDTO aadhaar) {
        this.aadhaar = aadhaar;
    }
}
