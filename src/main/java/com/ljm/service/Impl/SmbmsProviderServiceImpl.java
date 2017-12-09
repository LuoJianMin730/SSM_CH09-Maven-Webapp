package com.ljm.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljm.dao.SmbmsProviderDao;
import com.ljm.entity.SmbmsProvider;
import com.ljm.service.SmbmsProviderService;
@Service
public class SmbmsProviderServiceImpl implements SmbmsProviderService {
	
	@Autowired
	private SmbmsProviderDao smbmsProviderDao;

	public List<SmbmsProvider> getListSmbmsProvider(String proCode,
			String proName, Integer pageIndex, Integer pagaSize) {
		return smbmsProviderDao.getListSmbmsProvider(proCode, proName, (pageIndex-1)*pagaSize, pagaSize);
	}

	public int getSmbmsProviderRows(String proCode, String proName) {
		return smbmsProviderDao.getSmbmsProviderRows(proCode, proName);
	}

	public int addSmbmsProvider(SmbmsProvider smbmsProvider) {
		return smbmsProviderDao.addSmbmsProvider(smbmsProvider);
	}

	public SmbmsProvider getSmbmsProviderByid(Long id) {
		return smbmsProviderDao.getSmbmsProviderByid(id);
	}

	public int deleteSmbmsProvider(Long id) {
		return smbmsProviderDao.deleteSmbmsProvider(id);
	}

	public int updateSmbmsProvider(SmbmsProvider smbmsProvider) {
		return smbmsProviderDao.updateSmbmsProvider(smbmsProvider);
	}

	public List<SmbmsProvider> getListSmbmsProviderAll() {
		return smbmsProviderDao.getListSmbmsProviderAll();
	}
}
