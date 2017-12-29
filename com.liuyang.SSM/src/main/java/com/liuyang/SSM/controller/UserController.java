package com.liuyang.SSM.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liuyang.SSM.entity.ResultModel;
import com.liuyang.SSM.entity.UserEntity;
import com.liuyang.SSM.service.UserService;


@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Resource
	private UserService userService;
	
	@RequestMapping(value = "/insetUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel insetUserInfo(@RequestBody List<UserEntity> list){
		ResultModel model = new ResultModel();
		int result = userService.insertUserEntity(list);
		switch (result) {
		case 0:
			model.setCode("200");
			model.setStatus("-1");
			model.setMsg("插入失败");
			break;
		case 1:
			model.setCode("200");
			model.setStatus("0");
			model.setMsg("插入成功");
			break;
		default:
			break;
		}
		return model;
	}
}
