package com.example.demo.domain.entity;

import com.example.demo.domain.request.MeetingRequestDto;
import com.example.demo.domain.request.UpdateMeetingRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Meeting entity
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "MEETING")
public class Meeting {
    @Transient
    private static final String MID_PREFIX = "mid-";

    @Id
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
    private long createdAt;
    private long updatedAt;
    private boolean isDeleted;

    public Meeting() {
        this.meetingId = MID_PREFIX + UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Meeting(MeetingRequestDto meetingRequestDto) {

        this.meetingId = MID_PREFIX + UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.isDeleted = false;
        this.name = meetingRequestDto.getName();
        this.dateTime = meetingRequestDto.getDateTime();
        this.createUserId = meetingRequestDto.getCreateUserId();
        this.hours = meetingRequestDto.getHours();
        this.minutes = meetingRequestDto.getMinutes();
        this.inviteUserIds = meetingRequestDto.getInviteUserIds();
        this.videoOnAtJoin = meetingRequestDto.isVideoOnAtJoin();
        this.micOnAtJoin = meetingRequestDto.isMicOnAtJoin();
        this.recordStatus = meetingRequestDto.isRecordStatus();
    }

    public Meeting(UpdateMeetingRequestDto updateMeetingRequestDto) {

        this.meetingId = updateMeetingRequestDto.getMeetingId();
        this.name = updateMeetingRequestDto.getName();
        this.dateTime = updateMeetingRequestDto.getDateTime();
        this.createUserId = updateMeetingRequestDto.getCreateUserId();
        this.hours = updateMeetingRequestDto.getHours();
        this.minutes = updateMeetingRequestDto.getMinutes();
        this.inviteUserIds = updateMeetingRequestDto.getInviteUserIds();
        this.videoOnAtJoin = updateMeetingRequestDto.isVideoOnAtJoin();
        this.micOnAtJoin = updateMeetingRequestDto.isMicOnAtJoin();
        this.recordStatus = updateMeetingRequestDto.isRecordStatus();
        this.updatedAt = System.currentTimeMillis();
        this.isDeleted = updateMeetingRequestDto.isDeleted();
    }
}