package com.alkim.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkim.controller.IAssetController;

import com.alkim.services.IAssetService;

@RestController
@RequestMapping("/rest/api/asset")
public class AssetControllerImpl implements IAssetController {
	
	@Autowired
	private IAssetService assetService;

	@GetMapping(path = "/{customerId}/{assetName}")
	@Override
	public Long getAssetUsableSize(@PathVariable(name = "customerId") Long customerId,@PathVariable(name = "assetName") String assetName) {
		// TODO Auto-generated method stub
		return assetService.getAssetUsableSize(customerId, assetName);
	}
}
