package com.main.dto;

import com.main.entity.ComplaintStatus;
import com.main.entity.ComplaintType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ComplaintRequest {
    private ComplaintStatus status;
    private List<String> files;
    private ComplaintType type;
    private String location;
    private String description;
    private String complainedAbout;
    private Integer complainantId;
}

