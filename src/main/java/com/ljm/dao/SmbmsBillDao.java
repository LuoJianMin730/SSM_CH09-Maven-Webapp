package com.ljm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsBill;
import com.ljm.entity.SmbmsProvider;

public interface SmbmsBillDao {
	/**
	 * ��ѯ����
	 * @param productName
	 * @param providerId
	 * @param isPayment
	 * @return
	 */
	int getListSmbmsBillCount(@Param("productName") String productName,@Param("providerId") Integer providerId,
			@Param("isPayment") Integer isPayment);
	/**
	 * ��ѯ���ж���
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
	 * ��������
	 * @param smbmsBill
	 * @return
	 */
	int addSmbmsBill(SmbmsBill smbmsBill);
	/**
	 * ��id��ѯ������Ϣ
	 * @param id
	 * @return
	 */
	SmbmsBill getSmbmsBillByid(Long id);
	/**
	 * ɾ��������Ϣ
	 * @param id
	 * @return
	 */
	int deleteSmbmsBill(Long id);
	/**
	 * ���¶�����Ϣ
	 * @param smbmsBill
	 * @return
	 */
	int updateSmbmsBill(SmbmsBill smbmsBill);
}
