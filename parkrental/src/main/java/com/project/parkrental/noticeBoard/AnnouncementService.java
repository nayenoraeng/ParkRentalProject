package com.project.parkrental.noticeBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {


    @Autowired
    private AnnouncementRepository announcementRepository;


    // 공지사항 전체 조회
    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        announcements.forEach(a -> System.out.println("Announcement: " + a.getTitle() + ", Postdate: " + a.getPostdate()));
        return announcements;
    }

    // ID로 공지사항 조회
    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }


    // 공지사항 생성
    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    // 공지사항 수정
    public Announcement updateAnnouncement(Long idx, Announcement updatedAnnouncement) {
        Optional<Announcement> existingAnnouncement = announcementRepository.findById(idx);
        if (existingAnnouncement.isPresent()) {
            Announcement announcement = existingAnnouncement.get();
            announcement.setTitle(updatedAnnouncement.getTitle());
            announcement.setContent(updatedAnnouncement.getContent());
            announcement.setOfile(updatedAnnouncement.getOfile());
            announcement.setSfile(updatedAnnouncement.getSfile());
            System.out.println("Updating announcement with idx: " + idx); // 추가된 로그
            return announcementRepository.save(announcement); // 단일 save 호출
        } else {
            System.out.println("Announcement not found with idx: " + idx); // 추가된 로그
            return null; // 존재하지 않는 경우 null 반환
        }
    }

    // 공지사항 삭제
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    // 공지사항 저장
    public void save(Announcement announcement) {
        announcementRepository.save(announcement);
    }

}
