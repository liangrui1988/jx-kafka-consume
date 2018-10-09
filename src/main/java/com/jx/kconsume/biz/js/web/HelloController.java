package com.jx.kconsume.biz.js.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jx.kconsume.biz.bean.ResultLog;
import com.jx.kconsume.biz.js.model.UserMarkingBean;

@RestController
@RequestMapping("hello")
public class HelloController {

	@RequestMapping("/hi")
	public ResultLog userMarking(HttpServletRequest request, HttpServletResponse response,
			UserMarkingBean userMarking) {
		ResultLog result = new ResultLog();
		result.setMsg("world");
		return result;
	}
}
