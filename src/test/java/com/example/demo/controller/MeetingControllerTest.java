package com.example.demo.controller;

import com.example.demo.domain.entity.Meeting;
import com.example.demo.domain.request.MeetingRequestDto;
import com.example.demo.domain.request.UpdateMeetingRequestDto;
import com.example.demo.exception.InvalidMeetingException;
import com.example.demo.exception.SwivelMeetServiceException;
import com.example.demo.service.MeetingService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class tests the {@link MeetingController} class.
 */
class MeetingControllerTest {

    private final String meetingId = "mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d";
    List<String> idList = new ArrayList<>();
    MeetingRequestDto meetingRequestDto = createMeetingRequestDto();
    UpdateMeetingRequestDto updateMeetingRequestDto = updateMeetingRequestDto();
    private MockMvc mvc;
    @Mock
    private MeetingService meetingService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        MeetingController meetingController = new MeetingController(meetingService, 5, userService);
        mvc = MockMvcBuilders.standaloneSetup(meetingController).build();
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test should pass when meeting is created successfully")
    void Should_ReturnOk_When_CreatingMeetingIsSuccessful() throws Exception {

        Long millis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(meetingRequestDto.getDateTime().toString()).getTime();

        mvc.perform(post("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Meeting created successfully.")))
                .andExpect(jsonPath("$.data.meetingId", startsWith("mid-")))
                .andExpect(jsonPath("$.data.name", is(meetingRequestDto.getName())))
                .andExpect(jsonPath("$.data.hours", is(meetingRequestDto.getHours())))
                .andExpect(jsonPath("$.data.minutes", is(meetingRequestDto.getMinutes())))
                .andExpect(jsonPath("$.data.dateTime", is(millis)))
                .andExpect(jsonPath("$.data.inviteUserIds", is(meetingRequestDto.getInviteUserIds())))
                .andExpect(jsonPath("$.data.videoOnAtJoin", is(meetingRequestDto.isVideoOnAtJoin())))
                .andExpect(jsonPath("$.data.recordStatus", is(meetingRequestDto.isRecordStatus())))
                .andExpect(jsonPath("$.data.micOnAtJoin", is(meetingRequestDto.isMicOnAtJoin())))
                .andExpect(jsonPath("$.displayMessage", is("Meeting was created successfully.")));
    }

    @Test
    @DisplayName("Test should pass when time zone is invalid")
    void Should_ReturnBadRequest_When_InvalidTimeZoneInCreateMeeting() throws Exception {
        mvc.perform(post("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Europe/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid time zone."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid time zone."));
    }

    @Test
    @DisplayName("Test should pass when time zone is invalid")
    void Should_ReturnBadRequest_When_InvalidTimeZoneInGetMeeting() throws Exception {
        mvc.perform(get("/api/v1/meet/mid-8427ff8f-e0d6-4ad4-81bf-9121720c5f92")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Europe/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid time zone."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid time zone."));
    }

    @Test
    @DisplayName("Test should pass when time zone is invalid")
    void Should_ReturnBadRequest_When_InvalidTimeZoneInGetMeetingList() throws Exception {
        mvc.perform(get("/api/v1/meet/0/10")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Europe/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid time zone."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid time zone."));
    }

    @Test
    @DisplayName("Test should pass when required field missing")
    void Should_ReturnBadRequest_When_NameIsEmpty() throws Exception {

        meetingRequestDto.setName("");

        mvc.perform(post("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Required fields are missing."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Required fields are missing."));
    }


    @Test
    @DisplayName("Test should pass when duration of the meeting is invalid")
    void Should_ReturnBadRequest_When_DurationIsInValid() throws Exception {

        meetingRequestDto.setHours(90);
        meetingRequestDto.setMinutes(200);

        mvc.perform(post("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid time duration."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid time duration."));
    }

    @Test
    @DisplayName("Test should pass when schedule date and time is before or equals to the current date and time")
    void Should_ReturnBadRequest_When_DateTimeIsInvalid() throws Exception {

        meetingRequestDto.setDateTime(Timestamp.valueOf("2021-04-06 09:01:10"));

        mvc.perform(post("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid dateTime."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid date or time."));
    }

    @Test
    @DisplayName("Test should pass when invitees limit is exceeding 5")
    void Should_ReturnBadRequest_When_InviteesLimitIsInvalid() throws Exception {

        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");

        meetingRequestDto.setInviteUserIds(idList);

        mvc.perform(post("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invitees limit exceeded."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Maximum invitees limit exceeded."));
    }

    @Test
    @DisplayName("Test should pass when create meeting cause an internal server error")
    void Should_ReturnInternalServerError_When_CreateMeeting() throws Exception {

        when(meetingService.createMeeting(any(Meeting.class))).thenThrow(mock(SwivelMeetServiceException.class));

        mvc.perform(post("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Internal server error."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Oops! something went wrong."));
    }

    @Test
    @DisplayName("Test should pass when update meeting cause an internal server error")
    void Should_ReturnInternalServerError_When_UpdateMeeting() throws Exception {

        doThrow(new SwivelMeetServiceException("Update meeting from database was failed.")).when(meetingService).updateMeeting(any(Meeting.class));

        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(updateMeetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Internal server error."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Oops! something went wrong."));
    }

    @Test
    @DisplayName("Test should pass when meeting is return successfully")
    void Should_ReturnOk_When_ReadMeetingById() throws Exception {

        Meeting meeting = new Meeting(meetingRequestDto);
        meeting.setMeetingId(meetingId);
        doReturn(meeting).when(meetingService).getMeeting(meetingId);

        mvc.perform(get("/api/v1/meet/mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.meetingId").value(meeting.getMeetingId()))
                .andExpect(jsonPath("$.message").value("Return the meeting successfully."))
                .andExpect(jsonPath("$.displayMessage").value("Return the meeting successfully."));
    }

    @Test
    @DisplayName("Test should pass when throw data access exception")
    void Should_ReturnInternalServerError_When_ReadMeetingById() throws Exception {

        doThrow(new SwivelMeetServiceException("Read meeting from database was failed.")).when(meetingService).getMeeting(meetingId);

        mvc.perform(get("/api/v1/meet/mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Internal server error."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Oops! something went wrong."));
    }

    @Test
    @DisplayName("Test should pass when meeting was not found")
    void Should_ReturnBadRequest_When_ReadMeetingById() throws Exception {

        doThrow(new InvalidMeetingException("Invalid meeting id.")).when(meetingService).getMeeting(meetingId);

        mvc.perform(get("/api/v1/meet/mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid meeting."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid meeting."));
    }

    @Test
    @DisplayName("Test should pass when meeting was not found")
    void Should_ReturnBadRequest_When_UpdateMeeting() throws Exception {

        doThrow(new InvalidMeetingException("Invalid meeting id.")).when(meetingService).updateMeeting(any(Meeting.class));

        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(updateMeetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid meeting."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid meeting."));
    }

    @Test
    @DisplayName("Test should pass when return meeting list")
    void Should_ReturnOk_When_NotThrowExceptions() throws Exception {

        Pageable paging = PageRequest.of(0, 10);
        Meeting meeting = new Meeting();
        Page<Meeting> meetingPage = new PageImpl<>(Collections.singletonList(meeting));

        doReturn(meetingPage).when(meetingService).listAllMeetings(paging.getPageNumber(), paging.getPageSize());

        mvc.perform(get("/api/v1/meet/0/10")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Return the meeting summary successfully."))
                .andExpect(jsonPath("$.displayMessage").value("Return the meeting summary successfully."));
    }

    @Test
    @DisplayName("Test should pass when throw data access exception")
    void Should_ReturnInternalServerError_When_ReadAllMeetings() throws Exception {

        Pageable paging = PageRequest.of(0, 10);

        doThrow(new SwivelMeetServiceException("Read meeting from database was failed.")).when(meetingService).listAllMeetings(paging.getPageNumber(), paging.getPageSize());

        mvc.perform(get("/api/v1/meet/0/10")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Internal server error."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Oops! something went wrong."));
    }

    @Test
    @DisplayName("Test should pass when throw data access exception")
    void Should_Ok_When_DeleteMeeting() throws Exception {

        doNothing().when(meetingService).deleteMeeting(meetingId);

        mvc.perform(put("/api/v1/meet/mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Meeting was deleted successfully."))
                .andExpect(jsonPath("$.displayMessage").value("Meeting was deleted successfully."));
    }
    @Test
    @DisplayName("Test should pass when throw data access exception")
    void Should_ReturnInternalServerError_When_ThrowDataAccessExceptions() throws Exception {

        doThrow(new SwivelMeetServiceException("Delete meeting from database was failed.")).when(meetingService).deleteMeeting(meetingId);

        mvc.perform(put("/api/v1/meet/mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Internal server error."))
                .andExpect(jsonPath("$.displayMessage").value("Oops! something went wrong."));
    }

    @Test
    @DisplayName("Test should pass when meeting was not found")
    void Should_ReturnBadRequest_When_DeleteMeeting() throws Exception {

        doThrow(new InvalidMeetingException("Invalid meeting id.")).when(meetingService).deleteMeeting("mid-8427ff8f-e0d6-4ad4-81bf-9121720c5f92");

        mvc.perform(put("/api/v1/meet/mid-8427ff8f-e0d6-4ad4-81bf-9121720c5f92")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid meeting."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid meeting."));
    }

    @Test
    @DisplayName("Test should pass when time zone is invalid")
    void Should_ReturnBadRequest_When_InvalidTimeZoneInDeleteMeeting() throws Exception {
        mvc.perform(put("/api/v1/meet/mid-c2cfbb1d-fa52-477c-b64f-c979c4657e9d")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Europe/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid time zone."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid time zone."));
    }


    @Test
    @DisplayName("Test should pass when meeting is updated successfully")
    void Should_ReturnOk_When_UpdateMeetingIsSuccessful() throws Exception {

        when(meetingService.updateMeeting(any(Meeting.class))).thenReturn(new Meeting(updateMeetingRequestDto));

        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(updateMeetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Meeting was updated successfully.")))
                .andExpect(jsonPath("$.data.meetingId", startsWith("mid-")))
                .andExpect(jsonPath("$.displayMessage", is("Meeting was updated successfully.")));
    }

    @Test
    @DisplayName("Test should pass when time zone is invalid")
    void Should_ReturnBadRequest_When_InvalidTimeZoneInUpdateMeeting() throws Exception {
        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Europe/Colombo"))
                .content(meetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid time zone."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid time zone."));
    }

    @Test
    @DisplayName("Test should pass when required field missing")
    void Should_ReturnBadRequest_When_NameIsEmptyInUpdateMeeting() throws Exception {

        updateMeetingRequestDto.setName("");

        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(updateMeetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Required fields are missing."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Required fields are missing."));
    }

    @Test
    @DisplayName("Test should pass when duration of the meeting is invalid")
    void Should_ReturnBadRequest_When_DurationIsInValidInUpdateMeeting() throws Exception {

        updateMeetingRequestDto.setHours(90);
        updateMeetingRequestDto.setMinutes(200);

        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(updateMeetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid time duration."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid time duration."));
    }

    @Test
    @DisplayName("Test should pass when schedule date and time is before or equals to the current date and time")
    void Should_ReturnBadRequest_When_DateTimeIsInvalidInUpdateMeeting() throws Exception {

        updateMeetingRequestDto.setDateTime(Timestamp.valueOf("2021-04-06 09:01:10"));

        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(updateMeetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid dateTime."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Invalid date or time."));
    }


    @Test
    @DisplayName("Test should pass when invitees limit is exceeding 5")
    void Should_ReturnBadRequest_When_InviteesLimitIsInvalidInUpdateMeeting() throws Exception {

        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");

        updateMeetingRequestDto.setInviteUserIds(idList);

        mvc.perform(put("/api/v1/meet")
                .headers(setUpHeader("uid-2997d49d-6ca1-409f-84b1-8f25934cf220", "Asia/Colombo"))
                .content(updateMeetingRequestDto.toJson())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Invitees limit exceeded."))
                .andExpect(jsonPath("$.data", nullValue()))
                .andExpect(jsonPath("$.displayMessage").value("Maximum invitees limit exceeded."));
    }


    private UpdateMeetingRequestDto updateMeetingRequestDto() {

        UpdateMeetingRequestDto meetingRequestDto = new UpdateMeetingRequestDto();

        meetingRequestDto.setMeetingId(meetingId);
        meetingRequestDto.setName("Test Meeting");
        meetingRequestDto.setCreateUserId("uid-2997d49d-6ca1-409f-84b1-8f25934cf220");
        meetingRequestDto.setHours(5);
        meetingRequestDto.setMinutes(10);
        meetingRequestDto.setDateTime(Timestamp.valueOf("2021-07-06 09:01:10"));

        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");
        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");

        meetingRequestDto.setInviteUserIds(idList);

        meetingRequestDto.setMicOnAtJoin(false);
        meetingRequestDto.setVideoOnAtJoin(true);
        meetingRequestDto.setRecordStatus(false);

        return meetingRequestDto;
    }

    private MeetingRequestDto createMeetingRequestDto() {

        MeetingRequestDto meetingRequestDto = new MeetingRequestDto();

        meetingRequestDto.setName("Test Meeting");
        meetingRequestDto.setCreateUserId("uid-2997d49d-6ca1-409f-84b1-8f25934cf220");
        meetingRequestDto.setHours(5);
        meetingRequestDto.setMinutes(10);
        meetingRequestDto.setDateTime(Timestamp.valueOf("2021-07-06 09:01:10"));

        idList.add("uid-49c545af-16d5-4ea7-a617-e20ec54c59c0");

        meetingRequestDto.setInviteUserIds(idList);

        meetingRequestDto.setMicOnAtJoin(false);
        meetingRequestDto.setVideoOnAtJoin(true);
        meetingRequestDto.setRecordStatus(false);

        return meetingRequestDto;
    }

    private HttpHeaders setUpHeader(String userId, String timeStamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-UserId", userId);
        headers.add("X-TimeZone", timeStamp);
        return headers;
    }
}