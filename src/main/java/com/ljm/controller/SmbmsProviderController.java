package com.ljm.controller;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.ljm.entity.SmbmsProvider;
import com.ljm.entity.SmbmsUser;
import com.ljm.service.SmbmsProviderService;

@Controller
@RequestMapping("provider")
public class SmbmsProviderController {
	@Autowired
	private SmbmsProviderService smbmsProviderService;
	
	private final int pagaSize = 5;
	
	@RequestMapping("provider.do")
	public String getProviders(String queryProCode,String queryProName,@RequestParam(defaultValue = "1")Integer pageIndex,Model model){
		//得到总行数
		int totalCount = smbmsProviderService.getSmbmsProviderRows(queryProCode, queryProName);
		//计算总页数
		int totalPageCount = totalCount % pagaSize==0?totalCount / pagaSize:totalCount / pagaSize +1;
		model.addAttribute("providerList", smbmsProviderService.getListSmbmsProvider(queryProCode, queryProName, pageIndex, pagaSize));
		model.addAttribute("totalCount", totalCount);//总行数
		model.addAttribute("currentPageNo", pageIndex);//当前页数
		model.addAttribute("totalPageCount", totalPageCount);//总页数
		return "jsp/providerlist";
	}
	
	@RequestMapping("goAddProviders.do")
	public String goAddProviders(@ModelAttribute("provider") SmbmsProvider provider){
		return "jsp/provideradd";
	}
	/**
	 * 新增供应商
	 * 实现文件上传功能
	 * @param smbmsProvider
	 * @param session
	 * @return 
	 */
	@RequestMapping("addProviders.do")
	public String addProviders(@ModelAttribute("provider") @Valid SmbmsProvider provider,BindingResult result,HttpSession session,@RequestParam("licenses") MultipartFile[] photos,
			HttpServletRequest req){
		if (!result.hasErrors()) {
			String path = req.getSession().getServletContext().getRealPath("licenses");
			List<String> errors = new ArrayList<String>();
			List<String> filenames = new ArrayList<String>();
			//1.检测
			for (MultipartFile photo : photos) {
				String err = getErrInfo(photo);
				//2.上传
				if (err == null) {
					String fileName = upload(photo, path);
					filenames.add(fileName);
				}else{
					errors.add(err);
				}
			}
			if (filenames.size() > 0) {
				provider.setCreatedBy(((SmbmsUser) session.getAttribute("userSession")).getId());
				provider.setCreationDate(new Timestamp(System.currentTimeMillis()));
				provider.setLicense("licenses" + File.separator + filenames.get(0));
				provider.setFrontCode("licenses" + File.separator + filenames.get(1));
				provider.setSideCode("licenses" + File.separator + filenames.get(2));
				if (smbmsProviderService.addSmbmsProvider(provider) > 0) {
					return "redirect:provider.do";
				}
			}
			req.setAttribute("errors", errors);
			req.setAttribute("filenames", filenames);
		}
		return "jsp/provideradd";//回到页面
	}
	
	public String upload(MultipartFile photo,String path){
		String fileName = photo.getOriginalFilename();
		long time = System.currentTimeMillis();
		int randNumber = RandomUtils.nextInt(1000000);
		File dest = new File(path,time+"_"+randNumber+"_"+fileName);
		try {
			photo.transferTo(dest);
			return time+"_"+randNumber+"_"+fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 1.上传的文件是否为空
	 * 2.上传文件的大小
	 * 3.上传文件的格式
	 * @param photo
	 * @return
	 */
	public String getErrInfo(MultipartFile photo){
		String fileName = photo.getOriginalFilename();
		String suf = FilenameUtils.getExtension(fileName);
		String err = null;
		if(photo.isEmpty()){
			err = String.format("%s：上传文件不能为空",fileName);
		}else if(photo.getSize()>500000){
			err = String.format("%s：上传文件大小不能超过500KB",fileName);
		}else if(!formats.contains(suf.toLowerCase())){
			err = String.format("%s：上传文件格式不正确，支持的格式：%s",fileName,formats);
		}
		return err;
	}
	//声明上传图片格式
	static List<String> formats = new ArrayList<String>();
	//静态代码
	static{
		formats.add("png");
		formats.add("jpg");
		formats.add("jpeg");
		formats.add("pneg");
	}
	@RequestMapping("viewProvider/{id}")
	public String viewProvider(@PathVariable("id") Long id,Model model){
		model.addAttribute("provider", smbmsProviderService.getSmbmsProviderByid(id));
		return "jsp/providerview";
	}
	
	/**
	 * 删除供应商
	 * @param id
	 * @return
	 */
	@RequestMapping("delProvider.do")
	@ResponseBody
	public Object delProvider(Long id){
		Map<String, String> map = new HashMap<String, String>();
		int resultConut = smbmsProviderService.deleteSmbmsProvider(id);
		if (resultConut > 0) {
			map.put("delResult", "true");
			return JSONArray.toJSONString(map);
		}else{
			map.put("delResult", "notexist");
			return JSONArray.toJSONString(map);
		}
	}
	@RequestMapping("goModifyProvider.do")
	public String modifyProvider(Long id,Model model){
		model.addAttribute("provider", smbmsProviderService.getSmbmsProviderByid(id));
		return "jsp/providermodify";
	}
	@RequestMapping("modifyProvider.do")
	public String modifyProvider(SmbmsProvider smbmsProvider){
		if (smbmsProviderService.updateSmbmsProvider(smbmsProvider) > 0) {
			return "redirect:provider.do";
		}
		return "jsp/providermodify";
	}
}
