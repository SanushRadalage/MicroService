package com.example.demo.controller;

import com.example.demo.configuration.ResourceBundleMessageSourceBean;
import com.example.demo.configuration.Translator;
import com.example.demo.domain.entity.Meeting;
import com.example.demo.domain.request.MeetingRequestDto;
import com.example.demo.domain.request.UpdateMeetingRequestDto;
import com.example.demo.domain.response.MeetingListResponseDto;
import com.example.demo.domain.response.MeetingResponseDto;
import com.example.demo.domain.response.ResponseDto;
import com.example.demo.exception.InvalidMeetingException;
import com.example.demo.exception.SwivelMeetServiceException;
import com.example.demo.service.MeetingService;
import com.example.demo.utils.ErrorsResponseStatusType;
import com.example.demo.utils.ResponseStatusType;
import com.example.demo.utils.SuccessResponseStatusType;
import com.example.demo.wrapper.ErrorResponseWrapper;
import com.example.demo.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Controller for meeting service
 */
@Slf4j
@RestController
@Validated
@RequestMapping("api/v1/meet")
public class MeetingController {

    private static final int DEFAULT_PAGE = 0;
    private static final int PAGE_MAX_SIZE = 250;
    private static final String INVALID_TIME_LOG = "Invalid time zone of userId: {}, Time zone: {}";
    private final int inviteesLimit;
    private final MeetingService meetingService;
    Logger logger = LoggerFactory.getLogger(MeetingController.class);
    ResourceBundleMessageSourceBean resourceBundleMessageSourceBean = new ResourceBundleMessageSourceBean();
    Translator translator = new Translator(resourceBundleMessageSourceBean.messageSource());

    @Autowired
    public MeetingController(MeetingService meetingService, @Value("${inviteesLimit}") final int inviteesLimit) {
        this.meetingService = meetingService;
        this.inviteesLimit = inviteesLimit;
    }

    /**
     * Schedule a meeting
     *
     * @param userId            meeting creator's Id
     * @param timeZone
     * @param meetingRequestDto
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ResponseWrapper> createMeeting(@RequestHeader(name = "X-UserId") String userId,
                                                         @RequestHeader(name = "X-timeZone") String timeZone,
                                                         @RequestBody MeetingRequestDto meetingRequestDto) {
        try {
            meetingRequestDto.setCreateUserId(userId);
            String objectToJson = meetingRequestDto.toLogJson();

            logger.debug("create meeting request by userId {}, meetingRequestDto: {}", userId, objectToJson);

            if (!isValidTimeZone(timeZone)) {
                logger.debug(INVALID_TIME_LOG, userId, timeZone);
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIMEZONE);
            }

            if (!meetingRequestDto.isRequiredAvailable()) {
                return getErrorResponse(ErrorsResponseStatusType.FIELDS_MISSING);
            }

            if (!meetingRequestDto.isValidMeetingDuration(meetingRequestDto.getHours(),
                    meetingRequestDto.getMinutes())) {
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIME_DURATION);
            }

            if (!meetingRequestDto.isValidDateTime()) {
                return getErrorResponse(ErrorsResponseStatusType.INVALID_DATETIME);
            }

            if (!meetingRequestDto.isValidInviteeLimit(inviteesLimit)) {
                return getErrorResponse(ErrorsResponseStatusType.LIMIT_EXCEED);
            }

            return getSuccessResponse(createMeetingResponse(meetingRequestDto, objectToJson), SuccessResponseStatusType.CREATE_MEETING);

        } catch (SwivelMeetServiceException e) {
            logger.error("Error when create meeting by userId: {}", userId, e);
            return getInternalServerError();
        }
    }

    /**
     * Read meeting by Id
     *
     * @param userId    user Id
     * @param timeZone
     * @param meetingId request meeting id
     * @return
     */
    @GetMapping("/{meetingId}")
    public ResponseEntity<ResponseWrapper> readMeeting(@RequestHeader(name = "X-UserId") String userId,
                                                       @RequestHeader(name = "X-TimeZone") String timeZone,
                                                       @PathVariable String meetingId) {
        try {
            if (!isValidTimeZone(timeZone)) {
                logger.debug("Invalid time zone of userId: {}, meetingId: {}, Time zone: {}", userId,
                        meetingId, timeZone);
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIMEZONE);
            }

            Meeting meeting = meetingService.getMeeting(meetingId);

            MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting);
            String objectToJson = meetingResponseDto.toLogJson();

