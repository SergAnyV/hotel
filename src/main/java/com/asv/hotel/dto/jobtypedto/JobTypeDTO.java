package com.asv.hotel.dto.jobtypedto;

import com.asv.hotel.entities.UserType;
import lombok.Builder;
import lombok.Data;


import java.util.Set;

@Builder
@Data
public class JobTypeDTO {

    private String title;

    private String description;

    private Boolean isActive;

    private Set<UserType> userTypes;

}
