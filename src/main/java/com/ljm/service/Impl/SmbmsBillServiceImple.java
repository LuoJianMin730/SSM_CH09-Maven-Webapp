package com.ljm.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljm.dao.SmbmsBillDao;
import com.ljm.entity.SmbmsBill;
import com.ljm.service.SmbmsBillService;
@Service
public class SmbmsBillServiceImple implements SmbmsBillService {
	
	@Autowired
	private SmbmsBillDao smbmsBillDao;

	public List<SmbmsBill> getListSmbmsBill(String productName,
			Integer providerId, Integer isPayment, Integer pageIndex,
			Integer pagaSize) {
		return smbmsBillDao.getListSmbmsBill(productName, providerId, isPayment, (pageIndex-1)*pagaSize, pagaSize);
	}

	public int getListSmbmsBillCount(String productName, Integer providerId,
			Integer isPayment) {
		return smbmsBillDao.getListSmbmsBillCount(productName, providerId, isPayment);
	}

	public int addSmbmsBill(SmbmsBill smbmsBill) {
		return smbmsBillDao.addSmbmsBill(smbmsBill);
	}

	public SmbmsBill getSmbmsBillByid(Long id) {
		return smbmsBillDao.getSmbmsBillByid(id);
	}

	public int deleteSmbmsBill(Long id) {
		return smbmsBillDao.deleteSmbmsBill(id);
	}

	public int updateSmbmsBill(SmbmsBill smbmsBill) {
		return smbmsBillDao.updateSmbmsBill(smbmsBill);
	}
}
