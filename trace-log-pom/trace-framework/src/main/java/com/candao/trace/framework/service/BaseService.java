package com.candao.trace.framework.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.candao.trace.framework.bean.PagingList;
import com.candao.trace.framework.constants.OperateTriggerType;
import com.candao.trace.framework.jpa.BaseIdEntity;
import com.candao.trace.framework.jpa.repository.base.BaseRepository;
import com.candao.trace.framework.spring.bean.FastActionContext;

/**
 * Service基类<br/>
 * 抽象一些service共用方法
 * 
 */
public class BaseService {
	
	/**
	 * 根据ClientType获取当前操作触发方
	 * @return
	 */
	public OperateTriggerType getCurrentOperateTriggerType(){
		
		return OperateTriggerType.get(getCurrentClientType());
	}
	
	/**
     * 获取当前客户端请求ClientType
     * @return
     */
    public int getCurrentClientType(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentClientType() : 0;
    }
    
    /**
     * 获取当前用户token
     * @return
     */
    public String getCurrentToken(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentToken() : null;
    }
    
    /**
     * 获取当前用户所属运营商ID
     * @return
     */
    public int getCurrentPlatformId(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentPlatformId() : 0;
    }
    
    /**
     * 获取当前用户ID
     * @return
     */
    public int getCurrentAccountId(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentAccountId() : 0;
    }
    
    /**
     * 获取当前账号单个门店ID
     * @return
     */
    public Integer getCurrentOnlyStoreId(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentOnlyStoreId() : null;
    }
    
    /**
     * 获取当前用户所关联门店ID
     * @return
     */
    public List<Integer> getCurrentStoreIds(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentStoreIds() : null;
    }
    
    /**
     * 获取当前用户所关联组织ID
     * @return
     */
    public List<Integer> getCurrentOrgIds(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
	
    	return ctx != null ? ctx.getCurrentOrgIds() : null;
    }
    
    /**
     * 获取当前用户登录名
     * @return
     */
    public String getCurrentLoginName(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentLoginName() : null;
    }
    
    /**
     * 获取当前用户运营商编码
     * @return
     */
    public String getCurrentPlatformCode(){
    	
    	FastActionContext ctx = FastActionContext.getActionContext();
    	
    	return ctx != null ? ctx.getCurrentPlatformCode() : null;
    }
    
    /**
	 * 是否为系统运营商
	 * @param platformId
	 * @return
	 */
	public static boolean isSysPlatform(Integer platformId){
		
		return BaseIdEntity.isSysPlatform(platformId);
	}
	
	/**
	 * 是否为系统运营商
	 * @return
	 */
	public static boolean isSysPlatform(){
		
		return BaseIdEntity.isSysPlatform();
	}
	
	/**
	 * 获取分页的PagingList
	 * @param dbRepository-xxRepository
	 * @param pageNow-当前页(从1开始)
	 * @param pageSize-每页记录数
	 * @param queryParams-where查询参数
	 * @param sort-排序
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <T> PagingList<T> getPagingList(BaseRepository dbRepository, int pageNow, int pageSize, Specification<T> queryParams) {
		return getPagingList(dbRepository, pageNow, pageSize, queryParams, null);
	}

	/**
	 * 获取分页的PagingList
	 * @param dbRepository-xxRepository
	 * @param pageNow-当前页(从1开始)
	 * @param pageSize-每页记录数
	 * @param queryParams-where查询参数
	 * @param sort-排序
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> PagingList<T> getPagingList(BaseRepository dbRepository, int pageNow, int pageSize, Specification<T> queryParams, Sort sort) {
		Pageable pageable = null;
		if (sort != null) {
			pageable = new PageRequest(pageNow - 1, pageSize, sort);
		} else {
			pageable = new PageRequest(pageNow - 1, pageSize);
		}
		Page<T> list = dbRepository.findAll(queryParams, pageable);
		return PagingList.getPagingList(list);
	}
	
//	/**
//	 * 获取分页的PagingList
//	 * @param dbRepository-xxRepository
//	 * @param preSql-正常sql语句前缀
//	 * @param preCountSql-求总sql语句前缀
//	 * @param queryParams-where查询参数
//	 * @param sort-排序
//	 * @param pageNow-当前页
//	 * @param pageSize-每页记录数
//	 * @return
//	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public <T> PagingList<T> getPagingList(BaseRepository dbRepository, String preSql, String preCountSql, QueryParams<T> queryParams, Sort sort, int pageNow, int pageSize) {
//		StringBuffer sqlStr = new StringBuffer(preSql);
//		StringBuffer countSqlStr = new StringBuffer(preCountSql);
//		Pageable pageable = new PageRequest(pageNow, pageSize, sort);
//		List<T> list = dbRepository.findByQueryParam(sqlStr, queryParams, pageable);
//		int count = dbRepository.countByQueryParam(countSqlStr, queryParams).intValue();
//		PagingList<T> pagingList = new PagingList<T>();
//		pagingList.set(pageNow, pageSize, list, count);
//		return pagingList;
//	}
}
