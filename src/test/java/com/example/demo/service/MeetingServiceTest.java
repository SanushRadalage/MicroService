package com.example.demo.service;

import com.example.demo.domain.entity.Meeting;
import com.example.demo.exception.SwivelMeetServiceException;
import com.example.demo.repository.MeetingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * This class tests the {@link MeetingService} class.
 */
@RunWith(MockitoJUnitRunner.class)
class MeetingServiceTest {

    private final Meeting meeting = new Meeting();

    private final String meetingId = "mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d";

    @Mock
    private MeetingRepository meetingRepository;
    @InjectMocks
    private MeetingService meetingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test should pass when meeting object save to database")
    void Should_SaveMeeting_When_NotThrowDataAccessException() {
        when(meetingRepository.save(ArgumentMatchers.any(Meeting.class))).thenReturn(meeting);
        meetingService.createMeeting(meeting);
        verify(meetingRepository).save(meeting);
    }

    @Test
    @DisplayName("Test should pass when throw DataAccessException")
    void Should_NotSaveMeeting_When_ThrowDataAccessException() {
        when(meetingRepository.save(meeting)).thenThrow(mock(DataAccessException.class));
        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.createMeeting(meeting));
        assertEquals("Saving meeting to database was failed", swivelMeetServiceException.getMessage());
    }

    @Test
    @DisplayName("Test should pass when read meeting by id from database")
    void Should_ReturnMeeting_When_NotThrowDataAccessException() {
        Meeting meeting = new Meeting();
        meeting.setMeetingId(meetingId);
        doReturn(Optional.of(meeting)).when(meetingRepository).findById(meetingId);
        Meeting returnMeeting = meetingService.getMeeting(meetingId);
        verify(meetingRepository, atLeast(1)).findById(returnMeeting.getMeetingId());
    }

    @Test
    @DisplayName("Test should pass when the meeting was not found")
    void Should_ThrowMeetingWasNotFound_When_FindMeetingById() {
        doReturn(Optional.empty()).when(meetingRepository).findById(meetingId);

        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.getMeeting("mid-8427ff8f-e0d6-4ad4-81bf-9121720c5f"));
        assertEquals("Invalid meeting Id.", swivelMeetServiceException.getMessage());
    }

    @Test
    @DisplayName("Test should pass when throw DataAccessException")
    void Should_ThrowDataAccessException_When_FindMeetingById() {
        when(meetingRepository.findById(meetingId)).thenThrow(mock(DataAccessException.class));
        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.getMeeting(meetingId));
        assertEquals("Read meeting from database was failed.", swivelMeetServiceException.getMessage());
    }

    @Test
    @DisplayName("Test should pass when return page")
    void Should_ReturnPage_When_NotThrowDataAccessException() {

        Pageable paging = PageRequest.of(0, 10);
        Meeting meeting = new Meeting();
        Page<Meeting> meetingPage = new PageImpl<>(Collections.singletonList(meeting));

        when(meetingRepository.findAll(paging)).thenReturn(meetingPage);

        Page<Meeting> returnMeetsPage = meetingService.listAllMeetings(0, 10);
        assertEquals(returnMeetsPage.getTotalElements(), 1);
    }

    @Test
    @DisplayName("Test should pass when throw DataAccessException")
    void Should_NotReturnPage_When_ThrowDataAccessException() {

        Pageable paging = PageRequest.of(0, 10);
        when(meetingRepository.findAll(paging)).thenThrow(mock(DataAccessException.class));
        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.listAllMeetings(0, 10));
        assertEquals("Read meetings from database was failed.", swivelMeetServiceException.getMessage());
    }

    @Test
    @DisplayName("Test should pass when meeting object updated")
    void Should_UpdateMeeting_When_NotThrowException() {
        meeting.setMeetingId(meetingId);
        doReturn(Optional.of(meeting)).when(meetingRepository).findById(meetingId);
        meetingService.updateMeeting(meeting);
        verify(meetingRepository).save(meeting);
    }

    @Test
    @DisplayName("Test should pass when throw DataAccessException")
    void Should_ThrowDataAccessException_When_UpdateMeeting() {
        meeting.setMeetingId(meetingId);
        when(meetingRepository.findById(meetingId)).thenThrow(mock(DataAccessException.class));
        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.updateMeeting(meeting));
        assertEquals("Read/Write meetings from database was failed.", swivelMeetServiceException.getMessage());
    }


    @Test
    @DisplayName("Test should pass when the meeting was not found")
    void Should_ThrowMeetingWasNotFound_When_UpdateMeeting() {
        meeting.setMeetingId("mid-123abc");
        doReturn(Optional.empty()).when(meetingRepository).findById("mid-123abc");

        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.updateMeeting(meeting));
        assertEquals("Invalid meeting Id.", swivelMeetServiceException.getMessage());
    }


    @Test
    @DisplayName("Test should pass when meeting object deleted")
    void Should_DeleteMeeting_When_NotThrowException() {
        doReturn(Optional.of(true)).when(meetingRepository).findById(meetingId);
        meetingService.deleteMeeting(meetingId);
        verify(meetingRepository, atLeast(1)).deleteById(meetingId);
    }


    @Test
    @DisplayName("Test should pass when the meeting was not found")
    void Should_ThrowMeetingWasNotFound_When_DeleteMeeting() {
        meeting.setMeetingId("mid-123abc");
        doReturn(Optional.empty()).when(meetingRepository).findById("mid-123abc");

        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.deleteMeeting("mid-123abc"));
        assertEquals("Invalid meeting Id.", swivelMeetServiceException.getMessage());
    }

    @Test
    @DisplayName("Test should pass when throw DataAccessException")
    void Should_ThrowDataAccessException_When_DeleteMeeting() {
        when(meetingRepository.findById(meetingId)).thenThrow(mock(DataAccessException.class));
        SwivelMeetServiceException swivelMeetServiceException = assertThrows(
                SwivelMeetServiceException.class, () -> meetingService.deleteMeeting(meetingId));
        assertEquals("Read/Delete meeting from database was failed.", swivelMeetServiceException.getMessage());
    }

}