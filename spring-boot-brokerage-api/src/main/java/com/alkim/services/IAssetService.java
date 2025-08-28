package com.alkim.services;

import java.util.List;

import com.alkim.controller.dto.DtoAsset;

public interface IAssetService {
	
	public Long getAssetUsableSize(Long customerId, String assetName);
	
	public void updateUsableSize(Long customerId, String assetName, Long newValue);
	
	public List<DtoAsset> getAllAssetsByCustomerId(Long customerId);


}
