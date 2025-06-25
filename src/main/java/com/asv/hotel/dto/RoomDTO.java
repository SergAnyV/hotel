package com.asv.hotel.dto;

import com.asv.hotel.entities.Room;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RoomDTO  {
    private String number;
    private String type;
    private String description;
    private Integer capacity;
    private BigDecimal pricePerNight;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
