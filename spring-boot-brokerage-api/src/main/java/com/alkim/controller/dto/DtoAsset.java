package com.alkim.controller.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoAsset {
	
	private String assetName;
	
	private Long size;
	
	private Long usableSize;

}
