package com.untact.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.untact.constant.RegexpConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeMemberInfoVO {
	@NotNull(message="emailnotnull")
	@NotEmpty(message="emailnotnull")
	@Pattern(regexp=RegexpConstant.EMAIL_REGEXP,
	message="emailregexp")
	private String email;
	
	@NotNull(message="namenotnull")
	@NotEmpty(message="namenotnull")
	private String name;
}
