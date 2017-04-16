package com.imooc.util;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.sf.json.JSONObject;

import org.apache.http.ParseException;

import com.imooc.po.AccessToken;
import com.imooc.util.WeixinUtil;



public class AccessTokenUtil {
	private static final String APPID = "wxd02afbd599ae1294";
	private static final String APPSECRET = "3b8fae7987f09ff76dfe73792e9e61d8";	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	WeixinUtil weixinutil;
	
	/**
	 * 获取accessToken
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static AccessToken getAccessToken() throws ParseException, IOException{
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		
		System.out.println("wgj----------getAccessToken url="+url);
		
		JSONObject jsonObject = WeixinUtil.doGetStr(url);
		if(jsonObject!=null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	public static void Save(AccessToken _accessToken) throws IOException {
		try {
			Properties property = new Properties();
			property.setProperty("AccessToken", _accessToken.getToken());
			//property.setProperty("AccessTokenSecret", _accessToken.getTokenSecret());
			property.store(new FileOutputStream("accesstoken.properties"), "accesstoken.properties");

			System.out.println("Successfully Save Access Token");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读取AccessToken数据
	public static String Read(String _filename) throws IOException {
		try {
			Properties property = new Properties();
			String accessToken = null;
			property.load(new FileInputStream(_filename));
			
			//AccessToken _accessToken = new AccessToken(property.getProperty("AccessToken"), property.getProperty("AccessTokenSecret"));
			accessToken = property.getProperty("AccessToken");
			System.out.println("Successfully Read Access Token");
			return accessToken;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
