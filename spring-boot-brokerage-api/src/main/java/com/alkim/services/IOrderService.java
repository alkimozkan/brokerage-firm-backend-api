package com.alkim.services;

import java.util.Date;
import java.util.List;

import com.alkim.controller.dto.DtoOrder;
import com.alkim.controller.dto.DtoOrderIU;

public interface IOrderService {
	
	public DtoOrder createOrder(DtoOrderIU order);
	
	public List<DtoOrder> getOrderByIdAndDate(Long id, Date startDate, Date endDate);
	


}
