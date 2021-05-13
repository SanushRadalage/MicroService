package com.example.demo.domain.request;

import com.example.demo.domain.entity.Meeting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.ElementCollection;
import java.sql.Timestamp;
import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UpdateMeetingRequestDto extends RequestDto {
    private String meetingId;
    private String name;
    private Timestamp dateTime;
    private String createUserId;
    private int hours;
    private int minutes;
    @ElementCollection
    private List<String> inviteUserIds;
    private boolean videoOnAtJoin;
    private boolean micOnAtJoin;
    private boolean recordStatus;
    private boolean isDeleted;

    public UpdateMeetingRequestDto(Meeting meeting) {
        meetingId = meeting.getMeetingId();
        this.name = meeting.getName();
        this.dateTime = meeting.getDateTime();
        this.createUserId = meeting.getCreateUserId();
        this.hours = meeting.getHours();
        this.minutes = meeting.getMinutes();
        this.inviteUserIds = meeting.getInviteUserIds();
        this.videoOnAtJoin = meeting.isVideoOnAtJoin();
        this.micOnAtJoin = meeting.isMicOnAtJoin();
        this.recordStatus = meeting.isRecordStatus();
        this.isDeleted = meeting.isDeleted();
    }

    /**
     * Validate availability of all the required fields
     *
     * @return
     */
    @Override
    public boolean isRequiredAvailable() {
        return isEmpty(name) && isEmpty(createUserId) && dateTime != null && hours >= 0 && minutes >= 0 && inviteUserIds != null && !inviteUserIds.isEmpty();
    }

    /**
     * Validate the meeting start time. It must to be a future time stamp
     *
     * @return
     */
    public boolean isValidDateTime() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return (dateTime).after(currentTimestamp);
    }

    /**
     * Validate the duration of the meeting
     *
     * @param hours
     * @param minutes
     * @return
     */
    public boolean isValidMeetingDuration(int hours, int minutes) {
        return hours < 24 && minutes < 60;
    }

    /**
     * Validate the invitees count
     *
     * @param inviteesLimit value read from property file
     * @return
     */
    public boolean isValidInviteeLimit(int inviteesLimit) {
        return inviteUserIds.size() <= inviteesLimit;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
