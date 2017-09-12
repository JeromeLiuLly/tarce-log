package com.candao.trace.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.candao.trace.demo.api.bean.DemoUser;
import com.candao.trace.demo.service.DemoUserService;
import com.candao.trace.framework.bean.request.ReqExternalData;
import com.candao.trace.framework.bean.response.RspData;
import com.candao.trace.framework.controller.BaseController;

/**
 * 第三方请求Controller 示例 <br/>
 * 1. 继承至dms-framework中的BaseController <br/>
 * 2. 所有@RequestMapping 在该类中定义 <br/>
 * 3. 请求数据统一为：@RequestBody ReqExternalData reqData (父类提供快速方法提取参数), POST形式<br/>
 * 4. 响应数据统一为：RspData对象(可调用父类方法快速构建该对象) <br/>
 * 5. 请求路径为/external/xx形式
 * 
 */
@RestController
@RequestMapping(value = "/external/demo")
public class DemoExternalController extends BaseController {

	@Autowired
	DemoUserService demoUserService;

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public RspData getUser(@RequestBody ReqExternalData reqData) {
		// TODO 参数验证
		int uid = reqData.getAsInt("uid");
		DemoUser user = demoUserService.getUser(uid);
		return retSuccessRspData(user);
	}
}
