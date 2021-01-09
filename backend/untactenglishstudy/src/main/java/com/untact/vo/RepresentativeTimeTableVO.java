package com.untact.vo;

import java.util.List;

import com.untact.representativetimetableitem.domain.RepresentativeTimeTableItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepresentativeTimeTableVO {
	private String title;
	private List<RepresentativeTimeTableItem> timeTableItem;
}
