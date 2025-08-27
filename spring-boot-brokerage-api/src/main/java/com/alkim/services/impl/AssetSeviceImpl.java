package com.alkim.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkim.controller.repository.AssetRepository;
import com.alkim.entities.Asset;
import com.alkim.services.IAssetService;

@Service
public class AssetSeviceImpl implements IAssetService{
	
	@Autowired
	private AssetRepository assetRepository;
	
	@Override
	public Long getAssetUsableSize(Long customerId, String assetName) {
		// TODO Auto-generated method stub
		Optional<Asset> assetOpt = assetRepository.findByAssetNameAndCustomerId(assetName, customerId);

        return assetOpt.map(Asset::getUsableSize).orElse(null);
	}

}
