package com.project.parkrental.inquiryBoard;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Optional<Inquiry> findById(Long idx);

    @Modifying
    @Transactional
    @Query("UPDATE Inquiry i SET i.groupOrd = i.groupOrd + 1 WHERE i.originNo = :originNo AND i.groupOrd > :groupOrd")
    void updateGroupOrd(@Param("originNo") Long originNo, @Param("groupOrd") Integer groupOrd);

    // 제목이나 내용으로 검색
    List<Inquiry> findByTitleContainingOrContentContainingOrUsernameContaining(
            String title, String content, String username, Pageable pageable);
    

}
