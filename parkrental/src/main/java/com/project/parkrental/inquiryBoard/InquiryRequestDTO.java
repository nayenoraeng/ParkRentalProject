package com.project.parkrental.inquiryBoard;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequestDTO {

    private Long idx;
    private Integer parentIdx;
    private String username;
    private String parentUsername;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private LocalDateTime postdate;
    private Integer viewCount;
    private Integer responses;
    private String ofile;
    private String sfile;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String inquiryPassword;

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

    public @NotBlank(message = "제목을 입력해주세요.") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "제목을 입력해주세요.") String title) {
        this.title = title;
    }

    public @NotBlank(message = "내용을 입력해주세요.") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "내용을 입력해주세요.") String content) {
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

    public @NotBlank(message = "비밀번호를 입력해주세요.") String getInquiryPassword() {
        return inquiryPassword;
    }

    public void setInquiryPassword(@NotBlank(message = "비밀번호를 입력해주세요.") String inquiryPassword) {
        this.inquiryPassword = inquiryPassword;
    }
}
