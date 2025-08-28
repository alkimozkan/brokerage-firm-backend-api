package com.alkim.controller.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alkim.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	List<Order> findByCustomerIdAndCreateDateBetween(Long customerId, Date startDate, Date endDate);
	
	List<Order> findByCustomerId(Long customerId);
	
}
