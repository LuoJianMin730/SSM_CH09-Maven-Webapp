package com.ljm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsUser;

public interface SmbmsUserDao {
	/**
	 * 登录
	 * @param smbmsUser
	 * @return
	 */
	SmbmsUser login(SmbmsUser smbmsUser);
	/**
	 * 查询所有用户
	 * @return
	 */
	List<SmbmsUser> getListSmbmsUser(@Param("userName") String userName,@Param("userRole") Integer userRole,
			@Param("pageIndex") Integer pageIndex,@Param("pagaSize") Integer pagaSize);
	/** 
	 * 查询用户的行数
	 * @param userName
	 * @param userRole
	 * @return
	 */
	int getUserRows(@Param("userName") String userName,@Param("userRole") Integer userRole);
	/**
	 * 用于Josn 
	 * 按userCode查询用户信息
	 * @return 用户信息
	 */
	SmbmsUser getSmbmsUserByUserCode(@Param("userCode") String userCode);
	/**
	 * 新增用户
	 * @param smbmsUser
	 * @return 受影响的行数
	 */
	int addSmbmsUser(SmbmsUser smbmsUser);
	/**
	 * 按id查询用户信息
	 * @param id
	 * @return
	 */
	SmbmsUser getSmbmsUserByid(Long id);
}