package com.alkim.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "asset",
	   uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "asset_name"})
	  )
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Asset {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "asset")
    private List<Order> orders;
	
	@Column(name = "asset_name")
	private String assetName;
	
	@Column(name = "size")
	private Long size;
	
	@Column(name = "usable_size")
	private Long usableSize;
}
