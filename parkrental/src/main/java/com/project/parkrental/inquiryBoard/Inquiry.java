package com.project.parkrental.inquiryBoard;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Builder
@Entity
@Table(name = "inquiry")
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false)
    private Long inquiryReRef; // 참조하는 게시글 ID (루트 게시글)
    @Column(nullable = false)
    private Integer inquiryReLev; // 답글의 레벨 (몇 단계의 답글인지)
    @Column(nullable = false)
    private Integer inquiryReSeq; // 답글 순서
    @Column(nullable = false)
    private String username;
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
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inquiry> replies = new ArrayList<>(); // 자식 게시글 (답글들)
    @ManyToOne
    @JoinColumn(name="parentId")
    private Inquiry parent;

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
