package com.asv.hotel.util;

public interface DTOConvertable<DTO,Model> {
  DTO convertToDTO(Model model);
  Model convertToEntity (DTO dto);
}
