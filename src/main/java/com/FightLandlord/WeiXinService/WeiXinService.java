package com.FightLandlord.WeiXinService;

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.net.URLEncoder;  
import java.nio.charset.Charset;  
import java.nio.charset.StandardCharsets;  
import java.util.Map;  
  
import org.apache.commons.lang.StringUtils;  
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpUriRequest;  
import org.apache.http.entity.ContentType;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClientBuilder;  
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  


import com.FightLandlord.model.User;  
  
  
  
  
  
public class WeiXinService   
{  
  
    private static final Logger logger = LoggerFactory.getLogger(WeiXinService.class);  
      
    public static final String WX_AUTH_LOGIN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";  
    public static final String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";  
    //appid和appSecret 是在公众平台上申请的  
    //AppId  
    public static final String WX_APP_ID = "wxb6411cbea5c*****";  
    //AppSecret  
    public static final String WX_APP_KEY = "86b91b295d23f34337b76cacd*******";  
  
      
  
      
    public User checkLogin(String code) throws JSONException   
    {  
        //获取授权 access_token  
        StringBuffer loginUrl = new StringBuffer();  
        loginUrl.append(WX_AUTH_LOGIN_URL).append("?appid=")  
                .append(WX_APP_ID).append("&secret=")  
                .append(WX_APP_KEY).append("&code=").append(code)  
                .append("&grant_type=authorization_code");  
        String loginRet = WeiXinService.get(loginUrl.toString());  
        JSONObject grantObj = new JSONObject(loginRet);  
        String errcode = grantObj.optString("errcode");  
        if (!StringUtils.isEmpty(errcode))   
        {  
            logger.error("login weixin error"+loginRet);  
            return null;  
        }  
        String openId = grantObj.optString("openid");  
        if (StringUtils.isEmpty(openId))   
        {  
            logger.error("login weixin getOpenId error"+loginRet);  
            return null;  
        }  
          
        String accessToken = grantObj.optString("access_token");  
        String expiresIn = grantObj.optString("expires_in");  
        String refreshToken = grantObj.optString("refresh_token");  
        String scope = grantObj.optString("scope");  
          
        //获取用户信息  
        StringBuffer userUrl = new StringBuffer();  
        userUrl.append(WX_USERINFO_URL).append("?access_token=").append(accessToken).append("&openid=").append(openId);  
        String userRet = WeiXinService.get(userUrl.toString());  
        JSONObject userObj = new JSONObject(userRet);  
        User user = new User();  
        user.setOpenId(openId);  
        user.setAccessToken(accessToken); 
        user.setRefreshToken(refreshToken);
        user.setScope(scope);  
        user.setExpiresIn(Integer.valueOf(expiresIn));  
        String nickname = userObj.optString("nickname");  
        String sex = userObj.optString("sex");  
        String userImg = userObj.optString("headimgurl");  
        String unionid = userObj.optString("unionid");  
        user.setNickname(nickname);  
        user.setUserImg(userImg); 
        user.setSex(sex); 
        user.setUnionid(unionid);  
        return user;  
    }  
      
      
    public static String get(String url) {  
        String body = null;  
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {       
            logger.info("create httppost:" + url);  
            HttpGet get = new HttpGet(url);  
            get.addHeader("Accept-Charset","utf-8");  
            HttpResponse response = sendRequest(httpClient, get);  
            body = parseResponse(response);  
        } catch (IOException e) {  
            logger.error("send post request failed: {}", e.getMessage());  
        }  
  
        return body;  
    }  
      
    private static String paramsToString(Map<String, String> params) {  
        StringBuilder sb = new StringBuilder();  
        try{  
            for (String key : params.keySet()) {  
                sb.append(String.format("&%s=%s", key, URLEncoder.encode(params.get(key),StandardCharsets.UTF_8.toString())));  
            }  
        }catch(UnsupportedEncodingException e){  
            logger.warn("{}: encode url parameters failed", e.getMessage());  
        }  
        return sb.length() > 0 ? "?".concat(sb.substring(1)) : "";  
    }  
      
    private static HttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost)  
            throws ClientProtocolException, IOException {  
        HttpResponse response = null;  
        response = httpclient.execute(httpost);  
        return response;  
    }  
      
    private static String parseResponse(HttpResponse response) {  
        logger.info("get response from http server..");  
        HttpEntity entity = response.getEntity();  
  
        logger.info("response status: " + response.getStatusLine());  
        Charset charset = ContentType.getOrDefault(entity).getCharset();  
        if (charset != null) {  
            logger.info(charset.name());  
        }  
  
        String body = null;  
        try {  
            body = EntityUtils.toString(entity, "utf-8");  
            logger.info("body " + body);  
        } catch (IOException e) {  
            logger.warn("{}: cannot parse the entity", e.getMessage());  
        }  
  
        return body;  
    }  
  
  
      
  
}  
