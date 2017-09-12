package com.candao.trace.framework.net.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.candao.trace.framework.util.FastStringUtils;
import com.candao.trace.log.logger.Logger;

/**
 * 
 * rest请求工具类
 * 
 */
public class FastRestHttpClient {

	/**
	 * post请求(body参数)
	 * @param url
	 * @param obj
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult postWithBody(String url, Object obj, FastRestRequestHandler fastRestRequestHandler){
		
		String bodyInfo = obj != null ? JSON.toJSONString(obj) : null;
		
		return postWithBody(url, bodyInfo, fastRestRequestHandler);
	}
	
	/**
	 * post请求(body参数)
	 * @param url
	 * @param bodyInfo
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult postWithBody(String url, String bodyInfo, FastRestRequestHandler fastRestRequestHandler){
		
		HttpPost request = new HttpPost(url);
		
		if(bodyInfo != null){
			StringEntity entity = new StringEntity(bodyInfo, fastRestRequestHandler.getCharsetEncoding());
			setHttpEntityContentParams(entity, fastRestRequestHandler);
			request.setEntity(entity);
		}
		
		return request(request, fastRestRequestHandler);
	}
	
	/**
	 * post请求(表单参数)
	 * @param url
	 * @param paramMap
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult postWithParam(String url, Map<String, Object> paramMap, FastRestRequestHandler fastRestRequestHandler){
		
		HttpPost request = new HttpPost(url);
		
		setFormParam(request, paramMap, fastRestRequestHandler);
		
		return request(request, fastRestRequestHandler);
	}
	
	/**
	 * put请求(body参数)
	 * @param url
	 * @param obj
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult putWithBody(String url, Object obj, FastRestRequestHandler fastRestRequestHandler){
		
		String bodyInfo = obj != null ? JSON.toJSONString(obj) : null;
		
		return putWithBody(url, bodyInfo, fastRestRequestHandler);
	}
	
	/**
	 * put请求(body参数)
	 * @param url
	 * @param bodyInfo
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult putWithBody(String url, String bodyInfo, FastRestRequestHandler fastRestRequestHandler){
		
		HttpPut request = new HttpPut(url);
		
		if(bodyInfo != null){
			StringEntity entity = new StringEntity(bodyInfo, fastRestRequestHandler.getCharsetEncoding());
			setHttpEntityContentParams(entity, fastRestRequestHandler);
			request.setEntity(entity);
		}
		
		return request(request, fastRestRequestHandler);
	}
	
	/**
	 * put请求(表单参数)
	 * @param url
	 * @param paramMap
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult putWithParam(String url, Map<String, Object> paramMap, FastRestRequestHandler fastRestRequestHandler){
		
		HttpPut request = new HttpPut(url);
		
		setFormParam(request, paramMap, fastRestRequestHandler);
		
		return request(request, fastRestRequestHandler);
	}
	
	/**
	 * get请求
	 * @param url
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult get(String url, FastRestRequestHandler fastRestRequestHandler){
		
		return get(url, null, fastRestRequestHandler);
	}
	
	/**
	 * get请求
	 * @param url
	 * @param paramMap
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult get(String url, Map<String, Object> paramMap, FastRestRequestHandler fastRestRequestHandler){
		
		String getUrl = getQueryParamUrl(url, paramMap);
		
		HttpGet request = new HttpGet(getUrl);
		
		return request(request, fastRestRequestHandler);
	}
	
	/**
	 * delete请求
	 * @param url
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult delete(String url, FastRestRequestHandler fastRestRequestHandler){
		
		return delete(url, null, fastRestRequestHandler);
	}
	
	/**
	 * delete请求
	 * @param url
	 * @param paramMap
	 * @param fastRestRequestHandler
	 * @return
	 */
	public static HttpResult delete(String url, Map<String, Object> paramMap, FastRestRequestHandler fastRestRequestHandler){
		
		String deleteUrl = getQueryParamUrl(url, paramMap);
		
		HttpDelete request = new HttpDelete(deleteUrl);
		
		return request(request, fastRestRequestHandler);
	}
	
