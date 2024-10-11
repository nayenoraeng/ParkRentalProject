package com.project.parkrental.noticeBoard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // 공지사항 목록 페이지
    @GetMapping("/guest/noticeList")
    public String listAnnouncements(Model model) {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        return "guest/noticeList";
    }

    // 공지사항 상세 페이지
    @GetMapping("/user/noticeDetail/{id}")
    public String viewAnnouncement(@PathVariable("id") Long id, Model model) {
        Announcement announcement = announcementService.getAnnouncementById(id).orElse(null);
        if (announcement != null) {
            // Optionally, increment the view count
            announcement.setViewCount(announcement.getViewCount() + 1);
            announcementService.save(announcement);
        }
        model.addAttribute("announcement", announcement);
        return "user/noticeDetail";  // Make sure this file exists in the user folder
    }

    // 공지사항 작성 페이지 (user/noticeWrite 경로)
    @GetMapping("/admin/noticeWrite") // URL 매핑을 noticeWrite로 변경
    public String showCreateForm(Model model) {
        model.addAttribute("announcement", new Announcement());
        return "admin/noticeWrite"; // user 폴더 내 뷰
    }

    @PostMapping("/guest/noticeList")
    public String createAnnouncement(@ModelAttribute Announcement announcement,
                                     @RequestParam("inquiry_ofile") MultipartFile file) throws IOException {

        // 파일 업로드 처리
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename(); // 원본 파일명
            String fileExtension = getFileExtension(originalFileName); // 파일 확장자 추출

            // 허용된 확장자 목록
            List<String> allowedExtensions = Arrays.asList("pdf", "zip", "rar", "ppt", "pptx");

            // 파일 확장자가 허용된 형식인지 확인
            if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                // 허용되지 않는 파일 형식일 경우 예외 처리
                throw new IOException("허용되지 않는 파일 형식입니다. PDF, ZIP, RAR, PPT 파일만 업로드 가능합니다.");
            }

            String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName; // 저장될 파일명
            String uploadPath = System.getProperty("user.home") + "/uploads/"; // 홈 디렉토리의 uploads 폴더

            File saveFile = new File(uploadPath, storedFileName);
            file.transferTo(saveFile); // 파일 저장

            // 파일 정보 저장
            announcement.setOfile(originalFileName); // 원본 파일명 저장
            announcement.setSfile(storedFileName);   // 서버에 저장된 파일명 저장
        }

        announcement.setUsername("admin"); // 작성자 설정
        announcement.setPostdate(LocalDateTime.now()); // Set current date and time
        announcementService.createAnnouncement(announcement); // 공지사항 저장
        return "redirect:/guest/noticeList"; // 목록 페이지로 리다이렉트
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        }
        return "";
    }


    // 공지사항 수정 페이지
    @GetMapping("/admin/noticeEdit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Announcement announcement = announcementService.getAnnouncementById(id).orElse(null);
        model.addAttribute("announcement", announcement);
        return "admin/noticeEdit"; // 수정된 경로 및 파일명
    }


    // 공지사항 수정 처리
    @PostMapping("/notice/update/{idx}")
    public String updateAnnouncement(@PathVariable("idx") Long idx, @ModelAttribute Announcement updatedAnnouncement) {
        System.out.println("Updating announcement with IDX: " + idx); // 로그 추가
        announcementService.updateAnnouncement(idx, updatedAnnouncement);
        return "redirect:/guest/noticeList"; // 목록으로 리다이렉트
    }
    // 공지사항 삭제 처리
    @GetMapping("/notice/delete/{id}")
    public String deleteAnnouncement(@PathVariable("id") Long id) {
        announcementService.deleteAnnouncement(id);
        return "redirect:/guest/noticeList";
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            // Set the upload path
            String uploadPath = System.getProperty("user.home") + "/uploads/";
            Path filePath = Paths.get(uploadPath).resolve(filename).normalize();

            // Create PathResource instead of UrlResource
            Resource resource = new PathResource(filePath);

            // Check if the file exists
            if (!resource.exists()) {
                throw new IOException("파일을 찾을 수 없습니다: " + filename);
            }

            // Determine the content type based on the file extension
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default to binary stream
            }

            // Encode the filename for the Content-Disposition header
            String encodedFilename = URLEncoder.encode(resource.getFilename(), "UTF-8").replaceAll("\\+", "%20");

            // Return the file as a response
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType)) // Set content type
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename)
                    .body(resource);

        } catch (IOException e) {
            // Log the exception (optional)
            e.printStackTrace();  // For debugging

            return ResponseEntity.notFound().build();
        }
    }
}