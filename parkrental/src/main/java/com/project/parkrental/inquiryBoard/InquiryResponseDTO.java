package com.project.parkrental.inquiryBoard;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@ToString
@Getter @Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseDTO {

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

    public InquiryResponseDTO(Long idx, String title, String content, LocalDateTime postdate, Integer viewCount)
    {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.postdate = postdate;
        this.viewCount = viewCount;
    }
}
