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
public class DtoOrder {
	
	//private Customer customer;
	
	private String customerName;
	
	private Long orderID;
	
	//private Long customerID;

	//private Asset asset;
	
	private String assetName;
	
	//private Long assetID;
	
	//private Side orderSide;
	
	private String orderType;
	
	private Long size;

	private Long price;

	//private Status status;
	
	private String statusType;

	private Date createDate;
	
	private String errorMessage;

}
