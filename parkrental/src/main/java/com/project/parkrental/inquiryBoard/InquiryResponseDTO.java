package com.project.parkrental.inquiryBoard;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
@Getter @Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponseDTO {

    private Long idx;
    private Long originNo;
    private Integer groupOrd;
    private Integer groupLayer;
    private String username;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private LocalDateTime postdate;
    private Integer viewCount;
    private Integer responses;
    private String ofile;
    private String sfile;

    public InquiryResponseDTO(Long idx, String title, String username, String content, LocalDateTime postdate,
                              Integer viewCount)
    {
        this.idx = idx;
        this.title = title;
        this.username = username;
        this.content = content;
        this.postdate = postdate;
        this.viewCount = viewCount;
    }

    public InquiryResponseDTO(Long idx, String title, String content, LocalDateTime postdate, Integer viewCount) {
    }
}
