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
	 * 用户登录
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
		session.setAttribute("error", "登录名和密码不正确!");
		return "login";
	}
	/**
	 * 用户退出
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
	 * 分页查询用户列表
	 * @param queryname 用户姓名
	 * @param queryUserRole 用户角色
	 * @param pageIndex 当前页
	 * @param model 返回model
	 * @return 用户列表
	 */
	@RequestMapping("user.do")
	public String getUsers(String queryname,Integer queryUserRole,@RequestParam(defaultValue = "1")Integer pageIndex,Model model){
		//得到总行数
		int totalCount = smbmsUserService.getUserRows(queryname, queryUserRole);
		//计算
		int totalPageCount = totalCount%pagaSize==0?totalCount/pagaSize:totalCount/pagaSize+1;
		model.addAttribute("userList", smbmsUserService.getListSmbmsUser(queryname, queryUserRole, pageIndex, pagaSize));
		model.addAttribute("roleList", smbmsRoleService.getListSmbmsRoles());
		model.addAttribute("currentPageNo", pageIndex);//当前的页码
		model.addAttribute("totalPageCount", totalPageCount);//总页数
		model.addAttribute("totalCount", totalCount);//总行数
		model.addAttribute("queryUserName", queryname);
		model.addAttribute("queryUserRole", queryUserRole);
		return "jsp/userlist";
	}
	/**
	 * 跳转添加用户信息页面
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("goAdduser.do")
	public String goAddUser(@ModelAttribute("user") SmbmsUser user,Model model){
		model.addAttribute("roles", getMap(smbmsRoleService.getListSmbmsRoles()));
		return "jsp/adduser";
	}
	/**
	 * 添加用户信息
	 * @param user
	 * @param result
	 * @param session
	 * @param photos
	 * @param req
	 * @return
	 */
	@RequestMapping("addUser.do")
	public String addUser(@ModelAttribute("user") @Valid SmbmsUser user,BindingResult result,HttpSession session,@RequestParam("licenses") MultipartFile[] photos,
			HttpServletRequest req){
		if (!result.hasErrors()) {
			String path = req.getSession().getServletContext().getRealPath("licenses");
			List<String> errors = new ArrayList<String>();
			List<String> filenames = new ArrayList<String>();
			//1.检测
			for(MultipartFile photo:photos){
				String err = getErrInfo(photo);
				//2.上传
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
					//重定向到xxx.do
					return "redirect:user.do";
				}
			}
			req.setAttribute("errors", errors);
			req.setAttribute("filenames", filenames);
		}
		return "jsp/adduser";//回到页面
	}
	/**
	 * 拼接图片文件名称
	 * @param photo
	 * @param path
	 * @return
	 */
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
	
	//List转换Map
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
	/**
	 * 跳转用户详情页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("viewUser/{id}")
	public String viewUser(@PathVariable("id") Long id,Map<String,Object> model){
		model.put("user", smbmsUserService.getSmbmsUserByid(id));
		return "jsp/userview";
	}
	
	/**
	 * JSON
	 * 验证密码
	 * @param oldpassword
	 * @param session
	 * @return
	 */
	@RequestMapping("verifyPwd.do")
	@ResponseBody
	public Object verifyPwd(String oldpassword,HttpSession session){
		Map<String, String> map = new HashMap<String, String>();
		SmbmsUser smbmsUser = smbmsUserService.getSmbmsUserByid(((SmbmsUser) session.getAttribute("userSession")).getId());
		if (smbmsUser.getUserPassword().equals(oldpassword)) {
			map.put("result", "true");
		}else if(smbmsUser == null){
			map.put("result", "sessionerror");
		}else if(oldpassword.isEmpty()){
			map.put("result", "error");
		}else{
			map.put("result", "false");
		}
		return JSONArray.toJSONString(map);
	}
	/**
	 * 修改密码
	 * @param newpassword
	 * @param session
	 * @return
	 */
	@RequestMapping("modifyPwd.do")
	public String modifyPwd(@RequestParam("newpassword") String newpassword, HttpSession session){
		int resultConut = smbmsUserService.updatePwd(((SmbmsUser) session.getAttribute("userSession")).getId(), newpassword);
		if (resultConut > 0) {
			return "redirect:user.do";
		}
		return "jsp/pwdmodify";
	}
	/**
	 * 删除用户信息
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteUser.do")
	@ResponseBody
	public Object deleteUser(Long id){
		Map<String, String> map = new HashMap<String, String>();
		if (smbmsUserService.deleteSmbmsUser(id) > 0) {
			map.put("delResult", "true");
		}else {
			map.put("delResult", "false");
		}
		return JSONArray.toJSONString(map);
	}
	/**
	 * 跳转更新用户信息页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("goUpdateUser.do")
	public String goUpdateUser(Long id,Model model){
		model.addAttribute("user",smbmsUserService.getSmbmsUserByid(id));
		model.addAttribute("roleList", smbmsRoleService.getListSmbmsRoles());
		return "jsp/usermodify";
	}
	/**
	 * 修改用户信息
	 * @param id
	 * @param userName
	 * @param gender
	 * @param birthday
	 * @param phone
	 * @param address
	 * @param userRole
	 * @param photos
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping("updateUser.do")
	public String updateUser(@RequestParam("id") Long id,@RequestParam("userName") String userName,@RequestParam("gender") Integer gender,
			@RequestParam("birthday") Date birthday,@RequestParam("phone") String phone,@RequestParam("address") String address,
			@RequestParam("userRole") Integer userRole,@RequestParam("licenses") MultipartFile[] photos,
			HttpServletRequest req,HttpSession session){
		String path = req.getSession().getServletContext().getRealPath("licenses");
		SmbmsUser user = new SmbmsUser(id,userName,gender,birthday,phone,address,userRole);
		user.setModifyBy(id);
		user.setModifyDate(new Timestamp(System.currentTimeMillis()));
		user.setIdPicPath("licenses" + File.separator + upload(photos[0], path));
		user.setWorkPicPath("licenses" + File.separator + upload(photos[1], path));
		if (smbmsUserService.updateSmbmsUser(user) > 0) {
			return "redirect:user.do";
		}
		return "jsp/usermodify";
	}
}
