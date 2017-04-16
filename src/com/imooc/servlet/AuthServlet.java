package com.imooc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import net.sf.json.JSONObject;

import com.imooc.util.CheckUtil;
import com.imooc.util.MessageUtil;
import com.imooc.util.WeixinUtil;
import com.imooc.menu.Menu;
import com.imooc.po.AccessToken;
import com.imooc.util.AccessTokenUtil;


public class AuthServlet extends HttpServlet {
	/**
	 * 授权验证
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 消息的接收与响应
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");

			String reqStr = map.toString();
			System.out.println(reqStr);
			
			String message="";
			System.out.println(message);
			System.out.println("wgj----------servlet doPost业务处理结束");
			
			out.print(message);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
}
