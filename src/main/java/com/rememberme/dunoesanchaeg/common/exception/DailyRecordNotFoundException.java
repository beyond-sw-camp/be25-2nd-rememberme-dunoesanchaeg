package com.rememberme.dunoesanchaeg.common.exception;

public class DailyRecordNotFoundException extends BaseException{
    public DailyRecordNotFoundException(){
        super(404, "오늘의 하루기록이 존재하지 않습니다.");
    }
}
