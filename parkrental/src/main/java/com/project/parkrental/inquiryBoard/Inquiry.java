package com.project.parkrental.inquiryBoard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Inquiry {

    @Id
    private Long idx;
    private int parentIdx;
    private String username;
    private String parentUsername;
    private String title;
    private String content;
    private Timestamp postdate;
    private int viewCount;
    private int responses;
    private String ofile;
    private String sfile;
    @Column(name = "inquiryPassword")
    private String inquiryPassword;
}