            logger.debug("Successfully meeting read by userId {}, meetingId {}, meeting response {}",
                    userId, meeting, objectToJson);
            return getSuccessResponse(meetingResponseDto, SuccessResponseStatusType.READ_MEETING);

        } catch (InvalidMeetingException e) {
            logger.error("Error when read meeting, meetingId {}", meetingId, e);
            return getErrorResponse(ErrorsResponseStatusType.INVALID_MEETING);
        } catch (SwivelMeetServiceException e) {
            logger.error("Error when read meeting, meetingId {}", meetingId, e);
            return getInternalServerError();
        }
    }

    /**
     * Read meeting list
     *
     * @param userId
     * @param timeZone
     * @param page     request page
     * @param size     request size
     * @return
     */
    @GetMapping("/{page}/{size}")
    public ResponseEntity<ResponseWrapper> readMeetingList(@RequestHeader(name = "X-UserId") String userId,
                                                           @RequestHeader(name = "X-TimeZone") String timeZone,
                                                           @Min(DEFAULT_PAGE) @PathVariable int page,
                                                           @Positive @Max(PAGE_MAX_SIZE) @PathVariable int size) {
        try {

            if (!isValidTimeZone(timeZone)) {
                logger.debug(INVALID_TIME_LOG, userId, timeZone);
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIMEZONE);
            }

            Page<Meeting> meetingListPage = meetingService.listAllMeetings(page, size);
            List<MeetingResponseDto> list = new ArrayList<>();

            for (int i = 0; i < meetingListPage.getContent().size(); i++) {
                MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meetingListPage.getContent().get(i));
                list.add(meetingResponseDto);
            }

            MeetingListResponseDto meetingListResponseDto = new MeetingListResponseDto(meetingListPage, list);

            String objectToJson = meetingListResponseDto.toLogJson();

            logger.debug("Successfully meeting list read by userId {}, meeting response {}", userId,
                    objectToJson);
            return getSuccessResponse(meetingListResponseDto, SuccessResponseStatusType.READ_SUMMARY);

        } catch (SwivelMeetServiceException e) {
            logger.error("Error when read meeting list", e);
            return getInternalServerError();
        }
    }

    /**
     * Update meeting by Id
     *
     * @param userId
     * @param timeZone
     * @param updateMeetingRequestDto request body
     * @return
     */
    @PutMapping("")
    public ResponseEntity<ResponseWrapper> updateMeeting(@RequestHeader(name = "X-UserId") String userId,
                                                         @RequestHeader(name = "X-timeZone") String timeZone,
                                                         @RequestBody UpdateMeetingRequestDto updateMeetingRequestDto) {
        try {
            String objectToJson = updateMeetingRequestDto.toLogJson();
            logger.debug("update meeting request for userId {}, meetingRequestDto: {}", userId, objectToJson);

            if (!isValidTimeZone(timeZone)) {
                logger.debug(INVALID_TIME_LOG, userId, timeZone);
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIMEZONE);
            }

            if (!updateMeetingRequestDto.isRequiredAvailable()) {
                return getErrorResponse(ErrorsResponseStatusType.FIELDS_MISSING);
            }

            if (!updateMeetingRequestDto.isValidMeetingDuration(updateMeetingRequestDto.getHours(),
                    updateMeetingRequestDto.getMinutes())) {
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIME_DURATION);
            }

            if (!updateMeetingRequestDto.isValidDateTime()) {
                return getErrorResponse(ErrorsResponseStatusType.INVALID_DATETIME);
            }

            if (!updateMeetingRequestDto.isValidInviteeLimit(inviteesLimit)) {
                return getErrorResponse(ErrorsResponseStatusType.LIMIT_EXCEED);
            }

            return getSuccessResponse(createUpdateMeetingResponse(updateMeetingRequestDto, objectToJson),
                    SuccessResponseStatusType.UPDATE_MEETING);

        } catch (InvalidMeetingException e) {
            logger.error("Error when update meeting, meetingId {}", updateMeetingRequestDto.getMeetingId(), e);
            return getErrorResponse(ErrorsResponseStatusType.INVALID_MEETING);
        } catch (SwivelMeetServiceException e) {
            logger.error("Error when update meeting, meetingId {}", updateMeetingRequestDto.getMeetingId(), e);
            return getInternalServerError();
        }
    }

    /**
     * Delete meeting by Id
     *
     * @param userId
     * @param timeZone
     * @param meetingId
     * @return
     */
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<ResponseWrapper> deleteMeeting(@RequestHeader(name = "X-UserId") String userId,
                                                         @RequestHeader(name = "X-TimeZone") String timeZone,
                                                         @PathVariable String meetingId) {
        try {
            if (!isValidTimeZone(timeZone)) {
                logger.debug("Invalid time zone of userId: {}, meetingId: {}, Time zone: {}", userId,
                        meetingId, timeZone);
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIMEZONE);
            }

            meetingService.deleteMeeting(meetingId);

            logger.debug("Successfully meeting deleted by userId {}, meetingId {}",
                    userId, meetingId);
            return getSuccessResponse(null, SuccessResponseStatusType.DELETE_MEETING);

        } catch (InvalidMeetingException e) {
            logger.error("Error when delete meeting, meetingId {}", meetingId, e);
            return getErrorResponse(ErrorsResponseStatusType.INVALID_MEETING);
        } catch (SwivelMeetServiceException e) {
            logger.error("Error when delete meeting, meetingId {}", meetingId, e);
            return getInternalServerError();
        }
    }

    /**
     * Update meeting delete status
     *
     * @param userId
     * @param timeZone
     * @param meetingId
     * @return
     */
    @PutMapping("/{meetingId}")
    public ResponseEntity<ResponseWrapper> updateMeetingDeleteStatus(@RequestHeader(name = "X-UserId") String userId,
                                                                     @RequestHeader(name = "X-TimeZone") String timeZone,
                                                                     @PathVariable String meetingId) {
        try {
            if (!isValidTimeZone(timeZone)) {
                logger.debug("Invalid time zone of userId: {}, meetingId: {}, Time zone: {}", userId,
                        meetingId, timeZone);
                return getErrorResponse(ErrorsResponseStatusType.INVALID_TIMEZONE);
            }

            meetingService.updateMeetingDeleteStatus(meetingId);

            logger.debug("Successfully meeting delete flag updated by userId {}, meetingId {}",
                    userId, meetingId);
            return getSuccessResponse(null, SuccessResponseStatusType.DELETE_MEETING);

        } catch (InvalidMeetingException e) {
            logger.error("Error when update delete flag, meetingId {}", meetingId, e);
            return getErrorResponse(ErrorsResponseStatusType.INVALID_MEETING);
        } catch (SwivelMeetServiceException e) {
            logger.error("Error when update delete flag, meetingId {}", meetingId, e);
            return getInternalServerError();
        }
    }

    /**
     * Create meeting response dto using meeting request dto
     *
     * @param meetingRequestDto get from request body
     * @return meeting response dto
     */
    private MeetingResponseDto createMeetingResponse(MeetingRequestDto meetingRequestDto, String objectToJson) {
        logger.debug("meeting created by userId {}, meetingRequestDto: {}", meetingRequestDto.getCreateUserId(), objectToJson);
        Meeting meeting = new Meeting(meetingRequestDto);
        meetingService.createMeeting(meeting);
        return new MeetingResponseDto(meeting);
    }

    /**
     * Update meeting response dto using meeting request dto
     *
     * @param updateMeetingRequestDto get from request body
     * @return meeting response dto
     */
    private MeetingResponseDto createUpdateMeetingResponse(UpdateMeetingRequestDto updateMeetingRequestDto, String objectToJson) {
        logger.debug("meeting updated by userId {}, meetingRequestDto: {}", updateMeetingRequestDto.getCreateUserId(), objectToJson);
        Meeting meeting = new Meeting(updateMeetingRequestDto);
        Meeting meeting1 = meetingService.updateMeeting(meeting);
        return new MeetingResponseDto(meeting1);
    }


    /**
     * Validate the time zone
     *
     * @param timeZone
     * @return
     */
    private boolean isValidTimeZone(String timeZone) {
        for (String tzId : TimeZone.getAvailableIDs()) {
            if (tzId.equals(timeZone)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Success response wrapper
     *
     * @param responseDto
     * @return response entity
     */
    private ResponseEntity<ResponseWrapper> getSuccessResponse(ResponseDto responseDto,
                                                               SuccessResponseStatusType successResponseStatusType) {
        ResponseWrapper responseWrapper = new ResponseWrapper(ResponseStatusType.SUCCESS,
                successResponseStatusType.getMessage(), responseDto,
                translator.toLocale(successResponseStatusType.
                        getCodeString(successResponseStatusType.getSuccessCode())));
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    /**
     * Failure response wrapper for internal server error
     *
     * @return response entity
     */
    private ResponseEntity<ResponseWrapper> getInternalServerError() {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorsResponseStatusType.INTERNAL_SERVERERROR.getMessage(), null, translator.toLocale(ErrorsResponseStatusType.getCodeString(ErrorsResponseStatusType.INTERNAL_SERVERERROR.getErrorCode())), ErrorsResponseStatusType.INTERNAL_SERVERERROR.getErrorCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param errorsResponseStatusType enum containing error code and developer messages
     * @return response entity
     */
    private ResponseEntity<ResponseWrapper> getErrorResponse(ErrorsResponseStatusType errorsResponseStatusType) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, errorsResponseStatusType.getMessage(), null, translator.toLocale(ErrorsResponseStatusType.getCodeString(errorsResponseStatusType.getErrorCode())), errorsResponseStatusType.getErrorCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

}


