package com.alkim.spring_data_jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alkim.controller.dto.DtoAsset;
import com.alkim.controller.dto.DtoOrder;
import com.alkim.controller.dto.DtoOrderIU;
import com.alkim.controller.repository.OrderRepository;
import com.alkim.entities.Asset;
import com.alkim.entities.Customer;
import com.alkim.entities.Order;
import com.alkim.entities.Side;
import com.alkim.entities.Status;
import com.alkim.services.IAssetService;
import com.alkim.services.impl.AssetSeviceImpl;
import com.alkim.services.impl.OrderServiceImpl;
import com.alkim.starter.SpringDataJpaApplication;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {SpringDataJpaApplication.class})
class SpringDataJpaApplicationTests {
	
	@Autowired
	private IAssetService assetService;
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	
	@Test
	public void testGetAssetUsableSize() {
		Long usableSize = assetService.getAssetUsableSize(1L, "TRY");
		if(usableSize != null) {
			System.out.println("customer "+1L + " TRY "+ "usable size: " +usableSize);
		}
		assertEquals(200000L, usableSize);
	}
	
	@Test
	public void testGetAssetSize() {
		Long assetSize = assetService.getAssetSize(1L, "TRY");
		if(assetSize != null) {
			System.out.println("customer "+1L + " TRY "+ "size: " +assetSize);
		}
		assertEquals(200000L, assetSize);
	}
	
	@Test
	public void testGetAllAssetsByCustomerId() {
		List<DtoAsset> dtoAssets = assetService.getAllAssetsByCustomerId(1L);
		for (DtoAsset dtoAsset : dtoAssets) {
			if(dtoAsset.getAssetName().equals("TRY")) {
				assertEquals(200000L, dtoAsset.getSize());
				assertEquals(200000L, dtoAsset.getUsableSize());
			} else if(dtoAsset.getAssetName().equals("USD")) {
				assertEquals(1000L, dtoAsset.getSize());
				assertEquals(1000L, dtoAsset.getUsableSize());
			}
		}
	}
	
	@Test
	public void testCreateOrder() {
		DtoOrderIU dtoOrderIU = new DtoOrderIU();
		Date now = new Date();
		Asset asset = new Asset();
		asset.setId(2L);
		Customer customer = new Customer();
		customer.setId(2L);
		Side side = new Side();
		side.setId(2L);
		side.setSideName("BUY");
		Status status = new Status();
		status.setId(1L);
		
		dtoOrderIU.setSize(100L);
		dtoOrderIU.setPrice(40L);
		dtoOrderIU.setCreateDate(now);
		dtoOrderIU.setAssetName("USD");
		dtoOrderIU.setAsset(asset);
		dtoOrderIU.setCustomer(customer);
		dtoOrderIU.setOrderSide(side);
		dtoOrderIU.setStatus(status);
		
		DtoOrder order = orderServiceImpl.createOrder(dtoOrderIU);
		
		assertEquals(100L, order.getSize());
		assertEquals(40L, order.getPrice());
		
	}
	@Test
	public void testGetOrderByIdAndDate() {
		Date now = new Date();
		Date specificDate = new Date(125, 0, 1);
		List<DtoOrder> dtoOrders = orderServiceImpl.getOrderByIdAndDate(1L, specificDate, now);
		
		assertNotNull(dtoOrders);
	}
	
	@Test
	public void testDeleteOrderById() {
		
		String result = orderServiceImpl.deleteOrderById(1L, 3L);
		
		List<String> expected = Arrays.asList("Buy order canceled succesfully", "Sell order canceled succesfully");
		assertTrue(expected.contains(result));
		
	}
	
	

}
