package com.project.parkrental.inquiryBoard;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .originNo(inquiryCreate.getOriginNo())
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
        // 파일 확장자를 대소문자 구분 없이 체크
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") ||
                lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".png") ||
                lowerCaseFileName.endsWith(".gif") ||
                lowerCaseFileName.endsWith(".bmp") ||
                lowerCaseFileName.endsWith(".webp");
    }

}
