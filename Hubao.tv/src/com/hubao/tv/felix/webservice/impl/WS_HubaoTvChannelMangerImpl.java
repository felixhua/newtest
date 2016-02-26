package com.hubao.tv.felix.webservice.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.hubao.tv.felix.dbhelper.PropertiesCnfig;
import com.hubao.tv.felix.webservice.HubaoTvChannelManger;
import com.hubao.tv.felix.webservice.impl.helper.WS_HubaoTvChannelMangerImplHelper;

public class WS_HubaoTvChannelMangerImpl implements HubaoTvChannelManger {

	private static Logger logger = Logger.getLogger(WS_HubaoTvChannelMangerImpl.class);

	@Override
	public void CreateChannel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Map<String, Object> msgmap = new HashMap<String, Object>();
		Map<String, Object> paramsmap = WS_HubaoTvChannelMangerImplHelper.ValidationCreateChannelPramse(request, response);
		if (paramsmap != null) {
			if (WS_HubaoTvChannelMangerImplHelper.ValidationCreateChannelSign(paramsmap)) {
				PropertiesCnfig pro = new PropertiesCnfig();
				Properties pros = new Properties();
				try {
					pros = pro.LoadPropertie();
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("WS_HubaoTvChannelMangerImpl类中CreateChannel方法读取配置出现异常" + e);
					msgmap.put("status", 1);
					msgmap.put("msg", "config read exception");
					JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
					response.getWriter().print(jsonObject);
				}

				String WangsuRtmpPushUrl = pros.getProperty("WangsuRtmpPushUrl");
				// 生成的频道号
				String Channeid = msgmap.get("userid").toString() + System.currentTimeMillis();
				String RtmpPushUrl = String.format(WangsuRtmpPushUrl, Channeid);
				msgmap.put("status", 0);
				msgmap.put("msg", "O(∩_∩)O success!!!");
				msgmap.put("PushUrl", RtmpPushUrl);
				JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
				response.getWriter().print(jsonObject);

			} else {
				msgmap.put("status", 1);
				msgmap.put("msg", "Sign error");
				JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
				response.getWriter().print(jsonObject);
			}
		} else {
			msgmap.put("status", 1);
			msgmap.put("msg", "Parameter is null or is not complete");
			JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
			response.getWriter().print(jsonObject);
		}

	}

	@Override
	public void MangerChannel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Map<String, Object> msgmap = new HashMap<String, Object>();
		Map<String, Object> paramsmap = WS_HubaoTvChannelMangerImplHelper.ValidationMangerChannelPramse(request, response);

		if (paramsmap != null) {
			if (WS_HubaoTvChannelMangerImplHelper.ValidationMangerChannelSign(paramsmap)) {

				String wsreturn = WS_HubaoTvChannelMangerImplHelper.MangerChannelRequestAcion(paramsmap);
				if (wsreturn.contains("success")) {

					if (logger.isDebugEnabled()) {
						logger.debug("WS_HubaoTvChannelMangerImpl类中MangerChannel方法请求返回：" + wsreturn);
					}
					msgmap.put("status", 0);
					msgmap.put("msg", "O(∩_∩)O success!!!");
					JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
					response.getWriter().print(jsonObject);

				} else {
					logger.error("WS_HubaoTvChannelMangerImpl类中MangerChannel方法请求返回：" + wsreturn);
					msgmap.put("status", 1);
					msgmap.put("msg", "request WS  API  error ");
					JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
					response.getWriter().print(jsonObject);
				}

			} else {
				msgmap.put("status", 1);
				msgmap.put("msg", "Sign error");
				JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
				response.getWriter().print(jsonObject);
			}
		} else {
			msgmap.put("status", 1);
			msgmap.put("msg", "Parameter is null or is not complete");
			JSONObject jsonObject = JSONObject.fromObject(msgmap.toString());
			response.getWriter().print(jsonObject);
		}

	}

	@Override
	public void QueryChannelConnectionUsernum(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

}
