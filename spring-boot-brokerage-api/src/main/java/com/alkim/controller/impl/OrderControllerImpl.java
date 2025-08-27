package com.alkim.controller.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkim.controller.IOrderController;
import com.alkim.controller.dto.DtoOrder;
import com.alkim.controller.dto.DtoOrderIU;
import com.alkim.entities.Status;
import com.alkim.services.IOrderService;

@RestController
@RequestMapping("/rest/api/order")
public class OrderControllerImpl implements IOrderController{
	
	@Autowired
	private IOrderService orderService;
	
	
	
	@Override
	@PostMapping(path = "/save")
	public DtoOrder createOrder(@RequestBody DtoOrderIU dtoOrderIU) {
	    Status nStatus = new Status();
	    OrderStatus orderStatus = OrderStatus.PENDING;
	    nStatus.setId(orderStatus.getCode()); //PENDING (Hard-coded)
	    dtoOrderIU.setStatus(nStatus);
		return orderService.createOrder(dtoOrderIU);
	}
	
	public enum OrderStatus {
	    PENDING((long)0),
	    MATCHED((long)1),
	    CANCELED((long)2);

	    private final long code;

	    OrderStatus(long l) {
	    	this.code = l;
		}

	    public long getCode() {
	        return code;
	    }
	}
	
	@GetMapping(path = "list/{id}/{startDate}/{endDate}")
	@Override
	public List<DtoOrder> getOrderByIdAndDate(@PathVariable(name = "id") Long id, @PathVariable(name = "startDate") 
	@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @PathVariable(name = "endDate") 
	@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		// TODO Auto-generated method stub
		return orderService.getOrderByIdAndDate(id, startDate, endDate);
	}
	
	

}