	/**
	 * 构建参数URL
	 * @param url
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String getQueryParamUrl(String url, Map<String, Object> paramMap){
		
		if(FastStringUtils.isBlank(url) || paramMap == null || paramMap.isEmpty()){
			return url;
		}
		
		List<String> queryParamItems = new ArrayList<String>();
		Iterator<String> iterator = paramMap.keySet().iterator();
		while(iterator.hasNext()){
			String k = iterator.next();
			if(FastStringUtils.isBlank(k) || "_".equals(k)) continue;
			k = URLEncoder.encode(k);
			String v = URLEncoder.encode(String.valueOf(paramMap.get(k)));
			queryParamItems.add(new StringBuffer(k).append("=").append(v).toString());
		}
		
		String queryParams = FastStringUtils.join(queryParamItems, "&");
		
		if(url.indexOf("?") > -1){
			return new StringBuffer(url).append("&").append(queryParams).toString();
		}else{
			return new StringBuffer(url).append("?").append(queryParams).toString();
		}
	}
	
	/**
	 * 设置表单参数
	 * @param request
	 * @param paramMap
	 * @param fastRestRequestHandler
	 */
	private static void setFormParam(HttpEntityEnclosingRequestBase request, Map<String, Object> paramMap, FastRestRequestHandler fastRestRequestHandler){
		
		if(request == null || paramMap == null || paramMap.isEmpty()) return;
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		Iterator<String> iterator = paramMap.keySet().iterator();
		
		while(iterator.hasNext()){
			
			String k = iterator.next();
			
			if(FastStringUtils.isBlank(k) || "_".equals(k)) continue;
			
			String v = String.valueOf(paramMap.get(k));
			
			nvps.add(new BasicNameValuePair(k, v));
		}
		
		try {
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, fastRestRequestHandler.getCharsetEncoding());
			setHttpEntityContentParams(urlEncodedFormEntity, fastRestRequestHandler);
			request.setEntity(urlEncodedFormEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private static void setHttpEntityContentParams(AbstractHttpEntity httpEntity, FastRestRequestHandler fastRestRequestHandler){
		if(httpEntity != null && fastRestRequestHandler != null){
			ContentType contentType = fastRestRequestHandler.getHttpEntityContentType();
			if(contentType != null) httpEntity.setContentType(contentType.toString());
		}
	}
	
	/**
	 * 执行请求操作
	 * @param request
	 * @param fastRestRequestHandler
	 * @return
	 */
	private static HttpResult request(HttpRequestBase request, FastRestRequestHandler fastRestRequestHandler){
		
		HttpResult httpResult = new HttpResult();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		
		try {
			
			if(!fastRestRequestHandler.preHandle(httpClient, request)){
				return new HttpResult(-1);
			}
			
			fastRestRequestHandler.setHead(request);
			fastRestRequestHandler.setConfig(request);
			
			httpResponse = httpClient.execute(request);
			
			response(httpResponse, httpResult, fastRestRequestHandler);
			
			fastRestRequestHandler.postHandle(httpClient, request, httpResponse, httpResult);
			
		} catch (ClientProtocolException e) {
			fastRestRequestHandler.onError(e);
		} catch (IOException e) {
			fastRestRequestHandler.onError(e);
		} catch (Exception e) {
			fastRestRequestHandler.onError(e);
		}finally {
			Logger.info("FastRestHttp:request" + request);
			Logger.info("FastRestHttp:httpResponse" + httpResponse);
			try {
				httpClient.close();
			} catch (IOException e) {
				fastRestRequestHandler.onError(e);
			}
		}
		
		return httpResult;
	}
	
	/**
	 * 获取响应信息
	 * @param httpResponse
	 * @param httpResult
	 * @param fastRestRequestHandler
	 * @throws Exception
	 */
	private static void response(CloseableHttpResponse httpResponse, HttpResult httpResult, FastRestRequestHandler fastRestRequestHandler) throws Exception {
		
		try {
			
			// 解析返结果
			HttpEntity entity = httpResponse.getEntity();
			
			httpResult.statusCode = httpResponse.getStatusLine().getStatusCode();
			httpResult.response = httpResponse;
			
			if (entity != null && httpResult.statusCode == HttpStatus.SC_OK) {
				httpResult.content = EntityUtils.toString(entity, fastRestRequestHandler.getCharsetEncoding());
			}
			
			// 关闭底层的HttpEntity流
			EntityUtils.consume(entity);
			
		} finally {
			httpResponse.close();
		}
	}
	
}
