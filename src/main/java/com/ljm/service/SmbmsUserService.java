package com.ljm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsUser;

public interface SmbmsUserService {

	SmbmsUser login(SmbmsUser smbmsUser);
	
	List<SmbmsUser> getListSmbmsUser(String userName,Integer userRole,Integer pageIndex,Integer pagaSize);
	
	int getUserRows(String userName,Integer userRole);
	
	SmbmsUser getSmbmsUserByUserCode(String userCode);
	
	int addSmbmsUser(SmbmsUser smbmsUser);
	
	SmbmsUser getSmbmsUserByid(Long id);
	
	int updatePwd(Long id,String userPassword);
	
	int deleteSmbmsUser(Long id);
	
	int updateSmbmsUser(SmbmsUser user);
}
