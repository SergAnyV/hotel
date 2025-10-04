package com.asv.hotel.dto.jobtypedto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JobTypeSimpleDTO {

    private String title;

    private String description;

}
