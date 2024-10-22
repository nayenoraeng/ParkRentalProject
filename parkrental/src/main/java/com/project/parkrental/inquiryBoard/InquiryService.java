package com.project.parkrental.inquiryBoard;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


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
                inquiry -> new InquiryResponseDTO(
                        inquiry.getIdx(),          // 필요한 필드로 변경
                        inquiry.getTitle(),        // 제목 필드 예시
                        inquiry.getUsername(),
                        inquiry.getContent(),
                        inquiry.getPostdate(),
                        inquiry.getViewCount()// 생성일 필드 예시
                )
        );

        return inquiryResponseDTOs;
    }

    //제목검색
    public Page<InquiryResponseDTO> searchInquiriesByTitle(String title, Pageable pageable) {
        Page<Inquiry> inquiries = inquiryRepository.findByTitleContaining(title, pageable);
        return inquiries.map(inquiry -> new InquiryResponseDTO(
                inquiry.getIdx(),
                inquiry.getTitle(),
                inquiry.getUsername(),
                inquiry.getContent(),
                inquiry.getPostdate(),
                inquiry.getViewCount()
        ));
    }

    //내용검색
    public Page<InquiryResponseDTO> searchInquiriesByContent(String content, Pageable pageable) {
        Page<Inquiry> inquiries = inquiryRepository.findByContentContaining(content, pageable);
        return inquiries.map(inquiry -> new InquiryResponseDTO(
                inquiry.getIdx(),
                inquiry.getTitle(),
                inquiry.getUsername(),
                inquiry.getContent(),
                inquiry.getPostdate(),
                inquiry.getViewCount()
        ));
    }

    //아이디검색
    public Page<InquiryResponseDTO> searchInquiriesByUsername(String username, Pageable pageable) {
        Page<Inquiry> inquiries = inquiryRepository.findByUsernameContaining(username, pageable);
        return inquiries.map(inquiry -> new InquiryResponseDTO(
                inquiry.getIdx(),
                inquiry.getTitle(),
                inquiry.getUsername(),
                inquiry.getContent(),
                inquiry.getPostdate(),
                inquiry.getViewCount()
        ));
    }


    //상세 보기
    public Inquiry inquiryView(Long idx){
        return inquiryRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + idx));
    }

    //글삭제
    public void inquiryDelete(Long idx){
        inquiryRepository.deleteById(idx);
    }

    //조회수 증가
    @Transactional
    public void inquiryUpdateViewCount(Long idx){
        inquiryRepository.findById(idx).ifPresent(inquiry -> {
            inquiry.incrementViewCount();
            inquiryRepository.save(inquiry);
        });
    }

    //글쓰기
    @Transactional
    public void inquiryWrite(InquiryRequestDTO inquiryCreate, HttpServletRequest request)
            throws IOException, ServletException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인한 사용자 이름


        Inquiry inquiry = Inquiry.builder()
                .groupOrd(inquiryCreate.getGroupOrd() != null ? inquiryCreate.getGroupOrd() : 0)
                .groupLayer(inquiryCreate.getGroupLayer() != null ? inquiryCreate.getGroupLayer() : 0)
                .username(username)
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

    //수정하기
    @Transactional
    public void inquiryUpdate(Long idx, InquiryRequestDTO inquiryRequest, MultipartFile file) throws IOException {
        Inquiry inquiry = inquiryRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + idx));

        // 파일 처리
        handleFileUpload(file, inquiryRequest);

        // 게시글 업데이트
        Inquiry updatedInquiry = Inquiry.builder()
                .idx(inquiry.getIdx())
                .originNo(inquiryRequest.getOriginNo())
                .groupOrd(inquiryRequest.getGroupOrd() != null ? inquiryRequest.getGroupOrd() : 0)
                .groupLayer(inquiryRequest.getGroupLayer() != null ? inquiryRequest.getGroupLayer() : 0)
                .username(inquiry.getUsername())
                .title(inquiryRequest.getTitle())
                .content(inquiryRequest.getContent())
                .postdate(inquiry.getPostdate())
                .viewCount(inquiry.getViewCount()) // 기존 값 유지
                .responses(inquiry.getResponses())
                .ofile(inquiryRequest.getOfile())
                .sfile(inquiryRequest.getSfile())
                .inquiryPassword(inquiryRequest.getInquiryPassword())
                .build();

        inquiryRepository.save(updatedInquiry);
    }

    //파일 업로드
    private void handleFileUpload(MultipartFile file, InquiryRequestDTO inquiryRequest) throws IOException {
        String originalOfile = inquiryRequest.getOfile();
        String originalSfile = inquiryRequest.getSfile();

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
    }

    //이미지 파일 확인하기
    public boolean isImageFile(String fileName) {

        if(fileName == null){
            return true;
        }
        // 파일 확장자를 대소문자 구분 없이 체크
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") ||
                lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".png") ||
                lowerCaseFileName.endsWith(".gif") ||
                lowerCaseFileName.endsWith(".bmp") ||
                lowerCaseFileName.endsWith(".webp");
    }

    // 그룹 순서 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Inquiry i SET i.groupOrd = i.groupOrd + 1 WHERE i.originNo = :originNo AND i.groupOrd > :groupOrd")
    public void updateGroupOrd(@Param("originNo") Long originNo, @Param("groupOrd") Integer groupOrd) {
        inquiryRepository.updateGroupOrd(originNo, groupOrd);
    }

    //답글쓰기
    @Transactional
    public void inquiryReplyWrite(InquiryRequestDTO inquiryReply, HttpServletRequest request, MultipartFile file)
            throws IOException, ServletException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인한 사용자 이름

        // 원글의 정보를 조회
        Inquiry parentInquiry = getInquiryById(inquiryReply.getIdx());
        if (parentInquiry == null) {
            throw new RuntimeException("Parent Inquiry not found"); // 예외 처리
        }

        // 파일 업로드 처리
        handleFileUpload(file, inquiryReply);

        // 새로운 문의 객체 생성
        Inquiry inquiry = Inquiry.builder()
                .originNo(parentInquiry.getIdx()) // originNo는 원글의 idx
                .groupOrd(parentInquiry.getGroupOrd() + 1) // groupOrd는 원글의 groupOrd + 1
                .groupLayer(parentInquiry.getGroupLayer() + 1) // groupLayer는 원글의 groupLayer + 1
                .username(inquiryReply.getUsername()) // 추가된 사용자 이름
                .title(inquiryReply.getTitle())
                .content(inquiryReply.getContent())
                .postdate(inquiryReply.getPostdate()) // 현재 시간으로 설정
                .viewCount(0) // 기본값 처리
                .responses(0) // 기본값 처리
                .ofile(inquiryReply.getOfile())
                .sfile(inquiryReply.getSfile())
                .inquiryPassword(inquiryReply.getInquiryPassword())
                .build();

        System.out.println("Saving Inquiry: " + inquiry);


        inquiryRepository.save(inquiry);

        updateParentResponses(parentInquiry.getIdx());
    }

    // 부모 글의 responses 필드를 1로 업데이트하는 메서드
    @Transactional
    public void updateParentResponses(Long parentIdx) {
        Inquiry parentInquiry = inquiryRepository.findById(parentIdx)
                .orElseThrow(() -> new RuntimeException("Parent Inquiry not found"));

        parentInquiry.setResponses(1); // responses 필드를 1로 설정

        inquiryRepository.save(parentInquiry); // 부모 글 업데이트
    }
    // 원글 조회 메서드 추가
    public Inquiry getInquiryById(Long idx) {
        return inquiryRepository.findById(idx)
                .orElse(null); // 원글이 존재하지 않으면 null 반환
    }


}
