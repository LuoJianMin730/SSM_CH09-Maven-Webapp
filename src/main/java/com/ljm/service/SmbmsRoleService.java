package com.ljm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsRole;
import com.ljm.entity.SmbmsUser;

public interface SmbmsRoleService {
	
	List<SmbmsRole> getListSmbmsRoles();
	
}
