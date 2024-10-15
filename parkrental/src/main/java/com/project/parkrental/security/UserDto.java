package com.project.parkrental.security;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String username;
    private String email;
    private String phoneNum;
    private String postcode;
    private String address;
    private String detailAddress;

    public UserDto() {}
    public UserDto(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
        this.email = user.getEmail();
        this.postcode = user.getPostcode();
        this.address = user.getAddress();
        this.detailAddress = user.getDetailAddress();
    }
}
