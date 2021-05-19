package com.example.demo.repository;

import com.example.demo.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Meeting repository
 */
public interface MeetingRepository extends JpaRepository<Meeting, String> {

    /**
     * This method finds all non deleted meetings
     * @param pageable
     * @return meeting page
     */
    @Query(value = "SELECT * from meeting m where m.is_deleted = false",  nativeQuery = true)
    Page<Meeting> findAllMeetings(Pageable pageable);

    /**
     * This method finds the meeting by id.
     *
     * @param id meeting Id
     * @return meeting
     */
    @Query(value = "SELECT * FROM meeting m WHERE m.meeting_id=?1 AND m.is_deleted=false",
            nativeQuery = true)
    Optional<Meeting> findById(String id);

}
