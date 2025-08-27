package com.alkim.controller.dto;

import java.util.Date;

import com.alkim.entities.Asset;
import com.alkim.entities.Customer;
import com.alkim.entities.Side;
import com.alkim.entities.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoOrderIU {
	
	private Customer customer;

	private Asset asset;
	
	private String assetName;
	
	private Side orderSide;
	
	private Long size;

	private Long price;

	private Status status;

	private Date createDate;

}
