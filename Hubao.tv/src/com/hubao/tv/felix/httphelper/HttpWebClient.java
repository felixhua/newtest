/*
 * http访问 封装
 * @author felix
 */
package com.hubao.tv.felix.httphelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * http访问 封装
 * 
 * @author felix
 */
public class HttpWebClient {

	private String charset = "GBK";
	private HttpClient httpclient;
	private static final Logger logger = LogManager.getLogger(HttpWebClient.class);

	/**
	 * 构造方法
	 */
	public HttpWebClient() {
		httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);
	}

	/**
	 * 构造方法
	 * 
	 * @param charset
	 */
	public HttpWebClient(String charset) {
		this();
		this.charset = charset;
	}

	/**
	 * 构造方法 ：使用代理连接
	 * 
	 * @param ip
	 * @param port
	 */
	public HttpWebClient(String ip, int port) {
		this();
		HttpHost proxy = new HttpHost(ip, port);
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	private HttpGet setHttpGetHeader(HttpGet httpGet, Map<String, Object> headers) {
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}
		return httpGet;
	}

	public String get(HttpGet httpget) throws IOException {
		HttpResponse response = httpclient.execute(httpget);
		return this.getResponseBodyAsString(response);
	}

	public String get(Map<String, Object> headers, String url) throws IOException {
		HttpGet httpget = new HttpGet(url);
		httpget = this.setHttpGetHeader(httpget, headers);
		return this.get(httpget);
	}

	private String get(String url, String headerAccept) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		headers.put("Accept", headerAccept);
		return this.get(headers, url);
	}

	public String get(String url) throws IOException {
		return this.get(url, WebHeaderConstant.WEB_ACCEPT_XML);
	}

	public String getJson(String url) throws IOException {
		return this.get(url, WebHeaderConstant.WEB_ACCEPT_JSON);
	}

	public byte[] getFile(HttpGet httpget) throws IOException {
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		byte[] bs = EntityUtils.toByteArray(entity);
		if (entity != null) {
			entity.consumeContent();
		}
		return bs;
	}

	public byte[] getFile(Map<String, Object> headers, String url) throws IOException {
		HttpGet httpget = new HttpGet(url);
		httpget = this.setHttpGetHeader(httpget, headers);
		return this.getFile(httpget);
	}

	public byte[] getFile(String url) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		headers.put("Accept", WebHeaderConstant.WEB_ACCEPT_XML);
		return this.getFile(headers, url);
	}

	public byte[] getFile(String url, File file) throws IOException {
		byte[] bs = this.getFile(url);
		FileOutputStream output = new FileOutputStream(file);
		output.write(bs);
		output.close();
		return bs;
	}

	private HttpPost setHttpPostHeaderAndParams(HttpPost httpPost, Map<String, Object> headers, Map<String, Object> params) throws UnsupportedEncodingException {
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue().toString());
			}
		}
		//
		List nvps = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		return httpPost;
	}

	public String post(HttpPost httppost) throws IOException {

		HttpResponse response = httpclient.execute(httppost);
		// logger.debug(StringUtil.format("url:{0},params:",
		// httppost.getURI()));
		return this.getResponseBodyAsString(response);
	}

	public String post(Map<String, Object> headers, String url, Map<String, Object> params) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		this.setHttpPostHeaderAndParams(httpPost, headers, params);
		return this.post(httpPost);
	}

	public String post(String url, Map<String, Object> params) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		headers.put("Accept", WebHeaderConstant.WEB_ACCEPT_XML);
		return this.post(headers, url, params);
	}

	public String postJson(String url, Map<String, Object> params) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		headers.put("Accept", WebHeaderConstant.WEB_ACCEPT_JSON);
		return this.post(headers, url, params);
	}

	public String post(HttpPost httppost, String str) throws IOException {
		StringEntity reqEntity = new StringEntity(str);
		httppost.setEntity(reqEntity);
		HttpResponse response = httpclient.execute(httppost);
		return this.getResponseBodyAsString(response);
	}

	public String post(Map<String, Object> headers, String url, String str) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		this.setHttpPostHeaderAndParams(httpPost, headers, null);
		return this.post(httpPost, str);
	}

	public String post(String url, String str) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		headers.put("Accept", WebHeaderConstant.WEB_ACCEPT_XML);
		return this.post(headers, url, str);
	}

	public String postJson(String url, String str) throws IOException {
		Map<String, Object> headers = this.getCommonHeader();
		headers.put("Accept", WebHeaderConstant.WEB_ACCEPT_JSON);
		return this.post(headers, url, str);
	}

	public void close() {
		httpclient.getConnectionManager().shutdown();
	}

	// ///////////////////////////////////////////////////////////////
	public HttpClient getHttpclient() {
		return httpclient;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	private String getResponseBodyAsString(HttpResponse response) throws IOException {
		String html = null;
		GZIPInputStream gzin = null;
		InputStreamReader isr = null;

		if (response.getEntity().getContentEncoding() != null && response.getEntity().getContentEncoding().getValue().toLowerCase().indexOf("gzip") > -1) {
			try {
				InputStream is = response.getEntity().getContent();
				gzin = new GZIPInputStream(is);
				isr = new InputStreamReader(gzin, "iso-8859-1");
				BufferedReader br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String tempbf;
				while ((tempbf = br.readLine()) != null) {
					sb.append(tempbf);
					sb.append("\r\n");
				}
				html = sb.toString();
				html = new String(html.getBytes("iso-8859-1"), this.charset);
			} catch (Exception e) {
				logger.error("getResponseBodyAsString方法读取远程请求返回的html代码返回出错" + e);
			} finally {
				try {
					if (isr != null) {
						isr.close();
					}
					if (gzin != null) {
						gzin.close();
					}
				} catch (Exception ex) {
					logger.error("getResponseBodyAsString读取远程请求返回的html代码，关闭流时出现异常)" + ex);
				}

			}
		} else {
			try {
				HttpEntity entity = response.getEntity();
				html = EntityUtils.toString(entity, this.charset);
				if (entity != null) {
					entity.consumeContent();
				}
			} catch (Exception e) {
				logger.error("getResponseBodyAsString方法读取远程请求返回的html代码返回出错(在InputStream为空时)" + e);
			}
		}
		return html;
	}

	private Map<String, Object> getCommonHeader() {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("Accept-Charset", WebHeaderConstant.WEB_ACCEPT_CHARSET);
		headers.put("Accept-Encoding", WebHeaderConstant.WEB_ACCEPT_ENCODING);
		headers.put("Accept-Language", WebHeaderConstant.WEB_ACCEPT_LANGUAGE);
		headers.put("User-Agent", WebHeaderConstant.WEB_USER_AGENT);
		return headers;
	}
}
