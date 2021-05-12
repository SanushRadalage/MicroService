package com.example.demo.domain.response;

import com.example.demo.domain.entity.Meeting;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Response entity for meeting list
 */
@Getter
@Setter
public class MeetingListResponseDto extends PageResponseDto {

    private final List<MeetingResponseDto> meetingList;

    public MeetingListResponseDto(Page<Meeting> page, List<MeetingResponseDto> meetingResponseDtoList) {
        super(page);
        this.meetingList = new ArrayList<>();
        this.meetingList.addAll(meetingResponseDtoList);
    }


    @Override
    public String toLogJson() {
        return toJson();
    }
}
