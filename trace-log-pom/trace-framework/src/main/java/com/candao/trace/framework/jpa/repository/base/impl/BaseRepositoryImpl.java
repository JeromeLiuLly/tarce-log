package com.candao.trace.framework.jpa.repository.base.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.candao.trace.framework.jpa.DynamicExecuteSqlBuilder;
import com.candao.trace.framework.jpa.common.BaseSpecs;
import com.candao.trace.framework.jpa.repository.base.BaseRepository;
import com.candao.trace.framework.jpa.specification.Filter;
import com.candao.trace.framework.jpa.specification.QueryParams;
import com.candao.trace.framework.util.FastClassUtils;

/**
 * @author jeromeLiu
 * 
 *         基础工具实现类
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

	// 通过构造方法初始化EntityManager
	private final EntityManager entityManager;

	private DynamicExecuteSqlBuilder executeSqlBuilder;

	public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
		this.executeSqlBuilder = DynamicExecuteSqlBuilder.get(domainClass);
	}

	public DynamicExecuteSqlBuilder getExecuteSqlBuilder() {
		return this.executeSqlBuilder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listBySQL(String sql) {
		return entityManager.createNativeQuery(sql).getResultList();
	}

	@Override
	@Transactional
	public void add(T entity) {
		entityManager.persist(entity);
	}

	@Override
	@Transactional
	public T update(T entity) {
		return entityManager.merge(entity);
	}

	@Override
	@Transactional
	public T saveOrUpdate(T entity) {
		return entityManager.merge(entity);
	}

	@Override
	@Transactional
	public void delete(Class<T> entityClass, Object entityid) {
		delete(entityClass, new Object[] { entityid });
	}

	@Override
	@Transactional
	public void delete(Class<T> entityClass, Object[] entityids) {
		for (Object id : entityids) {
			entityManager.remove(entityManager.getReference(entityClass, id));
		}
	}

	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * 
	 * @param setParam
	 * @param whereParam
	 * @return
	 */
	@Transactional
	@Modifying
	public int dynamicUpdate(Object setParam, Object whereParam) {

		Map<String, Object> setParamMap = FastClassUtils.obj2Map(setParam);
		Map<String, Object> whereParamMap = FastClassUtils.obj2Map(whereParam);

		return dynamicUpdate(setParamMap, whereParamMap);
	}

	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * 
	 * @param obj
	 * @return
	 */
	@Transactional
	@Modifying
	public int dynamicUpdate(Object obj) {

		return dynamicUpdate(FastClassUtils.obj2Map(obj));
	}

	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * 
	 * @param paramMap
	 * @return
	 */
	@Transactional
	@Modifying
	public int dynamicUpdate(Map<String, Object> paramMap) {

		String updateSql = executeSqlBuilder.getUpdateSql(paramMap);

		return execute(updateSql, paramMap);
	}

	/**
	 * 动态执行update - set参数与where拼接动态生成
	 * 
	 * @param setParamMap
	 * @param whereParamMap
	 * @return
	 */
	@Transactional
	@Modifying
	public int dynamicUpdate(Map<String, Object> setParamMap, Map<String, Object> whereParamMap) {

		String updateSql = executeSqlBuilder.getUpdateSql(setParamMap, whereParamMap);

		return execute(updateSql, whereParamMap);
	}

	/**
	 * 删除操作 - 条件动态拼接
	 * 
	 * @param obj
	 * @return
	 */
	@Transactional
	@Modifying
	public int dynamicDelete(Object obj) {

		return dynamicDelete(FastClassUtils.obj2Map(obj));
	}

	/**
	 * 删除操作 - 条件动态拼接
	 * 
	 * @param paramMap
	 * @return
	 */
	@Transactional
	@Modifying
	public int dynamicDelete(Map<String, Object> paramMap) {

		String deleteSql = executeSqlBuilder.getDelSql(paramMap);

		return execute(deleteSql, paramMap);
	}

	/**
	 * 修改/删除操作
	 * 
	 * @param sql
	 * @return
	 */
	@Transactional
	@Modifying
	public int execute(String sql) {

		return execute(sql, null);
	}

	/**
	 * 修改/删除操作
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 */
	@Transactional
	@Modifying
	public int execute(String sql, Object obj) {

		Map<String, Object> paramMap = FastClassUtils.obj2Map(obj);

		return execute(sql, paramMap);
	}

	/**
	 * 修改/删除操作
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	@Transactional
	@Modifying
	public int execute(String sql, Map<String, Object> paramMap) {

		Query query = entityManager.createNativeQuery(sql);

		Set<Parameter<?>> parameters = query.getParameters();
		if(parameters != null && !parameters.isEmpty()){
			Iterator<Parameter<?>> iterator = parameters.iterator();
			while (iterator.hasNext()) {
				Parameter<?> next = iterator.next();
				String key = next.getName();
				if (StringUtils.isNoneBlank(key)) {
					query.setParameter(key, paramMap.get(key));
				}
			}
		}

		return query.executeUpdate();
	}

	@Override
	public T find(Class<T> entityClass, Object entityId) {
		return entityManager.find(entityClass, entityId);
	}

	@Override
	public Page<T> findByParam(T example, Pageable pageable) {
		return findAll(BaseSpecs.byAuto(entityManager, example), pageable);
	}

	/**
	 * 分析查询参数,并且合并到sql语句中
	 * 
	 * @param sql JPQL查询语句
	 * @param params 查询参数
	 * @return 参数对应的value
	 */
	private List<Object> analysisQueryParams(StringBuffer sql, QueryParams<?> params) {

		List<String> strList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		sql.append(" where 1 = 1 ");
		int i = 1;

		/*
		 * StringBuffer hql = sql;
		 * 
		 * for(Filter filter : params.getAllFilter()){ if (params.getOrFilters().contains(filter)) { if (filter.getValue() != null) { hql.append(" or "
		 * ).append(filter.getProperty() + " " + filter.getOperator().getOperator() + " ?"); } }else{ if (filter.getValue() != null) { hql.append(" and "
		 * ).append(filter.getProperty() + " " + filter.getOperator().getOperator() + " ?"); } } }

		 * for(Filter filter : params.getAllFilter()){ if
		 * (params.getOrFilters().contains(filter)) { if (filter.getValue() !=
		 * null) { hql.append(" or ").append(filter.getProperty() + " " +
		 * filter.getOperator().getOperator() + " ?"); } }else{ if
		 * (filter.getValue() != null) {
		 * hql.append(" and ").append(filter.getProperty() + " " +
		 * filter.getOperator().getOperator() + " ?"); } } }
		 * 
		 * System.out.println(hql);
		 */

		// 分析or条件
		for (Filter filter : params.getOrFilters()) {
			if (filter.getValue() != null) {
				strList.add(filter.getProperty() + " " + filter.getOperator().getOperator() + " ?" + (i++));
				valueList.add(filter.getValue());
			} else {
				strList.add(filter.getProperty() + " " + filter.getOperator().getOperator() + " ");
			}
		}
		if (!strList.isEmpty()) {
			sql.append(" and ").append("( ").append(StringUtils.join(strList, " or ")).append(" )");
		}
		strList.clear();

		// 分析and条件
		for (Filter filter : params.getAndFilters()) {
			if (filter.getValue() != null) {
				strList.add(filter.getProperty() + " " + filter.getOperator().getOperator() + " ?" + (i++));
				valueList.add(filter.getValue());
			} else {
				strList.add(filter.getProperty() + " " + filter.getOperator().getOperator() + " ");
			}
		}
		sql.append(" and ").append(StringUtils.join(strList, " and "));

		// 分析排序字段
		if (!params.getOrders().isEmpty()) {
			sql.append(" order by ");
			sql.append(StringUtils.join(params.getOrders(), ","));
		}
		return valueList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByQueryParam(StringBuffer sql, QueryParams<?> queryParams) {
		List<Object> values = analysisQueryParams(sql, queryParams);
		Query query = entityManager.createNativeQuery(sql.toString());
		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i + 1, values.get(i));
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByQueryParam(StringBuffer sql, QueryParams<?> queryParams, Sort sort) {
		List<Object> values = analysisQueryParams(sql, queryParams);
		Query query = entityManager.createNativeQuery(sql.toString());
		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i + 1, values.get(i));
		}
		// TODO 排序
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByQueryParam(StringBuffer sql, QueryParams<?> queryParams, Pageable pageable) {
		List<Object> values = analysisQueryParams(sql, queryParams);
		Query query = entityManager.createNativeQuery(sql.toString());
		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i + 1, values.get(i));
		}
		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}

	@Override
	public Long countByQueryParam(StringBuffer sql, QueryParams<?> queryParams) {
		List<Object> values = analysisQueryParams(sql, queryParams);
		Query query = entityManager.createNativeQuery(sql.toString());
		for (int i = 0; i < values.size(); i++) {
			query.setParameter(i + 1, values.get(i));
		}
		return ((Number) query.getSingleResult()).longValue();
	}

	/**
	 * 更新数据状态
	 * @param ids
	 * @param status
	 * @return
	 */
	@Transactional
	@Modifying
	public int changeStatus(List<Integer> ids, byte status){
		
		Map<String, Object> paramMap = executeSqlBuilder.getChangeStatusParam(ids, status);
		
		String changeStatusSql = executeSqlBuilder.getChangeStatusSql(ids, status);
		
		return execute(changeStatusSql, paramMap);
	}
	
}
