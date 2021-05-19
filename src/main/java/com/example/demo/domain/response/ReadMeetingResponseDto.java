package com.example.demo.domain.response;

import com.example.demo.domain.entity.Meeting;
import com.example.demo.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto gives meeting details including user objects
 */
@Getter
@Setter
public class ReadMeetingResponseDto implements ResponseDto {

    private final String meetingId;
    private final String name;
    private final Timestamp dateTime;
    private final User createUser;
    private final int hours;
    private final int minutes;
    @ElementCollection
    private final List<User> userList;
    private final boolean videoOnAtJoin;
    private final boolean micOnAtJoin;
    private final boolean recordStatus;
    private final long createdAt;
    private final long updatedAt;

    public ReadMeetingResponseDto(Meeting meeting, UserResponseDto userResponseDto) {

        this.meetingId = meeting.getMeetingId();
        this.name = meeting.getName();
        this.dateTime = meeting.getDateTime();
        this.createUser = userResponseDto.users.get(meeting.getCreateUserId());
        this.hours = meeting.getHours();
        this.minutes = meeting.getMinutes();
        userResponseDto.users.remove(meeting.getCreateUserId());
        this.userList = new ArrayList<>(userResponseDto.users.values());
        this.videoOnAtJoin = meeting.isVideoOnAtJoin();
        this.micOnAtJoin = meeting.isMicOnAtJoin();
        this.recordStatus = meeting.isRecordStatus();
        this.updatedAt = meeting.getUpdatedAt();
        this.createdAt = meeting.getCreatedAt();

    }

    @Override
    public String toLogJson() {
        return null;
    }
}
