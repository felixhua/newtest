package com.hubao.tv.felix.webservice.impl.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hubao.tv.felix.dbhelper.PropertiesCnfig;
import com.hubao.tv.felix.httphelper.HttpHelper;
import com.hubao.tv.felix.httphelper.HttpWebClient;

public class WS_HubaoTvChannelMangerImplHelper {

	private static Logger logger = Logger.getLogger(WS_HubaoTvChannelMangerImplHelper.class);

	/***
	 * CreateChannel方法使用的验证参数的方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> ValidationCreateChannelPramse(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> pramsMap = new HashMap<String, Object>();
		try {
			// 主播的平台编号
			String userid = request.getParameter("userid") == null ? "" : request.getParameter("userid").toString();
			// 随机的密码指示用来验证建议用时间戳
			String password = request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			// 频道的描述
			String channeldescribe = request.getParameter("channeldescribe") == null ? "" : HttpHelper.Decode(request.getParameter("channeldescribe").toString());
			String sign = request.getParameter("password") == null ? "" : request.getParameter("password").toString();

			if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(userid) || StringUtils.isEmpty(sign)) {
				logger.error("WS_HubaoTvChannelMangerImplHelper类中验证ValidationCreateChannelPramse方法发现参数不全：userid=" + userid + "password=" + password);
				return null;
			} else {
				pramsMap.put("userid", userid);
				pramsMap.put("password", password);
				pramsMap.put("channeldescribe", channeldescribe);
				pramsMap.put("sign", sign);

				return pramsMap;
			}

		} catch (Exception e) {
			// TODO: handle exceptionF
			logger.error("WS_HubaoTvChannelMangerImplHelper类中验证ValidationCreateChannelPramse方法出现异常：" + e);
			return null;
		}

	}

	/***
	 * CreateChannel方法使用的验证签名的方法
	 * 
	 * @param pramsmap
	 * @return
	 */
	public static boolean ValidationCreateChannelSign(Map<String, Object> pramsmap) {
		PropertiesCnfig pro = new PropertiesCnfig();
		Properties pros = pro.LoadPropertie();
		String key = pros.getProperty("WangsuAppKey");
		String userid = pramsmap.get("userid").toString();
		String password = pramsmap.get("password").toString();
		String sign = pramsmap.get("sign").toString();
		String weSign = DigestUtils.md5Hex(userid + password + key);

		if (weSign.equalsIgnoreCase(sign)) {
			return true;
		} else {
			logger.error("WS_HubaoTvChannelMangerImplHelper类中验证ValidationCreateChannelSign方法验证签名错误：我方签名：" + weSign + "对方签名：" + sign);
			return false;

		}

	}

