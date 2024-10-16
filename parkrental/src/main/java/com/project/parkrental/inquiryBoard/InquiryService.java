package com.project.parkrental.inquiryBoard;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.checkerframework.checker.nullness.Opt.orElseThrow;

@Service
public class InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    //전체 리스트 띄우기 (페이징)
    public Page<InquiryResponseDTO> findAll(Pageable pageable) {
        // Pageable을 사용하여 페이지 요청 처리
        Page<Inquiry> inquiryPage = inquiryRepository.findAll(pageable);

        // Inquiry 객체를 InquiryResponseDTO로 변환
        Page<InquiryResponseDTO> inquiryResponseDTOs = inquiryPage.map(
                inquiry -> {
                    List<ReplyResponseDTO> replies = inquiry.getReplies().stream()
                            .map(reply -> new ReplyResponseDTO(
                                    reply.getIdx(),          // 답글 ID
                                    reply.getUsername(),     // 작성자
                                    reply.getContent(),      // 내용
                                    reply.getPostdate()      // 작성일
                            ))
                            .collect(Collectors.toList());

                    return new InquiryResponseDTO(
                            inquiry.getIdx(),
                            inquiry.getTitle(),
                            inquiry.getContent(),
                            inquiry.getPostdate(),
                            inquiry.getViewCount(),
                            replies // 답글 목록 추가
                    );
                }
        );

        return inquiryResponseDTOs;
    }

    //상세 보기
    public Inquiry inquiryView(Long idx){
        return inquiryRepository.findById(idx).get();
    }

    //글삭제
    public void inquiryDelete(Long idx){
        inquiryRepository.deleteById(idx);
    }

    //글쓰기
    @Transactional
    public void inquiryWrite(InquiryRequestDTO inquiryCreate, HttpServletRequest request)
            throws IOException, ServletException
    {

        Inquiry inquiry = Inquiry.builder()
                .inquiryReRef(inquiryCreate.getInquiryReRef())
                .inquiryReLev(inquiryCreate.getInquiryReLev() != null ? inquiryCreate.getViewCount() : 0)
                .inquiryReSeq(inquiryCreate.getInquiryReSeq() != null ? inquiryCreate.getViewCount() : 0)
                .username(inquiryCreate.getUsername())
                .title(inquiryCreate.getTitle())
                .content(inquiryCreate.getContent())
                .postdate(inquiryCreate.getPostdate())
                .viewCount(inquiryCreate.getViewCount() != null ? inquiryCreate.getViewCount() : 0) // 기본값 처리
                .responses(inquiryCreate.getResponses() != null ? inquiryCreate.getResponses() : 0) // 기본값 처리
                .ofile(inquiryCreate.getOfile())
                .sfile(inquiryCreate.getSfile())
                .inquiryPassword(inquiryCreate.getInquiryPassword())
                .build();

        System.out.println("Saving Inquiry: " + inquiry);

        inquiryRepository.save(inquiry);
    }

    //글 수정
    @Transactional
    public void inquiryUpdate(Long idx, InquiryRequestDTO inquiryRequest, MultipartFile file,
                                            HttpServletRequest request)  throws IOException, ServletException{
        Optional<Inquiry> optionalInquiry =  inquiryRepository.findById(idx);

        if (optionalInquiry.isPresent()) {
            Inquiry inquiry = optionalInquiry.get();

            String originalOfile = inquiry.getOfile();
            String originalSfile = inquiry.getSfile();

            if (file != null && !file.isEmpty()) {
                String ofile = file.getOriginalFilename();
                String uploadDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 이미지 저장 경로 지정

                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // mkdirs()를 사용하여 필요한 모든 디렉토리 생성
                }

                String sfile = UUID.randomUUID().toString() + "_" + ofile;

                File destination = new File(dir, sfile);
                file.transferTo(destination); // 파일 저장

                // 파일 이름 업데이트
                inquiryRequest.setOfile(ofile);
                inquiryRequest.setSfile(sfile);
            } else {
                // 파일이 없을 경우 기존 파일 이름 유지
                inquiryRequest.setOfile(originalOfile);
                inquiryRequest.setSfile(originalSfile);
            }

            Inquiry updatePost = Inquiry.builder()
                    .idx(inquiry.getIdx())
                    .username(inquiry.getUsername())
                    .title(inquiryRequest.getTitle())
                    .content(inquiryRequest.getContent())
                    .postdate(inquiry.getPostdate())
                    .viewCount(inquiry.getViewCount()) // 기존 값 유지
                    .responses(inquiry.getResponses())
                    .ofile(inquiryRequest.getOfile())
                    .sfile(inquiryRequest.getSfile())
                    .inquiryPassword(inquiryRequest.getInquiryPassword())
                    .build();// 새로운 파일이 있는 경우 파일 업로드

            // 게시글 업데이트
            inquiryRepository.save(updatePost);
        } else {
            throw new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + idx);
        }
    }

    //답글 쓰기
//    @Transactional
//    public Inquiry inquiryReply (Long parentId, InquiryRequestDTO requestDto){
//        Inquiry parentInquiry = inquiryRepository.findByParentId(parentId);
//
//        Inquiry reply = Inquiry.builder()
//                .username(requestDto.getUsername())
//                .title(requestDto.getTitle())
//                .content(requestDto.getContent())
//                .inquiryReRef(parentInquiry.getInquiryReRef()) // 부모의 참조 ID 설정
//                .inquiryReLev(parentInquiry.getInquiryReLev() + 1) // 부모의 레벨 + 1
//                .inquiryReSeq(parentInquiry.getReplies().size() + 1) // 현재 답글 개수 + 1
//                .ofile(requestDto.getOfile())
//                .sfile(requestDto.getSfile())
//                .parent(parentInquiry) // 부모 게시글 설정
//                .build();
//
//        return inquiryRepository.save(reply);
//    }
}
