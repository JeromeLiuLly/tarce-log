package com.candao.trace.gateway.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.client.ClientException;

@Component
public class HttpService {

	public static final String AUTHORITY_SERVICE = "AUTHORITY-SERVICE";
	
	@Autowired
    LoadBalancerClient loadBalancerClient;
	@Autowired
    public RestTemplate restTemplate;
	
	private String ignoreTokenUri = "/log/*,/authority/foo/login,/authority/shop/login,/store/external/sync,/oplog/*";
	
	private String[] ignoreTokenUris;
	
	private String[] getIgnoreTokenUris() {
		if (this.ignoreTokenUris == null) {
			if (this.ignoreTokenUri == null || "".equals(this.ignoreTokenUri)) {
				this.ignoreTokenUris = new String[]{};
			} else {
				this.ignoreTokenUris = ignoreTokenUri.split(",");
			}
		}
		return ignoreTokenUris;
	}
	
	public boolean isIgnoreToken(String uri) {
		for (String ignoreUri : getIgnoreTokenUris()) {
			if (ignoreUri.indexOf("*") > -1) {
				boolean rs = uri.startsWith(ignoreUri.replace("*", ""));
				if (rs) {
					return rs;
				}
			}
			if (uri.equals(ignoreUri)) {
				return true;
			}
		}
		return false;
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@SuppressWarnings("rawtypes")
	public Map getAccountByToken(String token, int clientType) throws ClientException {
		ServiceInstance serviceInstance = loadBalancerClient.choose(AUTHORITY_SERVICE);
		if (serviceInstance == null) {
			throw new ClientException("Load balancer does not have available server for client: " + AUTHORITY_SERVICE.toLowerCase());
//			throw new TokenException();
		}
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/inner/account/getAccountByToken/" + token + "/" + clientType;
        return restTemplate.postForObject(url, null, Map.class);
	}
	
}
