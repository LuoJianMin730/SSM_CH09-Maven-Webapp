package com.ljm.service.Impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ljm.dao.SmbmsUserDao;
import com.ljm.entity.SmbmsUser;
import com.ljm.service.SmbmsUserService;

@Service
public class SmbmsUserServiceImpl implements SmbmsUserService {
	@Autowired
	private SmbmsUserDao smbmsUserDao;

	public SmbmsUser login(SmbmsUser smbmsUser) {
		return smbmsUserDao.login(smbmsUser);
	}

	public List<SmbmsUser> getListSmbmsUser(String userName, Integer userRole,
			Integer pageIndex, Integer pagaSize) {
		return smbmsUserDao.getListSmbmsUser(userName, userRole, (pageIndex-1)*pagaSize, pagaSize);
	}

	public int getUserRows(@Param("userName") String userName,
			@Param("userRole") Integer userRole) {
		return smbmsUserDao.getUserRows(userName, userRole);
	}

	public SmbmsUser getSmbmsUserByUserCode(String userCode) {
		return smbmsUserDao.getSmbmsUserByUserCode(userCode);
	}

	public int addSmbmsUser(SmbmsUser smbmsUser) {
		return smbmsUserDao.addSmbmsUser(smbmsUser);
	}

	public SmbmsUser getSmbmsUserByid(Long id) {
		return smbmsUserDao.getSmbmsUserByid(id);
	}
}
