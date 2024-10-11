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

}
