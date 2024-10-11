package com.project.parkrental.noticeBoard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // 필요한 추가 쿼리 메서드를 정의할 수 있음
}
