package com.ljm.dao;

import java.util.List;

import com.ljm.entity.SmbmsRole;

public interface SmbmsRoleDao {
	/**
	 * 查询所有用户角色
	 * @return
	 */
	List<SmbmsRole> getListSmbmsRoles();
}