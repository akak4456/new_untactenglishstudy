package com.example.study.global;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonHeader {
    private static CommonHeader instance = null;
    private Map<String,String> commonHeader = new ConcurrentHashMap<String,String>();
    private Date loginDate;

    public Map<String,String> getCommonHeader() throws IllegalArgumentException{
        if(commonHeader == null){
            throw new IllegalArgumentException();
        }
        long diff = new Date().getTime() - loginDate.getTime();
        if(diff >= 30*60*1000){
            //로그인 한지 30분이 지났으면
            invalidate();
        }
        return this.commonHeader;
    }
    public void invalidate(){
        commonHeader = new ConcurrentHashMap<>();
    }
    public void setJWTToken(String jwtToken){
        this.commonHeader.put("X-AUTH-TOKEN",jwtToken);
        loginDate = new Date();
    }
    public static synchronized CommonHeader getInstance(){
        if(instance == null) {
            instance = new CommonHeader();
        }
        return instance;
    }
}
