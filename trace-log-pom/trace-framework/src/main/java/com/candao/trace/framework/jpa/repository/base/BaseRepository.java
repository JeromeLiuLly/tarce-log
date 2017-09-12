package com.candao.trace.framework.jpa.repository.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.candao.trace.framework.jpa.DynamicExecuteSqlBuilder;
import com.candao.trace.framework.jpa.specification.QueryParams;

/**
 * @author jeromeLiu
 * 
 * 基础操作
 */
@NoRepositoryBean
public interface BaseRepository<T ,ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{
	
	public DynamicExecuteSqlBuilder getExecuteSqlBuilder();
	
	public List<Object[]> listBySQL(String sql);

    /**
     * 保存实体
     *
     * @param entity 实体id
     */
    public void add(T entity);

    /**
     * 更新实体
     * 
     * @param entity 实体
     */
    public T update(T entity);
    
    /**
     * 插入或更新实体
     * 
     * @param entity 实体
     */
    public T saveOrUpdate(T entity);

    /**
     * 删除实体
     *
     * @param entityClass 实体类
     * @param entityid    实体id
     */
    public void delete(Class<T> entityClass, Object entityid);

    /**
     * 批量删除实体
     *
     * @param entityClass 实体类
     * @param entityids   实体id数组
     */
    public void delete(Class<T> entityClass, Object[] entityids);

    /**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param setParam
	 * @param whereParam
	 * @return
	 */
	public int dynamicUpdate(Object setParam, Object whereParam);
	
	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param obj
	 * @return
	 */
	public int dynamicUpdate(Object obj);

	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param paramMap
	 * @return
	 */
	public int dynamicUpdate(Map<String, Object> paramMap);
	
	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * @param setParamMap
	 * @param whereParamMap
	 * @return
	 */
	public int dynamicUpdate(Map<String, Object> setParamMap, Map<String, Object> whereParamMap);
	
	/**
	 * 删除操作 - 条件动态拼接
	 * @param obj
	 * @return
	 */
	public int dynamicDelete(Object obj);

	/**
	 * 删除操作 - 条件动态拼接
	 * @param paramMap
	 * @return
	 */
	public int dynamicDelete(Map<String, Object> paramMap);
	
	/**
	 * 修改/删除操作
	 * @param sql
	 * @return
	 */
	public int execute(String sql);
	
	/**
	 * 修改/删除操作
	 * @param sql
	 * @param obj
	 * @return
	 */
	public int execute(String sql, Object obj);
	
	/**
	 * 修改/删除操作
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int execute(String sql, Map<String, Object> paramMap);
    
    /**
     * 获取实体
     *
     * @param <T>
     * @param entityClass 实体类
     * @param entityId   实体id
     * @return
     */
    public T find(Class<T> entityClass, Object entityId);
    
	/**
	 * @param example 对象
	 * @param pageable
	 * @return
	 */
	public Page<T> findByParam(T example, Pageable pageable);
	
	
	public List<T> findByQueryParam(StringBuffer sql, QueryParams<?> queryParams);
	
	public List<T> findByQueryParam(StringBuffer sql, QueryParams<?> queryParams, Sort sort);

	public List<T> findByQueryParam(StringBuffer sql, QueryParams<?> queryParams,Pageable pageable);
	
	/**
	 * 根据过滤条件求总
	 * 
	 * @param sql
	 * @param queryParams
	 * @return
	 */
	public Long countByQueryParam(StringBuffer sql, QueryParams<?> queryParams);
	
	/**
	 * 更新数据状态
	 * @param ids
	 * @param status
	 * @return
	 */
	public int changeStatus(List<Integer> ids, byte status);
}
