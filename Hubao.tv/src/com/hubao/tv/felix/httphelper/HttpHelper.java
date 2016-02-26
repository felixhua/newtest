/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hubao.tv.felix.httphelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author felix
 */
public class HttpHelper {

    private static final Logger logger = LogManager.getLogger(HttpHelper.class);
    /* MD5*/
    private final static char[] hex = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',};

    /**
     * Turns array of bytes into string representing each byte as
     * unsigned hex number.
     *
     * @param hash	array of bytes to convert to hex-string
     * @return	generated hex string
     */
    public static String toHex(byte hash[]) {
        StringBuilder buf = new StringBuilder(hash.length * 2);

        for (int idx = 0; idx < hash.length; idx++) {
            buf.append(hex[(hash[idx] >> 4) & 0x0f]).append(hex[hash[idx] & 0x0f]);
        }

        return buf.toString();
    }

    /**
     * Digest the input.
     *
     * @param input the data to be digested.
     * @return the md5-digested input
     */
    public static byte[] digest(byte[] input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(input);
        } catch (NoSuchAlgorithmException nsae) {
            throw new Error(nsae.toString());
        }
    }

    /**
     * Digest the input.
     *
     * @param input1 the first part of the data to be digested.
     * @param input2 the second part of the data to be digested.
     * @return the md5-digested input
     */
    public static byte[] digest(byte[] input1, byte[] input2) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input1);
            return md5.digest(input2);
        } catch (NoSuchAlgorithmException nsae) {
            throw new Error(nsae.toString());
        }
    }

    /**
     * Digest the input.
     *
     * @param input the data to be digested.
     * @return the md5-digested input as a hex string
     */
    public static String hexDigest(byte[] input) {
        return toHex(digest(input));
    }

    /**
     * Digest the input.
     *
     * @param input1 the first part of the data to be digested.
     * @param input2 the second part of the data to be digested.
     * @return the md5-digested input as a hex string
     */
    public static String hexDigest(byte[] input1, byte[] input2) {
        return toHex(digest(input1, input2));
    }

    /**
     * Digest the input.
     *
     * @param input the data to be digested.
     * @return the md5-digested input as a hex string
     */
    public static byte[] digest(String input) {
        try {
            return digest(input.getBytes("8859_1"));
        } catch (UnsupportedEncodingException uee) {
            throw new Error(uee.toString());
        }
    }

    /**
     * Digest the input.
     *
     * @param input the data to be digested.
     * @return the md5-digested input as a hex string
     */
    public static String hexDigest(String input) {
        try {
            return toHex(digest(input.getBytes("8859_1")));
        } catch (UnsupportedEncodingException uee) {
            throw new Error(uee.toString());
        }
    }

    public static String Decode(String value) throws UnsupportedEncodingException {
        return Decode(value, "utf-8");
    }

    public static String Decode(String value, String encodingName) throws UnsupportedEncodingException {
        return java.net.URLDecoder.decode(value, encodingName);
    }

    public static String Encode(String value) throws UnsupportedEncodingException {
        return Encode(value, "utf-8");
    }

    public static String Encode(String value, String encodingName) throws UnsupportedEncodingException {
        return java.net.URLEncoder.encode(value, encodingName);
    }

    public static void TextResult(HttpServletResponse res, String result) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        try {
            /* TODO output your page here*/
            out.print(result);
            out.flush();

        } finally {
            out.close();
        }
    }

    public static void TextResultJson(HttpServletResponse res, String result) throws IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        try {
            /* TODO output your page here*/
            out.print(result);
            out.flush();

        } finally {
            out.close();
        }
    }

    public static void TextResultLn(HttpServletResponse res, String result) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        try {
            /* TODO output your page here*/
            out.println(result);
            out.flush();

        } finally {
            out.close();
        }
    }
//    public static void TextResult(HttpString result) throws IOException {
//        HttpServletResponse res = ThreadLocalVariables.Response();
//        res.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = res.getWriter();
//        try {
//            /* TODO output your page here*/
//            out.println(result);
//
//        } finally {
//            out.close();
//        }
//    }

//    public static void JsonResult(JsonResult jsonResult) throws IOException {
//        HttpServletResponse res = ThreadLocalVariables.Response();
//        res.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = res.getWriter();
//        try {
//            /* TODO output your page here*/
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String json = gson.toJson(jsonResult);
//            out.println(json);
//
//        } finally {
//            out.close();
//        }
//    }
    
    
    /**
     * 获取来访真实IP
     * @param req
     * @return 
     */
    public static String GetRequestIp(HttpServletRequest req) {
        String ip = req.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = req.getHeader("WL-Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = req.getRemoteAddr();

        }
        return ip;
    }

    /**
     *  透过防火墙网卡 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
     * @param request
     * @return 
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        // ipAddress = request.getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress == null || ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    logger.error("getIpAddr方法透过网卡获取来访IP方法中出现异常，类名：HttpHelper，方法名getIpAddr：" + e);
                }
                ipAddress = inet.getHostAddress();
            }

        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     *  透过Ngnix映射获取请求客户端的真实IP地址,如果通过了Ngnix映射，此方法获取真实IP （不过需要先在Ngnix中配置proxy_set_header X-Real-IP $remote_addr）
     * @param request
     * @return 
     */
    public static String getIpAddrForNgnix(HttpServletRequest request) {
        String ipAddress = null;
        // ipAddress = request.getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress == null || ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    logger.error("getIpAddrForNgnix方法透过网卡获取来访IP方法中出现异常，类名：HttpHelper，方法名getIpAddr：" + e);
                }
                ipAddress = inet.getHostAddress();
            }
            if (ipAddress == null || ipAddress.equals("127.0.0.1")) {
                      ipAddress = request.getHeader("X-Real-IP");
            }

        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public static String Link(String title, String href, Map<String, Object> attributes, Map<String, Object> queryParams) {
        StringBuilder tempBuilder = new StringBuilder();
        if (queryParams != null) {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                tempBuilder.append("&").append(entry.getKey().toString()).append("=").append(entry.getValue().toString());
            }
            if (href.indexOf("?") != -1) {
                href = href + tempBuilder.toString();
            } else {
                href = href + "?" + tempBuilder.toString().substring(1);
            }
        }
        tempBuilder = new StringBuilder();
        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                tempBuilder.append(" ").append(entry.getKey().toString()).append("=\"").append(entry.getValue().toString()).append("\" ");
            }
        }
        String linkHtml = String.format("<a href=\"%s\"  %s  >%s</a>", href, tempBuilder.toString(), title);
        return linkHtml;
    }

    public static String Hidden(String name, Object value) {
        return String.format("<input value=\"%s\" type=\"hidden\" name=\"%s\" id=\"%s\" />", value, name, name);
    }
}
