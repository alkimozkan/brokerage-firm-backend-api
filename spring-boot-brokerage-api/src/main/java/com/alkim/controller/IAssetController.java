package com.alkim.controller;

import java.util.List;

import com.alkim.controller.dto.DtoAsset;
import com.alkim.entities.Asset;

public interface IAssetController {
	
	public Long getAssetUsableSize(Long customerId, String assetName);
	
	public Long getAssetSize(Long customerId, String assetName);
	
	public List<DtoAsset> getAllAssetsByCustomerId(Long customerId);


}
