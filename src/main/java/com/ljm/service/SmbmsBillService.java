package com.ljm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsBill;

public interface SmbmsBillService {
	
	int getListSmbmsBillCount(String productName,Integer providerId, Integer isPayment);

	List<SmbmsBill> getListSmbmsBill(String productName,Integer providerId,Integer isPayment,Integer pageIndex,Integer pagaSize);

	int addSmbmsBill(SmbmsBill smbmsBill);

	SmbmsBill getSmbmsBillByid(Long id);

	int deleteSmbmsBill(Long id);

	int updateSmbmsBill(SmbmsBill smbmsBill);
}
