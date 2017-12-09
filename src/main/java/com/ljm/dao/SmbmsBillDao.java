package com.ljm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsBill;
import com.ljm.entity.SmbmsProvider;

public interface SmbmsBillDao {
	/**
	 * 查询行数
	 * @param productName
	 * @param providerId
	 * @param isPayment
	 * @return
	 */
	int getListSmbmsBillCount(@Param("productName") String productName,@Param("providerId") Integer providerId,
			@Param("isPayment") Integer isPayment);
	/**
	 * 查询所有订单
	 * @param productName
	 * @param providerId
	 * @param isPayment
	 * @param pageIndex
	 * @param pagaSize
	 * @return
	 */
	List<SmbmsBill> getListSmbmsBill(@Param("productName") String productName,@Param("providerId") Integer providerId,
			@Param("isPayment") Integer isPayment,@Param("pageIndex") Integer pageIndex,@Param("pagaSize") Integer pagaSize);
	/**
	 * 新增订单
	 * @param smbmsBill
	 * @return
	 */
	int addSmbmsBill(SmbmsBill smbmsBill);
	/**
	 * 按id查询订单信息
	 * @param id
	 * @return
	 */
	SmbmsBill getSmbmsBillByid(Long id);
	/**
	 * 删除订单信息
	 * @param id
	 * @return
	 */
	int deleteSmbmsBill(Long id);
	/**
	 * 更新订单信息
	 * @param smbmsBill
	 * @return
	 */
	int updateSmbmsBill(SmbmsBill smbmsBill);
}
