package com.example.demo.repository;

import com.example.demo.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Meeting repository
 */
public interface MeetingRepository extends JpaRepository<Meeting, String> {

//    Page<Meeting> findAll(Pageable pageable);

}
