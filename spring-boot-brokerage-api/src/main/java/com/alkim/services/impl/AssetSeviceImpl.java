package com.alkim.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkim.controller.dto.DtoAsset;
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
	
	@Override
	public void updateUsableSize(Long customerId, String assetName, Long newValue) {
		
		Optional<Asset> assetOpt = assetRepository.findByAssetNameAndCustomerId(assetName, customerId);
		 if(assetOpt.isPresent()) {
	            Asset asset = assetOpt.get();
	            asset.setUsableSize(newValue);
	            assetRepository.save(asset);
	        }
	}

	@Override
	public List<DtoAsset> getAllAssetsByCustomerId(Long customerId) {
		
		List<DtoAsset> dtoList = new ArrayList<>();
		
		List<Asset> assetList = assetRepository.findByCustomerId(customerId);
		for (Asset asset : assetList) {
			DtoAsset dto = new DtoAsset();
			dto.setAssetName(asset.getAssetName());
			dto.setSize(asset.getSize());
			dto.setUsableSize(asset.getUsableSize());

			dtoList.add(dto);
		}
		return dtoList;
	}
	
	

}
