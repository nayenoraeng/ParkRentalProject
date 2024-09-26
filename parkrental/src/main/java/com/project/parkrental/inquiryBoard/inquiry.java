package com.project.parkrental.inquiryBoard;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class inquiry {

    @Id
    private Long idx;
    private int parentIdx;
    private String username;
    private String parentUsername;
    private String title;
    private String content;
    private Timestamp postdate;
    private int viewCount;
    private int response;
    private String ofile;
    private String sfile;
    private String inquiryPassword;
}
