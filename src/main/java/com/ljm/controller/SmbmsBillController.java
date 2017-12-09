package com.ljm.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.ljm.entity.SmbmsBill;
import com.ljm.entity.SmbmsUser;
import com.ljm.service.SmbmsBillService;
import com.ljm.service.SmbmsProviderService;

@Controller
@RequestMapping("bill")
public class SmbmsBillController {

	@Autowired
	private SmbmsBillService smbmsBillService;
	@Autowired
	private SmbmsProviderService smbmsProviderService;
	
	private final int pagaSize = 5;
	
	/**
	 * 分页查询订单信息
	 * @param queryProductName
	 * @param queryProviderId
	 * @param queryIsPayment
	 * @param pageIndex
	 * @param model
	 * @return
	 */
	@RequestMapping("getSmbmsBillList.do")
	public String getSmbmsBillList(String queryProductName,Integer queryProviderId,Integer queryIsPayment,@RequestParam(defaultValue = "1")Integer pageIndex,Model model){
		//得到总行数
		int totalCount = smbmsBillService.getListSmbmsBillCount(queryProductName, queryProviderId, queryIsPayment);
		//计算总页数
		int totalPageCount = totalCount % pagaSize==0?totalCount / pagaSize:totalCount / pagaSize +1;
		List<SmbmsBill> listSmbmsBill = smbmsBillService.getListSmbmsBill(queryProductName, queryProviderId, queryIsPayment, pageIndex, pagaSize);
		System.out.println(listSmbmsBill.size());
		model.addAttribute("billList", smbmsBillService.getListSmbmsBill(queryProductName, queryProviderId, queryIsPayment, pageIndex, pagaSize));
		model.addAttribute("providerList", smbmsProviderService.getListSmbmsProviderAll());
		
		model.addAttribute("totalCount", totalCount);//总行数
		model.addAttribute("currentPageNo", pageIndex);//当前页数
		model.addAttribute("totalPageCount", totalPageCount);//总页数
		
		model.addAttribute("queryProductName", queryProductName);
		model.addAttribute("queryProviderId", queryProviderId);
		model.addAttribute("queryIsPayment", queryIsPayment);
		return "jsp/billlist";
	}
	/**
	 * 跳转添加订单页面
	 * @return
	 */
	@RequestMapping("goAddSmbmsBill.do")
	public String goAddSmbmsBill(Model model){
		model.addAttribute("providerList",smbmsProviderService.getListSmbmsProviderAll());
		return "jsp/billadd";
	}
	/**
	 * 添加订单信息
	 * @param smbmsBill
	 * @param session
	 * @return
	 */
	@RequestMapping("addSmbmsBill.do")
	public String addSmbmsBill(SmbmsBill smbmsBill,HttpSession session){
		smbmsBill.setCreatedBy(((SmbmsUser) session.getAttribute("userSession")).getId());
		smbmsBill.setCreationDate(new Timestamp(System.currentTimeMillis()));
		if(smbmsBillService.addSmbmsBill(smbmsBill) > 0){
			return "redirect:getSmbmsBillList.do";
		}
		return "jsp/billadd";
	}
	/**
	 * 跳转订单详情页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("viewSmbmsBill/{id}")
	public String viewSmbmsBill(@PathVariable("id") Long id,Map<String,Object> model){
		model.put("bill", smbmsBillService.getSmbmsBillByid(id));
		return "jsp/billview";
	}
	/**
	 * 删除订单信息
	 * @param id
	 * @return
	 */
	@RequestMapping("deteleSmbmsBill.do")
	@ResponseBody
	public Object deteleSmbmsBill(Long id){
		Map<String, String> map = new HashMap<String, String>();
		if(smbmsBillService.deleteSmbmsBill(id) > 0){
			map.put("delResult", "true");
		}else{
			map.put("delResult", "false");
		}
		return JSONArray.toJSONString(map);
	}
	/**
	 * 跳转修改订单页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("goModifyBill/{id}")
	public String goModifyBill(@PathVariable("id") Long id,Model model){
		model.addAttribute("bill", smbmsBillService.getSmbmsBillByid(id));
		model.addAttribute("providerList", smbmsProviderService.getListSmbmsProviderAll());
		return "jsp/billmodify";
	}
	
	@RequestMapping("modifyBill.do")
	public String modifyBill(SmbmsBill smbmsBill,HttpSession session){
		smbmsBill.setModifyBy(((SmbmsUser) session.getAttribute("userSession")).getId());
		smbmsBill.setModifyDate(new Timestamp(System.currentTimeMillis()));
		if(smbmsBillService.updateSmbmsBill(smbmsBill) > 0){
			return "redirect:getSmbmsBillList.do";
		}
		return "jsp/billmodify";
	}
}
