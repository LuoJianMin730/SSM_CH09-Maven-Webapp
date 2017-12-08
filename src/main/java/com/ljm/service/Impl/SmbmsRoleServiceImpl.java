package com.ljm.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljm.dao.SmbmsRoleDao;
import com.ljm.entity.SmbmsRole;
import com.ljm.service.SmbmsRoleService;
@Service
public class SmbmsRoleServiceImpl implements SmbmsRoleService {
	@Autowired
	private SmbmsRoleDao smbmsRoleDao;

	public List<SmbmsRole> getListSmbmsRoles() {
		return smbmsRoleDao.getListSmbmsRoles();
	}

}
