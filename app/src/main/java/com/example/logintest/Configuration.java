package com.example.logintest;

public class Configuration {
    // google apps script 연결, 자체 제작 api 활용
    // 이미지 파일이 현재 공유 문서함 드라이브에 기재되지 않는 문제 존재
    public static final String APP_SCRIPT_WEB_APP_URL = "https://script.google.com/macros/s/AKfycbzLLSgKboEdxpA4U5K_iFaDYgHHDSViXRIhoadSJ0TU-O6Bzwo3a9LvoJxKZDVYsMwANw/exec";
    public static final String ADD_USER_URL = APP_SCRIPT_WEB_APP_URL;

    // 학번, 이름, 이미지 변수
    public static final String KEY_ID = "uId";
    public static final String KEY_NAME = "uName";
    public static final String KEY_IMAGE = "uImage";

    //google spreadsheet insert 동작 변수
    public  static final String KEY_ACTION = "action";

    //google spreadsheet 주차 변수
    public static final String KEY_WEEK = "week";
}