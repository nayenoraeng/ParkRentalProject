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
    private Long inquiryReRef;
    private Integer inquiryReLev;
    private Integer inquiryReSeq;
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
    private List<ReplyResponseDTO> replies = new ArrayList<>();
    private Inquiry parent;

    public InquiryResponseDTO(Long idx, String title, String content, LocalDateTime postdate,
                              Integer viewCount, List<ReplyResponseDTO> replies)
    {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.postdate = postdate;
        this.viewCount = viewCount;
        this.replies = replies;
    }
}
