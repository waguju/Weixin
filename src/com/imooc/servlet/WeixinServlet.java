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


public class WeixinServlet extends HttpServlet {
	/**
	 * ������֤
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}
	
	
	/**
	 * ��Ϣ�Ľ�������Ӧ
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
			
			//��ȡ�Ѿ������access_token
			String access_token="dtTgRjE0XJKhh_WeAz4KiEdIdWtuiuwAitgaumajQTaTa-qRp-PCk6SIELBG28i--MIHbA3gcBNCPcygiDP4OJ1b8q09PSf5exy0g-ZUzYwfpg7r_QBeCeVNkp3e-Ra2ZMYdAEAREQ";
			
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				if("1".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
				}else if("tokenupdate".equals(content)){
					System.out.println("wgj----------tokenupdate-----------");
					AccessToken token = AccessTokenUtil.getAccessToken();

					//�����µ�token������accesstoken.properties�����ļ�
					//AccessTokenUtil.Save(token);
					
					//����access_token
					access_token = token.getToken();
					message = token.getToken();
				}else if("manudelete".equals(content)){
					//ɾ���˵�
					int ret = WeixinUtil.deleteMenu(access_token);
					if (ret != 0){
						message="manuɾ��ʧ��,ret="+ret;
					}else{
						message="manuɾ���ɹ�,ret="+ret;
					}
				}else if("manucreate".equals(content)){
					//�����˵�
					Menu menu = new Menu();
					menu = WeixinUtil.initMenu();
					String jsonMenu=JSONObject.fromObject(menu).toString();
					int ret = WeixinUtil.createMenu(access_token,jsonMenu);
					if (ret != 0){
						message="manu����ʧ��,ret="+ret;
					}else{
						message="manu�����ɹ�,ret="+ret;
					}
					
				
				}else if("?".equals(content) || "��".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(content.startsWith("����")){
					String word = content.replaceAll("^����", "").trim();
					if("".equals(word)){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.threeMenu());
					}else{
						message = MessageUtil.initText(toUserName, fromUserName, WeixinUtil.translate(word));
					}
				}
			}else if(MessageUtil.MESSAGE_EVNET.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					String url = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName, url);
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
					String key = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName, key);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
				String label = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName, label);
			}
			
			System.out.println(message);
			System.out.println("wgj----------servlet doPostҵ�������");
			
			out.print(message);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
}
