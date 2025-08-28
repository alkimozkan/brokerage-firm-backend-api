package com.alkim.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkim.controller.dto.DtoOrder;
import com.alkim.controller.dto.DtoOrderIU;
import com.alkim.controller.impl.OrderControllerImpl.OrderStatus;
import com.alkim.controller.repository.OrderRepository;
import com.alkim.entities.Order;
import com.alkim.entities.Status;
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
		    	System.out.println("Sell order size cannot be greater than usable size!!");
		    	response.setErrorMessage("Sell order size cannot be greater than usable size!!");
		    	return response;
		    } else {
		    	Long newValue = usableSize - dtoOrderIU.getSize();
		    	assetServiceImpl.updateUsableSize(dtoOrderIU.getCustomer().getId(), dtoOrderIU.getAssetName(), newValue);
		    }
		    
	    } else if (dtoOrderIU.getOrderSide().getId() == 0){ //BUY
	    	Long totalAmount = dtoOrderIU.getPrice() * dtoOrderIU.getSize();
	    	Long usableSizeTRY = assetServiceImpl.getAssetUsableSize(dtoOrderIU.getCustomer().getId(), "TRY");
	    	if(totalAmount > usableSizeTRY) {
	    		System.out.println("Usable size TRY should be greater than order amount!!");
	    		response.setErrorMessage("Usable size TRY should be greater than order amount!!");
		    	return response;
	    	} else {
	    		Long newValue = usableSizeTRY - totalAmount;
	    		assetServiceImpl.updateUsableSize(dtoOrderIU.getCustomer().getId(), "TRY", newValue);
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
			
			
			dtoList.add(dto);
		}
		
		return dtoList;
	}
	
	
	public List<Order> getPendingOrdersByCustomerId(Long customerId) {
		
		//List<DtoOrder> dtoList = new ArrayList<>();
		List<Order> pendingOrderList = new ArrayList<>();

		List<Order> orderList = orderRepository.findByCustomerId(customerId);
		
		for (Order order : orderList) {
			
			if(!order.getStatus().getStatusName().equals("PENDING")) { //TODO: can be changed with final param 
				continue;
			}
			
			pendingOrderList.add(order);
		}
		
		return pendingOrderList;
		
	}
	
	@Override
	public String deleteOrderById(Long id, Long customerId) {
		
		List<Order> pendingOrderList = getPendingOrdersByCustomerId(customerId);
		
		Status nStatus = new Status();
	    OrderStatus orderStatus = OrderStatus.CANCELED;
	    nStatus.setId(orderStatus.getCode());
		
		if(!pendingOrderList.isEmpty()) {
			for (Order order : pendingOrderList) {
				if(order.getId() == id) {
					Order updateOrder = order;
					updateOrder.setStatus(nStatus);
					orderRepository.save(updateOrder);
					
					if(updateOrder.getOrderSide().getId() == 0) { //Canceling a BUY
						Long amount = updateOrder.getPrice() * updateOrder.getSize();
						Long usableSizeTRY = assetServiceImpl.getAssetUsableSize(customerId, "TRY") + amount;
						assetServiceImpl.updateUsableSize(customerId, "TRY", usableSizeTRY);
						return "Buy order canceled succesfully";
					} else if(updateOrder.getOrderSide().getId() == 1) { //Canceling a SELL
						Long amount = updateOrder.getPrice() * updateOrder.getSize();
						Long usableSizeTRY = assetServiceImpl.getAssetUsableSize(customerId, updateOrder.getAsset().getAssetName()) + amount;
						assetServiceImpl.updateUsableSize(customerId, updateOrder.getAsset().getAssetName(), usableSizeTRY);
						return "Sell order canceled succesfully";
					}
				}
			}
		} else {
			return "There is no pending order for this customer";
		}
		return "You cannot cancel this order";
	}

	

	
	
}
