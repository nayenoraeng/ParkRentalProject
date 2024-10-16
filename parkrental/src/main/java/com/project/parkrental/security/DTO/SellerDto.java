package com.project.parkrental.security.DTO;

import lombok.Data;

@Data
public class SellerDto {
    private String name;
    private String username;
    private String businessNum;
    private String email;
    private String phoneNum;
    private String postcode;
    private String address;
    private String detailAddress;
    public SellerDto() {}
    public SellerDto(Seller seller) {
        this.name = seller.getName();
        this.username = seller.getUsername();
        this.phoneNum = seller.getPhoneNum();
        this.email = seller.getEmail();
        this.postcode = seller.getPostcode();
        this.address = seller.getAddress();
        this.detailAddress = seller.getDetailAddress();
    }
}
