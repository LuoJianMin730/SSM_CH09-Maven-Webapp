package com.ljm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ljm.entity.SmbmsProvider;

public interface SmbmsProviderDao {
	/**
	 * ��ѯ���й�Ӧ��
	 * @param proCode ��Ӧ�̱���
	 * @param proName ��Ӧ������
	 * @param pageIndex ҳ��
	 * @param pagaSize ����
	 * @return �����������й�Ӧ��
	 */
	List<SmbmsProvider> getListSmbmsProvider(@Param("proCode") String proCode,@Param("proName") String proName,
			@Param("pageIndex") Integer pageIndex,@Param("pagaSize") Integer pagaSize);
	/**
	 * ��ѯ��Ӧ������
	 * @param proCode ��Ӧ�̱���
	 * @param proName ��Ӧ������
	 * @return ��Ӧ������
	 */
	int getSmbmsProviderRows(@Param("proCode") String proCode,@Param("proName") String proName);
	/**
	 * ������Ӧ��
	 * @param smbmsProvider
	 * @return ��Ӱ�������
	 */
	int addSmbmsProvider(SmbmsProvider smbmsProvider);
	/**
	 * ��id��ѯ��Ӧ����Ϣ
	 * @param id
	 * @return
	 */
	SmbmsProvider getSmbmsProviderByid(Long id);
	/**
	 * ɾ����Ӧ����Ϣ
	 * @param id
	 * @return
	 */
	int deleteSmbmsProvider(Long id);
	/**
	 * ���¹�Ӧ����Ϣ
	 * @param smbmsProvider
	 * @return
	 */
	int updateSmbmsProvider(SmbmsProvider smbmsProvider);
	
}
