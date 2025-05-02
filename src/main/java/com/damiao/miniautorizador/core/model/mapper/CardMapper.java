package com.damiao.miniautorizador.core.model.mapper;

import com.damiao.miniautorizador.core.model.dto.CardDto;
import com.damiao.miniautorizador.core.model.dto.CardResponseDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.util.Utilities;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {Utilities.class})
public interface CardMapper {

    @Mapping(target = "cardNumber", expression = "java(dto.getNumeroCartao())")
    @Mapping(target = "cardPassword", expression = "java(Utilities.encryptPassword(dto.getSenha()))")
    Card cardDtoToCard(CardDto dto);

    CardResponseDto cardToCardResponseDto(CardDto cardDto);
}
