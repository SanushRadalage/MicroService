package com.example.demo.domain.response;

import com.example.demo.domain.entity.Meeting;
import lombok.Getter;

import javax.persistence.ElementCollection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Response Dto for meeting service
 */
@Getter
public class MeetingResponseDto implements ResponseDto {

    private final String meetingId;
    private final String name;
    private final Timestamp dateTime;
    private final String createUserId;
    private final int hours;
    private final int minutes;
    @ElementCollection
    private final List<String> inviteUserIds;
    private final boolean videoOnAtJoin;
    private final boolean micOnAtJoin;
    private final boolean recordStatus;
    private final long createdAt;
    private final long updatedAt;

    public MeetingResponseDto(Meeting meeting) {
        this.meetingId = meeting.getMeetingId();
        this.name = meeting.getName();
        this.dateTime = meeting.getDateTime();
        this.createUserId = meeting.getCreateUserId();
        this.hours = meeting.getHours();
        this.minutes = meeting.getMinutes();
        this.inviteUserIds = meeting.getInviteUserIds();
        this.videoOnAtJoin = meeting.isVideoOnAtJoin();
        this.micOnAtJoin = meeting.isMicOnAtJoin();
        this.recordStatus = meeting.isRecordStatus();
        this.updatedAt = meeting.getUpdatedAt();
        this.createdAt = meeting.getCreatedAt();
//        DateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date dateCreated = new Date(meeting.getCreatedAt());
//        Date dateUpdated = new Date(meeting.getUpdatedAt());
//
//        this.createdAt = simple.format(dateCreated);
//        this.updatedAt = simple.format(dateUpdated);
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
