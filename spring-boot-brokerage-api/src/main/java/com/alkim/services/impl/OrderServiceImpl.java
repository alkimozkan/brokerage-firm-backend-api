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
		
		if(dtoOrderIU.getAssetName().equals("TRY")) {
			System.out.println("You cannot buy or sell TRY!!");
	    	response.setErrorMessage("You cannot buy or sell TRY!!");
	    	return response;
		}
		
		Long usableSize = assetServiceImpl.getAssetUsableSize(dtoOrderIU.getCustomer().getId(), dtoOrderIU.getAssetName());
		Long size = assetServiceImpl.getAssetSize(dtoOrderIU.getCustomer().getId(), dtoOrderIU.getAssetName());
		Long sizeTRY = assetServiceImpl.getAssetSize(dtoOrderIU.getCustomer().getId(), "TRY");

		
	    System.out.println("usableSize: "+usableSize);
	    System.out.println("size: "+size);

	    
	    
	    if(usableSize != null) {
	    	
		    if(dtoOrderIU.getOrderSide().getSideName().equals("SELL") ) { //SELL
			    if(usableSize < dtoOrderIU.getSize()) {
			    	System.out.println("Sell order size cannot be greater than usable size!!");
			    	response.setErrorMessage("Sell order size cannot be greater than usable size!!");
			    	return response;
			    } else {
			    	Long newValueUsableSize = usableSize - dtoOrderIU.getSize();
			    	Long newValueSizeNonTry = size - dtoOrderIU.getSize();
			    	assetServiceImpl.updateSize(dtoOrderIU.getCustomer().getId(), dtoOrderIU.getAssetName(), newValueSizeNonTry);
			    	assetServiceImpl.updateUsableSize(dtoOrderIU.getCustomer().getId(), dtoOrderIU.getAssetName(), newValueUsableSize); //Update non TRY usable size
			    	Long newValueSize = sizeTRY + dtoOrderIU.getPrice() * dtoOrderIU.getSize(); 
			    	assetServiceImpl.updateSize(dtoOrderIU.getCustomer().getId(), "TRY", newValueSize); //update TRY size
			    }
			    
		    } else if (dtoOrderIU.getOrderSide().getSideName().equals("BUY")){ //BUY
		    	Long totalAmount = dtoOrderIU.getPrice() * dtoOrderIU.getSize();
		    	Long usableSizeTRY = assetServiceImpl.getAssetUsableSize(dtoOrderIU.getCustomer().getId(), "TRY");
		    	if(totalAmount > usableSizeTRY) {
		    		System.out.println("Usable size TRY should be greater than order amount!!");
		    		response.setErrorMessage("Usable size TRY should be greater than order amount!!");
			    	return response;
		    	} else {
		    		Long newValueUsableSize = usableSizeTRY - totalAmount;
		    		assetServiceImpl.updateUsableSize(dtoOrderIU.getCustomer().getId(), "TRY", newValueUsableSize);
		    		Long newValueSize = size + dtoOrderIU.getSize();
		    		assetServiceImpl.updateSize(dtoOrderIU.getCustomer().getId(), dtoOrderIU.getAssetName(), newValueSize);
		    	}
		    } else {
		    	return null;
		    }
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
			dto.setOrderID(order.getId());
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
			
			if(!order.getStatus().getStatusName().equals("PENDING")) { 
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
	    OrderStatus orderStatus = OrderStatus.CANCELLED;
	    nStatus.setId(orderStatus.getCode());
		
		if(!pendingOrderList.isEmpty()) {
			for (Order order : pendingOrderList) {
				if(order.getId() == id) {
					Order updateOrder = order;
					updateOrder.setStatus(nStatus);
					orderRepository.save(updateOrder);
					
					if(updateOrder.getOrderSide().getSideName().equals("BUY")) { //Canceling a BUY
						Long amount = updateOrder.getPrice() * updateOrder.getSize();
						Long usableSizeTRY = assetServiceImpl.getAssetUsableSize(customerId, "TRY") + amount;
						if(usableSizeTRY != null)
							assetServiceImpl.updateUsableSize(customerId, "TRY", usableSizeTRY);
						else
							return "Customer does not have asset";
						Long size = assetServiceImpl.getAssetSize(customerId, updateOrder.getAsset().getAssetName()) - updateOrder.getSize();
						if(size != null)
							assetServiceImpl.updateSize(customerId, updateOrder.getAsset().getAssetName(), size);
						else
							return "Customer does not have asset";
						return "Buy order canceled succesfully";
					} else if(updateOrder.getOrderSide().getSideName().equals("SELL")) { //Canceling a SELL
						Long amount = updateOrder.getPrice() * updateOrder.getSize();
						Long sizeTRY = assetServiceImpl.getAssetSize(customerId, "TRY") - amount;
						if(sizeTRY != null)
						assetServiceImpl.updateSize(customerId, "TRY", sizeTRY);
						else
							return "Customer does not have asset";
						Long usableSize = assetServiceImpl.getAssetUsableSize(customerId, updateOrder.getAsset().getAssetName()) + updateOrder.getSize();
						if(usableSize != null)
							assetServiceImpl.updateUsableSize(customerId, updateOrder.getAsset().getAssetName(), usableSize);
						else
							return "Customer does not have asset";
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
