package com.project.parkrental.inquiryBoard;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.yaml.snakeyaml.events.Event;

import java.sql.Timestamp;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Builder
@Entity
@Table(name = "inquiry")
public class Inquiry extends BaseTimeEntity {

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public Integer getParentIdx() {
        return parentIdx;
    }

    public void setParentIdx(Integer parentIdx) {
        this.parentIdx = parentIdx;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParentUsername() {
        return parentUsername;
    }

    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostdate() {
        return postdate;
    }

    public void setPostdate(LocalDateTime postdate) {
        this.postdate = postdate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getResponses() {
        return responses;
    }

    public void setResponses(Integer responses) {
        this.responses = responses;
    }

    public String getOfile() {
        return ofile;
    }

    public void setOfile(String ofile) {
        this.ofile = ofile;
    }

    public String getSfile() {
        return sfile;
    }

    public void setSfile(String sfile) {
        this.sfile = sfile;
    }

    public String getInquiryPassword() {
        return inquiryPassword;
    }

    public void setInquiryPassword(String inquiryPassword) {
        this.inquiryPassword = inquiryPassword;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private Integer parentIdx;
    @Column(nullable = false)
    private String username;
    private String parentUsername;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @CreatedDate
    private LocalDateTime postdate;
    private Integer viewCount;
    private Integer responses;
    private String ofile;
    private String sfile;
    @Column(nullable = false)
    private String inquiryPassword;

   public boolean isNew() {
        return postdate==null;
    }

//    @Builder
//    public Inquiry(Integer parentIdx, String username, String parentUsername, String title, String content,
//                   LocalDateTime postdate, Integer viewCount, Integer responses, String ofile, String sfile,
//                   String inquiryPassword) {
//        this.parentIdx = parentIdx;
//        this.username = username;
//        this.parentUsername = parentUsername;
//        this.title = title;
//        this.content = content;
//        this.postdate = postdate;
//        this.viewCount = viewCount;
//        this.responses = responses;
//        this.ofile = ofile;
//        this.sfile = sfile;
//        this.inquiryPassword = inquiryPassword;
//    }

}
