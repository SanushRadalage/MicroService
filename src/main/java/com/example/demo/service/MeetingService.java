package com.example.demo.service;

import com.example.demo.domain.entity.Meeting;
import com.example.demo.exception.InvalidMeetingException;
import com.example.demo.exception.SwivelMeetServiceException;
import com.example.demo.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Meeting service handle CRUD
 */
@Service
public class MeetingService {

    MeetingRepository meetingRepository;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    /**
     * Create meeting
     *
     * @param meeting
     */
    public Meeting createMeeting(Meeting meeting) {
        try {
            meetingRepository.save(meeting);
            return meeting;
        } catch (DataAccessException e) {
            throw new SwivelMeetServiceException("Saving meeting to database was failed", e);
        }
    }

    /**
     * Get meeting by meetingId
     *
     * @param meetingId
     */
    public Meeting getMeeting(String meetingId) {
        try {
            if (meetingRepository.findById(meetingId).isPresent()) {
                if (!meetingRepository.findById(meetingId).get().isDeleted()) {
                    return meetingRepository.findById(meetingId).get();
                } else {
                    throw new InvalidMeetingException("Invalid meeting Id.");
                }
            } else {
                throw new InvalidMeetingException("Invalid meeting Id.");
            }
        } catch (DataAccessException e) {
            throw new SwivelMeetServiceException("Read meeting from database was failed.", e);
        }
    }

    /**
     * Get meeting list page wise
     *
     * @param page current page
     * @param size size per page
     * @return
     */
    public Page<Meeting> listAllMeetings(int page, int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            return meetingRepository.findAll(paging);

        } catch (DataAccessException e) {
            throw new SwivelMeetServiceException("Read meetings from database was failed.", e);
        }
    }

    /**
     * Update meeting
     */
    public Meeting updateMeeting(Meeting meeting) {
        try {
            Optional<Meeting> optionalMeetingFromDb = meetingRepository.findById(meeting.getMeetingId());

            if (optionalMeetingFromDb.isPresent()) {
                Meeting meetingFromDb = optionalMeetingFromDb.get();
                long createdAt = meetingFromDb.getCreatedAt();
                meeting.setCreatedAt(createdAt);
                return meetingRepository.save(meeting);
            } else
                throw new InvalidMeetingException("Invalid meeting Id.");

        } catch (DataAccessException e) {
            throw new SwivelMeetServiceException("Read/Write meetings from database was failed.", e);
        }
    }

    public void deleteMeeting(String meetingId) {
        try {
            if (meetingRepository.findById(meetingId).isPresent())
                meetingRepository.deleteById(meetingId);
            else
                throw new InvalidMeetingException("Invalid meeting Id.");
        } catch (DataAccessException e) {
            throw new SwivelMeetServiceException("Read/Delete meeting from database was failed.", e);
        }
    }
}

