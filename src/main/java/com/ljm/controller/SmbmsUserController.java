package com.ljm.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.ResultMap;
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
import com.ljm.entity.SmbmsRole;
import com.ljm.entity.SmbmsUser;
import com.ljm.service.SmbmsRoleService;
import com.ljm.service.SmbmsUserService;

@Controller
@RequestMapping("user")
public class SmbmsUserController {
	
	@Autowired
	private SmbmsUserService smbmsUserService;
	@Autowired
	private SmbmsRoleService smbmsRoleService;

	/**
	 * �û���¼
	 * @param smbmsUser
	 * @param session
	 * @return
	 */
	@RequestMapping("login.do")
	public String login(SmbmsUser smbmsUser,HttpSession session){
		SmbmsUser user = smbmsUserService.login(smbmsUser);
		if (user != null) {
			session.setAttribute("userSession", user);
			return "jsp/frame";
		}
		session.setAttribute("error", "��¼�������벻��ȷ!");
		return "login";
	}
	/**
	 * �û��˳�
	 * @param sessiong
	 * @return
	 */
	@RequestMapping("logout.do")
	public String logout(HttpSession sessiong){
		sessiong.removeAttribute("userSession");
		return "login";
	}
	
	private final int pagaSize = 5;
	/**
	 * ��ҳ��ѯ�û��б�
	 * @param queryname �û�����
	 * @param queryUserRole �û���ɫ
	 * @param pageIndex ��ǰҳ
	 * @param model ����model
	 * @return �û��б�
	 */
	@RequestMapping("user.do")
	public String getUsers(String queryname,Integer queryUserRole,@RequestParam(defaultValue = "1")Integer pageIndex,Model model){
		//�õ�������
		int totalCount = smbmsUserService.getUserRows(queryname, queryUserRole);
		//����
		int totalPageCount = totalCount%pagaSize==0?totalCount/pagaSize:totalCount/pagaSize+1;
		model.addAttribute("userList", smbmsUserService.getListSmbmsUser(queryname, queryUserRole, pageIndex, pagaSize));
		model.addAttribute("roleList", smbmsRoleService.getListSmbmsRoles());
		model.addAttribute("currentPageNo", pageIndex);//��ǰ��ҳ��
		model.addAttribute("totalPageCount", totalPageCount);//��ҳ��
		model.addAttribute("totalCount", totalCount);//������
		model.addAttribute("queryUserName", queryname);
		model.addAttribute("queryUserRole", queryUserRole);
		return "jsp/userlist";
	}
	
	@RequestMapping("goAdduser.do")
	public String goAddUser(@ModelAttribute("user") SmbmsUser user,Model model){
		model.addAttribute("roles", getMap(smbmsRoleService.getListSmbmsRoles()));
		return "jsp/adduser";
	}
	
	@RequestMapping("addUser.do")
	public String addUser(@ModelAttribute("user") @Valid SmbmsUser user,BindingResult result,HttpSession session,@RequestParam("licenses") MultipartFile[] photos,
			HttpServletRequest req){
		if (!result.hasErrors()) {
			String path = req.getSession().getServletContext().getRealPath("licenses");
			List<String> errors = new ArrayList<String>();
			List<String> filenames = new ArrayList<String>();
			//1.���
			for(MultipartFile photo:photos){
				String err = getErrInfo(photo);
				//2.�ϴ�
				if(err == null){
					String fileName = upload(photo, path);
					filenames.add(fileName);
				}else{
					errors.add(err);
				}
			}
			if(filenames.size() > 0){
				user.setCreatedBy(((SmbmsUser) session.getAttribute("userSession")).getId());
				user.setCreationDate(new Timestamp(System.currentTimeMillis()));
				user.setIdPicPath("licenses"+File.separator+filenames.get(0));
				user.setWorkPicPath("licenses"+File.separator+filenames.get(1));
				if (smbmsUserService.addSmbmsUser(user) > 0) {
					//�ض���xxx.do
					return "redirect:user.do";
				}
			}
			req.setAttribute("errors", errors);
			req.setAttribute("filenames", filenames);
		}
		return "jsp/adduser";//�ص�ҳ��
	}
	
	public String upload(MultipartFile photo,String path){
		String fileName = photo.getOriginalFilename();
		long time = System.currentTimeMillis();
		int randNumber = RandomUtils.nextInt(1000000);
		File dest = new File(path,time+"_"+randNumber+"_"+fileName);
		try {
			photo.transferTo(dest);
			return time+"_"+randNumber+"_"+fileName;
			//return dest.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 1.�ϴ����ļ��Ƿ�Ϊ��
	 * 2.�ϴ��ļ��Ĵ�С
	 * 3.�ϴ��ļ��ĸ�ʽ
	 * @param photo
	 * @return
	 */
	public String getErrInfo(MultipartFile photo){
		String fileName = photo.getOriginalFilename();
		String suf = FilenameUtils.getExtension(fileName);
		String err = null;
		if(photo.isEmpty()){
			err = String.format("%s���ϴ��ļ�����Ϊ��",fileName);
		}else if(photo.getSize()>500000){
			err = String.format("%s���ϴ��ļ���С���ܳ���500KB",fileName);
		}else if(!formats.contains(suf.toLowerCase())){
			err = String.format("%s���ϴ��ļ���ʽ����ȷ��֧�ֵĸ�ʽ��%s",fileName,formats);
		}
		return err;
	}
	//�����ϴ�ͼƬ��ʽ
	static List<String> formats = new ArrayList<String>();
	//��̬����
	static{
		formats.add("png");
		formats.add("jpg");
		formats.add("jpeg");
		formats.add("pneg");
	}
	
	//Listת��Map
	public Map<Long, String> getMap(List<SmbmsRole> roles){
		Map<Long, String> map = new HashMap<Long, String>();
		for (SmbmsRole role : roles) {
			map.put(role.getId(), role.getRoleName());
		}
		return map;
	}
	/**
	 * josn
	 * @param userCode
	 * @return
	 */
	@RequestMapping("userJosn.do")
	@ResponseBody
	public Object addUserJosn(String userCode){
		SmbmsUser smbmsUserByUserCode = smbmsUserService.getSmbmsUserByUserCode(userCode);
		return JSONArray.toJSONString(smbmsUserByUserCode);
	}
	
	@RequestMapping("viewUser/{id}")
	public String viewUser(@PathVariable("id") Long id,Map<String,Object> model){
		model.put("user", smbmsUserService.getSmbmsUserByid(id));
		return "jsp/userview";
	}
}
