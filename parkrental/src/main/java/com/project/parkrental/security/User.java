package com.project.parkrental.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @Column(nullable = false, unique = true)
    @NotBlank(message="아이디는 필수 값 입력 값입니다.")
    @Pattern(regexp = "^[a-z0-9%+-]{4,12}$", message="아이디는 영문 소문자, 숫자를 사용해 4-12자로 작성하세요. ")
    private String username;

    @Column(nullable = false)
    @NotBlank(message="비밀번호는 필수 값 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(nullable = false)
    @NotBlank(message="이름은 필수 값 입력 값입니다.")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "휴대전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNum;

    @Column(nullable = false, unique = true)
    @NotBlank(message="이메일은 필수 값 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9%+-]+@[A-za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String postcode;
    private String address;

    @NotBlank(message="주소를 더 입력해주세요.")
    private String detailAddress;

    private Timestamp regidate;
    private String authority;

    private int enabled;
    private String provider;
    private String providerId;
    private int isLocked;
    private int failCount;
    private Timestamp lockTimes;

}
