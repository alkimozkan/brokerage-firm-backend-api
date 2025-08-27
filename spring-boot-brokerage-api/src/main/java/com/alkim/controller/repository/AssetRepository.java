package com.alkim.controller.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alkim.entities.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer>{
	
    Optional<Asset> findByAssetNameAndCustomerId(String assetName, Long customerId);


}
