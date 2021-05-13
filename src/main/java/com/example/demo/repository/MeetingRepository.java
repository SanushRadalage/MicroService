package com.example.demo.repository;

import com.example.demo.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Meeting repository
 */
public interface MeetingRepository extends JpaRepository<Meeting, String> {

    @Query(value = "SELECT * from meeting m where m.is_deleted = 0 #pageable",  nativeQuery = true)
    Page<Meeting> findAllMeetings(Pageable pageable);

}
