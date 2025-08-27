package com.alkim.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkim.controller.dto.DtoOrder;
import com.alkim.controller.dto.DtoOrderIU;
import com.alkim.controller.repository.OrderRepository;
import com.alkim.entities.Order;
import com.alkim.services.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private AssetSeviceImpl assetServiceImpl;

	@Override
	public DtoOrder createOrder(DtoOrderIU dtoOrderIU) {
		DtoOrder response = new DtoOrder();
		Order order = new Order();
		Long usableSize = assetServiceImpl.getAssetUsableSize(dtoOrderIU.getCustomer().getId(), dtoOrderIU.getAssetName());
	    System.out.println("usableSize: "+usableSize);
	    
	    if(dtoOrderIU.getOrderSide().getId() == 1) { //SELL
		    if(usableSize < dtoOrderIU.getSize()) {
		    	System.out.println("Size is greater than usable size !!");
		    	return null;
		    }
	    } else if (dtoOrderIU.getOrderSide().getId() == 0){ //BUY
	    	Long totalAmount = dtoOrderIU.getPrice() * dtoOrderIU.getSize();
	    	Long usableSizeTRY = assetServiceImpl.getAssetUsableSize(dtoOrderIU.getCustomer().getId(), "TRY");
	    	if(totalAmount > usableSizeTRY) {
	    		System.out.println("Usable size TRY should be greater than total amount !!");
	    		return null;
	    	}
	    } else {
	    	return null;
	    }
	    	
		BeanUtils.copyProperties(dtoOrderIU, order);
		Order dbOrder = orderRepository.save(order);
		BeanUtils.copyProperties(dbOrder, response); 
		return response;
	}

	@Override
	public List<DtoOrder> getOrderByIdAndDate(Long id, Date startDate, Date endDate) {
		
		List<DtoOrder> dtoList = new ArrayList<>();
		List<Order> orderList = orderRepository.findByCustomerIdAndCreateDateBetween(id, startDate, endDate);
		for (Order order : orderList) {
			DtoOrder dto = new DtoOrder();
			dto.setSize(order.getSize());
			dto.setCreateDate(order.getCreateDate());
			dto.setPrice(order.getPrice());
			dto.setCustomerName(order.getCustomer().getCustomerName());
			//dto.setCustomerID(order.getCustomer().getId());
			//dto.setAssetID(order.getAsset().getId());
			dto.setAssetName(order.getAsset().getAssetName());
			//dto.setOrderSide(order.getOrderSide());
			dto.setOrderType(order.getOrderSide().getSideName());
			//dto.setStatus(order.getStatus());
			dto.setStatusType(order.getStatus().getStatusName());
			
			//BeanUtils.copyProperties(order, dto);
			dtoList.add(dto);
		}
		
		return dtoList;
	}

	

	
	
}
