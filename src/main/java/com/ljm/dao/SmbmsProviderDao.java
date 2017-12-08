package com.ljm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsProvider;

public interface SmbmsProviderDao {
	/**
	 * 查询所有供应商
	 * @param proCode 供应商编码
	 * @param proName 供应商名称
	 * @param pageIndex 页码
	 * @param pagaSize 行数
	 * @return 条件返回所有供应商
	 */
	List<SmbmsProvider> getListSmbmsProvider(@Param("proCode") String proCode,@Param("proName") String proName,
			@Param("pageIndex") Integer pageIndex,@Param("pagaSize") Integer pagaSize);
	/**
	 * 查询供应商行数
	 * @param proCode 供应商编码
	 * @param proName 供应商名称
	 * @return 供应商行数
	 */
	int getSmbmsProviderRows(@Param("proCode") String proCode,@Param("proName") String proName);
	/**
	 * 新增供应商
	 * @param smbmsProvider
	 * @return 受影响的行数
	 */
	int addSmbmsProvider(SmbmsProvider smbmsProvider);
	/**
	 * 按id查询供应商信息
	 * @param id
	 * @return
	 */
	SmbmsProvider getSmbmsProviderByid(Long id);
	/**
	 * 删除供应商信息
	 * @param id
	 * @return
	 */
	int deleteSmbmsProvider(Long id);
	/**
	 * 更新供应商信息
	 * @param smbmsProvider
	 * @return
	 */
	int updateSmbmsProvider(SmbmsProvider smbmsProvider);
	
}
