package com.jx.kconsume.biz.js.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jx.kconsume.biz.bean.ResultLog;
import com.jx.kconsume.biz.js.model.UserMarkingBean;
import com.jx.kconsume.biz.js.service.UserMarkingService;
import com.jx.kconsume.biz.utils.IdCreater;
import com.jx.kconsume.util.IPUtil;

/**
 * 用户标识相关
 * 
 * @date 2018-09-09
 * @author liangrui
 * @version 1.0.0
 *
 */
//@RestController
//@RequestMapping("init")
public class UserMarkingController {
	Logger logger = LoggerFactory.getLogger(UserMarkingController.class);

	@Value("${sys.env}")
	private String env = "prod";

	@Autowired
	@Qualifier("phoenixTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UserMarkingService userMarkingService;

	/**
	 * 日志接收，并发送到kafka
	 * 
	 * @param data
	 * @return
	 */
//	@RequestMapping("/marking")
	public ResultLog userMarking(HttpServletRequest request, HttpServletResponse response,
			UserMarkingBean userMarking) {
		ResultLog result = new ResultLog();
		// 跨域问题
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		// response.addHeader("Access-Control-Max-Age", "1800");// 30 min
		logger.info("hello_initMarking_src userMarking={}", userMarking);
//		if(true) {
//			return result;
//		}
		try {
			// FIXME: 验证user token,先省略
			// if (false) {
			// result.setCode(-5);
			// result.setMsg("用户验证失败");
			// }
			if (StringUtils.isBlank(userMarking.getUser_project())) {
				result.setCode(-1);
				result.setMsg("绑定失败！项目标识不能为空");
				return result;
			}
			if (!"qr".equals(userMarking.getUser_project()) && !"joyxuan_mall".equals(userMarking.getUser_project())
					&& !"space".equals(userMarking.getUser_project())) {
				result.setCode(-1);
				result.setMsg("绑定失败！项目标识无效！");
				return result;
			}
			String ip = IPUtil.getIPFromRequest(request);

			// 如果user_id=-1的情况下，mark_user_id永远只有一个，对应标识
			// 如果user_id!=-1的情况，需要取出 mark_user_id,来记录起来
			long user_marking_id = 0;
			// 是否有userID
			if (StringUtils.isBlank(userMarking.getUser_id())) {// 没有登录状态
				userMarking.setUser_id("-1");
				// 直接生成一个标识
			}
			if (!"-1".equals(userMarking.getUser_id())) {
				// 如果有用户，去找db查出原有的markID
				user_marking_id = userMarkingService.getUserMarkingIdByUserId(userMarking.getUser_id(),
						userMarking.getUser_project());
			}
			// 就否有客户端标识 cookie
			if (StringUtils.isBlank(userMarking.getClient_id()) || "-1".equals(userMarking.getClient_id())) {
				String clientID = IdCreater.getUUID("web-");
				userMarking.setClient_id(clientID);
			} else {
				// 如果有设备标识，查询出来
				if (user_marking_id <= 0) {
					user_marking_id = userMarkingService.getUserMarkingIdByMarking(userMarking.getClient_id(),
							userMarking.getUser_project());
				}
			}
			if (user_marking_id <= 0) {// 如果为空，再分配置一个
				user_marking_id = userMarkingService.getUserMarkingId();
			}
			// 写入DB，hbase
			userMarking.setMark_user_id(user_marking_id);
			int count = userMarkingService.upsert(userMarking, ip);
			if (count <= 0) {// 不成功
				result.setCode(-2);// 插入失败
			}
			result.setData(userMarking);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("userMarking异常,userMarking=%s==end", userMarking), e);
			result.setCode(-1);
			result.setMsg("系统异常");
		}
		return result;
	}

}
