package com.example.demo.domain.request;

import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the {@link MeetingRequestDto} class.
 */
class MeetingRequestDtoTest {

    private static final MeetingRequestDto meetingRequestDto = new MeetingRequestDto();
    List<String> idList = new ArrayList<>();


    @BeforeEach
    @DisplayName("MeetingDto object creation")
    void setUp() {
        meetingRequestDto.setName("Test Meeting");
        meetingRequestDto.setCreateUserId("uid-2997d49d-6ca1-409f-84b1-8f25934cf220");
        meetingRequestDto.setHours(5);
        meetingRequestDto.setMinutes(10);
        meetingRequestDto.setDateTime(Timestamp.valueOf("2021-06-06 09:01:10"));

        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        meetingRequestDto.setInviteUserIds(idList);

        meetingRequestDto.setMicOnAtJoin(false);
        meetingRequestDto.setVideoOnAtJoin(true);
        meetingRequestDto.setRecordStatus(false);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test should pass when dto contain all required fields correctly")
    void Should_ReturnTrue_When_AllRequiredFieldsAreAvailable() {
        assertTrue(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when name is null")
    void Should_ReturnFalse_When_NameIsNull() {
        meetingRequestDto.setName(null);
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when meeting name is empty")
    void Should_ReturnFalse_When_NameIsEmpty() {
        meetingRequestDto.setName("");
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when user id is null")
    void Should_ReturnFalse_When_UserIdIsNull() {
        meetingRequestDto.setName(null);
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when user id is empty")
    void Should_ReturnFalse_When_UserIdIsEmpty() {
        meetingRequestDto.setName("");
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when dateTime is null")
    void Should_ReturnFalse_When_DateTimeIsNull() {
        meetingRequestDto.setDateTime(null);
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when hours are less than 0")
    void Should_ReturnFalse_When_HoursIsLessThanZero() {
        meetingRequestDto.setHours(-1);
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when minutes are less than 0")
    void Should_ReturnFalse_When_MinutesIsLessThanZero() {
        meetingRequestDto.setMinutes(-1);
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when invitees id list is null")
    void Should_ReturnFalse_When_InviteesIdListIsNull() {
        meetingRequestDto.setInviteUserIds(null);
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when invitees id list is empty")
    void Should_ReturnFalse_When_InviteesIdListIsEmpty() {
        idList.clear();
        meetingRequestDto.setInviteUserIds(idList);
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when dto contain all required fields are not available")
    void Should_ReturnTrue_When_AllRequiredFieldsAreNotAvailable() {
        meetingRequestDto.setName(null);
        meetingRequestDto.setDateTime(null);
        meetingRequestDto.setHours(-1);
        meetingRequestDto.setMinutes(-1);
        idList.clear();
        assertFalse(meetingRequestDto.isRequiredAvailable());
    }

    @Test
    @DisplayName("Test should pass when schedule date and time is after the current date and time")
    void Should_ReturnTrue_When_DateTimeIsValid() {
        Assertions.assertTrue(meetingRequestDto.isValidDateTime());
    }

    @Test
    @DisplayName("Test should pass when schedule date and time is before the current date and time")
    void Should_ReturnFalse_When_DateTimeIsInValid() {
        meetingRequestDto.setDateTime(Timestamp.valueOf("2021-04-06 09:01:10"));
        Assertions.assertFalse(meetingRequestDto.isValidDateTime());
    }

    @Test
    @DisplayName("Test should pass when duration of the meeting is valid")
    void Should_ReturnTrue_When_DurationIsValid() {
        Assertions.assertTrue(meetingRequestDto.isValidMeetingDuration(meetingRequestDto.getHours(), meetingRequestDto.getMinutes()));
    }

    @Test
    @DisplayName("Test should pass when hour duration of the meeting is more than 24")
    void Should_ReturnFalse_When_HourDurationIsInValid() {
        meetingRequestDto.setHours(50);
        Assertions.assertFalse(meetingRequestDto.isValidMeetingDuration(meetingRequestDto.getHours(), meetingRequestDto.getMinutes()));
    }

    @Test
    @DisplayName("Test should pass when minute duration of the meeting is more than 60")
    void Should_ReturnFalse_When_MinuteDurationIsInValid() {
        meetingRequestDto.setMinutes(61);
        Assertions.assertFalse(meetingRequestDto.isValidMeetingDuration(meetingRequestDto.getHours(), meetingRequestDto.getMinutes()));
    }

    @Test
    @DisplayName("Test should pass when hour duration of the meeting is more than 24 and minute duration of the meeting is more than 60")
    void Should_ReturnFalse_When_MinuteAndHourDurationsAreInValid() {
        meetingRequestDto.setHours(50);
        meetingRequestDto.setMinutes(61);
        Assertions.assertFalse(meetingRequestDto.isValidMeetingDuration(meetingRequestDto.getHours(), meetingRequestDto.getMinutes()));
    }

    @Test
    @DisplayName("Test should pass when invitees limit not exceeding 5")
    void Should_ReturnTrue_When_CorrectInviteesLimit() {
        Assertions.assertTrue(meetingRequestDto.isValidInviteeLimit(5));
    }

    @Test
    @DisplayName("Test should pass when invitees limit not exceeding 5")
    void Should_ReturnFalse_When_InviteesLimitIsExceed() {
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        Assertions.assertFalse(meetingRequestDto.isValidInviteeLimit(5));
    }
}