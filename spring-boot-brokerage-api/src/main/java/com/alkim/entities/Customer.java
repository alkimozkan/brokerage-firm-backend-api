package com.alkim.entities;

import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@OneToMany(mappedBy = "customer")
    private List<Asset> assets;
	

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
	
}
