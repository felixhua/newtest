package com.hubao.tv.felix.webservice;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HubaoTvChannelManger {

	
	/***
	 * 创建流服务（开始直播）
	 */
	public void CreateChannel(HttpServletRequest request,
			HttpServletResponse response)  throws Exception;
	
	/***
	 * 创建流服务（直接操作直播比如关闭或者重启恢复等）
	 */
	public void  MangerChannel(HttpServletRequest request,
			HttpServletResponse response)throws Exception;
	
	
	/***
	 * 查询在线人数
	 */
	public void  QueryChannelConnectionUsernum (HttpServletRequest request,
			HttpServletResponse response)throws Exception;
	
	
	
	
}