	/***
	 * MangerChannel方法使用的验证参数的方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static Map<String, Object> ValidationMangerChannelPramse(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> pramsMap = new HashMap<String, Object>();
		try {
			// 主播的平台编号
			String userid = request.getParameter("userid") == null ? "" : request.getParameter("userid").toString();
			// 随机的密码指示用来验证建议用时间戳
			String password = request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			// 简要说明下关闭的原因
			String closeldescribe = request.getParameter("closeldescribe") == null ? "" : HttpHelper.Decode(request.getParameter("closeldescribe").toString());
			// 要执行的操作（比如是断开流连接还是恢复{ forbid, resume}）
			String operation = request.getParameter("operation") == null ? "" : HttpHelper.Decode(request.getParameter("operation").toString());
			// 要操作的流类型是播放流还是推流{play，publish}
			String operationtchanneltype = request.getParameter("operationtchanneltype") == null ? "" : HttpHelper.Decode(request.getParameter("operationtchanneltype").toString());
			String sign = request.getParameter("password") == null ? "" : request.getParameter("password").toString();

			if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(userid) || StringUtils.isEmpty(sign)) {
				logger.error("WS_HubaoTvChannelMangerImplHelper类中验证ValidationCreateChannelPramse方法发现参数不全：userid=" + userid + "password=" + password);
				return null;
			} else {
				pramsMap.put("userid", userid);
				pramsMap.put("password", password);
				pramsMap.put("closeldescribe", closeldescribe);
				pramsMap.put("operation", operation);
				pramsMap.put("operationtchanneltype", operationtchanneltype);
				pramsMap.put("sign", sign);

				return pramsMap;
			}

		} catch (Exception e) {
			// TODO: handle exceptionF
			logger.error("WS_HubaoTvChannelMangerImplHelper类中验证ValidationCloseChannelPramse方法出现异常：" + e);
			return null;
		}
	}

	/***
	 * MangerChannel方法使用的验证签名的方法
	 * 
	 * @param pramsmap
	 * @return
	 */
	public static boolean ValidationMangerChannelSign(Map<String, Object> pramsmap) {
		PropertiesCnfig pro = new PropertiesCnfig();
		Properties pros = pro.LoadPropertie();
		String key = pros.getProperty("WangsuAppKey");
		String userid = pramsmap.get("userid").toString();
		String password = pramsmap.get("password").toString();
		String sign = pramsmap.get("sign").toString();
		String weSign = DigestUtils.md5Hex(userid + password + key);

		if (weSign.equalsIgnoreCase(sign)) {
			return true;
		} else {
			logger.error("WS_HubaoTvChannelMangerImplHelper类中验证ValidationCloseChannelSign方法验证签名错误：我方签名：" + weSign + "对方签名：" + sign);
			return false;
		}
	}

	/***
	 * MangerChannel构造请求API的方法
	 * 
	 * @param pramsmap
	 * @return
	 */
	public static String MangerChannelRequestAcion(Map<String, Object> pramsmap) {

		PropertiesCnfig pro = new PropertiesCnfig();
		Properties pros = new Properties();
		try {
			pros = pro.LoadPropertie();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("WS_HubaoTvChannelMangerImplHelper类中MangerChannelRequestAcion方法读取配置出现异常" + e);
		}
		// 断流服务API地址
		String WangsuManagerRltUrl = pros.getProperty("WangsuManagerRltUrl");
		// 网宿平台用户名
		String WangsuProtalUsername = pros.getProperty("WangsuProtalUsername");
		String WangsuProtalPassword = pros.getProperty("WangsuProtalPassword");
		// 网宿API固定的一个命令值
		String cmd = "channel_manager";
		// 0表示禁播1表示恢复
		String action = pramsmap.get("operation").toString().equals("0") ? "forbid" : "resume";
		// 0表示播放1表示推流
		String type = pramsmap.get("operationtchanneltype").toString().equals("0") ? "play" : "publish";
		// 频道号查询数据库获得
		String channel = "";
		// 进行加密前的
		String beformd5str = WangsuProtalUsername + WangsuProtalPassword + channel;
		if (logger.isDebugEnabled()) {
			logger.debug("WS_HubaoTvChannelMangerImplHelper类中MangerChannelRequestAcion方法加密前的字符串为"+beformd5str);
		}
		String passwd=DigestUtils.md5Hex(beformd5str);
		HttpWebClient httpweb=new HttpWebClient();
		Map< String, Object>sendmap=new HashMap<String, Object>();
		sendmap.put("username", WangsuProtalUsername);
		sendmap.put("passwd", passwd);
		sendmap.put("cmd", cmd);
		sendmap.put("action", action);
		sendmap.put("type", type);
		sendmap.put("channel", channel);
		try {
			String wsresult=httpweb.post(WangsuManagerRltUrl,sendmap);
			return wsresult;
		} catch (IOException e) {
			// TODO Auto-generated catch block			 
			logger.error("WS_HubaoTvChannelMangerImplHelper类中MangerChannelRequestAcion请求网宿API出现异常" + e);
			return "";
		}	

	}

}
