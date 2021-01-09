package com.untact.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.untact.constant.RegexpConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeMemberPasswordVO {
	@NotNull(message="emailnotnull")
	@NotEmpty(message="emailnotnull")
	@Pattern(regexp=RegexpConstant.EMAIL_REGEXP,
	message="emailregexp")
	private String email;
	
	@NotNull(message="pwnotnull")
	@NotEmpty(message="pwnotnull")
	@Pattern(regexp=RegexpConstant.PW_REGEXP,
	message="pwregexp")
	private String oldPw;
	
	@NotNull(message="pwnotnull")
	@NotEmpty(message="pwnotnull")
	@Pattern(regexp=RegexpConstant.PW_REGEXP,
	message="pwregexp")
	private String newPw;
}
