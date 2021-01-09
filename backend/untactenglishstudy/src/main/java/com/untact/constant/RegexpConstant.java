package com.untact.constant;

public class RegexpConstant {
	public static final String PW_REGEXP = "^(?=.*[0-9]{1,50})(?=.*[~`!@#$%\\^&*()-+=]{1,50})(?=.*[a-zA-Z]{1,50}).{8,50}$";
	//8~50자 영어 대소문자, 숫자, 특수문자를 모두 포함해야함
	public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
	//이메일 형식
	private RegexpConstant() {
		
	}
}
