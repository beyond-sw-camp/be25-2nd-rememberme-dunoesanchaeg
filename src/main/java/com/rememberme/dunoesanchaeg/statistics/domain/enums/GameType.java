package com.rememberme.dunoesanchaeg.statistics.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {
    WORD_MEMORY("기억력"),
    ARITHMETIC("계산력"),
    DESCARTES_RPS("판단력");

    private final String displayName;
}