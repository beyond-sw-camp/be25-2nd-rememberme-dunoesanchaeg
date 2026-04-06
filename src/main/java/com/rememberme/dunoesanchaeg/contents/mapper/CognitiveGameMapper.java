package com.rememberme.dunoesanchaeg.contents.mapper;

import com.rememberme.dunoesanchaeg.contents.dto.request.GameResultInsertDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CognitiveGameMapper {

    int insertGameResult(GameResultInsertDto dto);
}