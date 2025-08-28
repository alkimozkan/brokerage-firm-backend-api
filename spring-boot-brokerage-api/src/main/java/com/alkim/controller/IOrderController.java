package com.alkim.controller;

import java.util.Date;
import java.util.List;


import com.alkim.controller.dto.DtoOrder;
import com.alkim.controller.dto.DtoOrderIU;

public interface IOrderController {
	
	public DtoOrder createOrder(DtoOrderIU dtoOrderIU);
	
	public List<DtoOrder> getOrderByIdAndDate(Long id, Date startDate, Date endDate);
	
	public String deleteOrderById(Long id, Long customerId);
}
