package com.candao.trace.framework.jpa.specification;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * 封装查询条件的实体
 * 
 * @author jeromeLiu
 *
 * @param <T>
 */
public class QueryParams<T> implements Specification<T> {

    /** 属性分隔符 */
    private static final String PROPERTY_SEPARATOR = ".";
    
    /**
     * and条件
     */
    private List<Filter> andFilters = new ArrayList<Filter>();
    
    /**
     * or条件
     */
    private List<Filter> orFilters = new ArrayList<Filter>();
    
    /**
     * 排序属性
     */
    private List<Order> orders = new ArrayList<Order>();
    
    /**
     * 所有过滤条件
     */
    private List<Filter> allFilter = new ArrayList<Filter>();

    /**
     * 添加一个and条件
     * @param filter 该条件
     * @return 链式调用
     */
    public  QueryParams<T> and(Filter filter){
        this.andFilters.add(filter);
        this.allFilter.add(filter);
        return this;
    }
    
    /**
     * 添加多个and条件
     * @param filter 该条件
     * @return 链式调用
     */
    public  QueryParams<T> and(Filter ...filter){
        this.andFilters.addAll(Arrays.asList(filter));
        this.allFilter.addAll(Arrays.asList(filter));
        return this;
    }
    
    /**
     * 添加一个or条件
     * @param filter 该条件
     * @return 链式调用
     */
    public  QueryParams<T> or(Filter filter){
        this.orFilters.add(filter);
        this.allFilter.add(filter);
        return this;
    }
    
    /**
     * 添加多个or条件
     * @param filter 该条件
     * @return 链式调用
     */
    public  QueryParams<T> or(Filter ...filter){
        this.orFilters.addAll(Arrays.asList(filter));
        this.allFilter.addAll(Arrays.asList(filter));
        return this;
    }
    
    /**
     * 升序字段
     * @param property 该字段对应变量名
     * @return 链式调用
     */
    public  QueryParams<T> orderASC(String property){
        this.orders.add(Order.asc(property));
        return this;
    }
    
    /**
     * 降序字段
     * @param property 该字段对应变量名
     * @return 链式调用
     */
    public  QueryParams<T> orderDESC(String property){
        this.orders.add(Order.desc(property));
        return this;
    }

    /**
     * 清除所有条件
     * @return 该实例
     */
    public QueryParams<T> clearAll(){
        if (!this.andFilters.isEmpty()) this.andFilters.clear();
        if (!this.orFilters.isEmpty()) this.orFilters.clear();
        if (!this.orders.isEmpty()) this.orders.clear();
        if (!this.allFilter.isEmpty()) this.allFilter.clear();
        return this;
    }
    
    /**
     * 清除and条件
     * @return 该实例
     */
    public QueryParams<T> clearAnd(){
        if (!this.andFilters.isEmpty()) this.andFilters.clear();
        return this;
    }
    
    /**
     * 清除or条件
     * @return 该实例
     */
    public QueryParams<T> clearOr(){
        if (!this.orFilters.isEmpty()) this.andFilters.clear();
        return this;
    }
    
    /**
     * 清除order条件
     * @return 该实例
     */
    public QueryParams<T> clearOrder(){
        if (!this.orders.isEmpty()) this.orders.clear();
        return this;
    }


    /**
     * 清除AND条件的Filter对象
     * @param filter
     * @return
     */
    public QueryParams<T> clearAndFilter(Filter filter){
    	this.andFilters.remove(filter);
    	return this;
    }
    
    public List<Filter> getAndFilters() {
        return andFilters;
    }

    public void setAndFilters(List<Filter> andFilters) {
        this.andFilters = andFilters;
    }

    public List<Filter> getOrFilters() {
        return orFilters;
    }

    public void setOrFilters(List<Filter> orFilters) {
        this.orFilters = orFilters;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Filter> getAllFilter() {
		return allFilter;
	}
	public void setAllFilter(List<Filter> allFilter) {
		this.allFilter = allFilter;
	}
	/**
     * 生成条件的
     * @param root 该对象的封装
     * @param query 查询构建器
     * @param cb 构建器
     * @return 条件集合
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate restrictions = cb.and(toAndPredicate(root,cb));
        //如果无or条件,toOrPredicate会自带一个 or 1=1,前面用and的话则变为and 1=1造成错误,所以这里判断下
        //这个貌似和版本有关,公司线上版本出现这个问题,本地测试没问题
        if(!orders.isEmpty()){
            restrictions = cb.and(restrictions,toOrPredicate(root,cb));
        }
        query.orderBy(toOrders(root,cb));
        return restrictions;
    }

    /**
     * 获取Path
     *
     * @param path
     *            Path
     * @param propertyPath
     *            属性路径
     * @return Path
     */
    @SuppressWarnings("unchecked")
    private <X> Path<X> getPath(Path<?> path, String propertyPath) {
        if (path == null || StringUtils.isEmpty(propertyPath)) {
            return (Path<X>) path;
        }
        String property = StringUtils.substringBefore(propertyPath, PROPERTY_SEPARATOR);
        return getPath(path.get(property), StringUtils.substringAfter(propertyPath, PROPERTY_SEPARATOR));
    }

    /**
     * 转换为Predicate
     *
     * @param root
     *            Root
     * @return Predicate
     */
    @SuppressWarnings("unchecked")
    private Predicate toAndPredicate(Root<T> root,CriteriaBuilder criteriaBuilder) {
        Predicate restrictions = criteriaBuilder.conjunction();
        if (root == null || CollectionUtils.isEmpty(andFilters)) {
            return restrictions;
        }
        for (Filter filter : andFilters) {
            if (filter == null) {
                continue;
            }
            String property = filter.getProperty();
            Filter.Operator operator = filter.getOperator();
            Object value = filter.getValue();
            Boolean ignoreCase = filter.getIgnoreCase();
            Path<?> path = getPath(root, property);
            if (path == null) {
                continue;
            }
            
            Predicate in = null;
            
            // 比较查询条件 add by lion.chen （2017年7月26日16:50:27）
            if(operator == Filter.Operator.greaterThan
					|| operator == Filter.Operator.greaterThanOrEqualTo
					|| operator == Filter.Operator.lessThan
					|| operator == Filter.Operator.lessThanOrEqualTo){
            	Predicate comparePredicate = getComparePredicate(operator, value, path, criteriaBuilder);
            	if(comparePredicate != null){
            		restrictions = criteriaBuilder.and(restrictions, getComparePredicate(operator, value, path, criteriaBuilder));
            	}
            	continue;
            }
            
            switch (operator) {
                case eq:
                    if (value != null) {
                        if (BooleanUtils.isTrue(ignoreCase) && String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(path, value));
                        }
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, path.isNull());
                    }
                    break;
                case ne:
                    if (value != null) {
                        if (BooleanUtils.isTrue(ignoreCase) && String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(path, value));
                        }
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, path.isNotNull());
                    }
                    break;
                case gt_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt((Path<Number>) path, (Number) value));
                    	restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan((Path<String>) path, (String)value));
                    }
                    break;
                case lt_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt((Path<Number>) path, (Number) value));
                    	restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan((Path<String>) path, (String)value));
                    }
                    break;
                case ge_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo((Path<String>) path, (String) value));
                    }
                    break;
                case le_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo((Path<String>) path, (String) value));
                    }
                    break;
                case gt:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt((Path<Number>) path, (Number) value));
                    }
                    break;
                case lt:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt((Path<Number>) path, (Number) value));
                    }
                    break;
                case ge:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge((Path<Number>) path, (Number) value));
                    }
                    break;
                case le:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le((Path<Number>) path, (Number) value));
                    }
                    break;
                case like:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        if (BooleanUtils.isTrue(ignoreCase)) {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like((Path<String>) path, (String) value));
                        }
                    }
                    break;
                case in:
                	if(value instanceof Collection<?>){
                		in = path.in((Collection<?>)value);
                	}else if(value.getClass().isArray()){
                		in = path.in((Array)value);
                	}
                	if(in != null){
                		 restrictions = criteriaBuilder.and(restrictions, in);
                	}
                    break;
                case notIn:
                	if(value instanceof Collection<?>){
                		in = path.in((Collection<?>)value);
                	}else if(value.getClass().isArray()){
                		in = path.in((Array)value);
                	}
                	if(in != null){
                		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(in));
                	}
                    break;
                case isNull:
                    restrictions = criteriaBuilder.and(restrictions, path.isNull());
                    break;
                case isNotNull:
                    restrictions = criteriaBuilder.and(restrictions, path.isNotNull());
                    break;
			default:
				break;
            }
        }
        return restrictions;
    }
    
    /**
	 * 获取比较查询条件
	 * @param value
	 * @param root
	 * @param cb
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Predicate getComparePredicate(Filter.Operator operator, Object value, Path<?> path, CriteriaBuilder cb){
		
		Class<? extends Object> javaType = path.getJavaType();
		
		if(operator == Filter.Operator.greaterThan){
			
			if(javaType.isAssignableFrom(String.class)) return cb.greaterThan((Path<String>) path, (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.greaterThan((Path<Date>) path, (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.greaterThan((Path<java.sql.Date>) path, (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.greaterThan((Path<DateTime>) path, (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.greaterThan((Path<Timestamp>) path, (Timestamp) value);
		}
		
		else if(operator == Filter.Operator.greaterThanOrEqualTo){
			
			if(javaType.isAssignableFrom(String.class)) return cb.greaterThanOrEqualTo((Path<String>) path, (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.greaterThanOrEqualTo((Path<Date>) path, (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.greaterThanOrEqualTo((Path<java.sql.Date>) path, (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.greaterThanOrEqualTo((Path<DateTime>) path, (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.greaterThanOrEqualTo((Path<Timestamp>) path, (Timestamp) value);
		}
		
		else if(operator == Filter.Operator.lessThan){
			
			if(javaType.isAssignableFrom(String.class)) return cb.lessThan((Path<String>) path, (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.lessThan((Path<Date>) path, (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.lessThan((Path<java.sql.Date>) path, (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.lessThan((Path<DateTime>) path, (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.lessThan((Path<Timestamp>) path, (Timestamp) value);
		}
		
		else if(operator == Filter.Operator.lessThanOrEqualTo){
			
			if(javaType.isAssignableFrom(String.class)) return cb.lessThanOrEqualTo((Path<String>) path, (String) value);
			
			else if(javaType.isAssignableFrom(Date.class)) return cb.lessThanOrEqualTo((Path<Date>) path, (Date) value);
			
			else if(javaType.isAssignableFrom(java.sql.Date.class)) return cb.lessThanOrEqualTo((Path<java.sql.Date>) path, (java.sql.Date) value);
			
			else if(javaType.isAssignableFrom(DateTime.class)) return cb.lessThanOrEqualTo((Path<DateTime>) path, (DateTime) value);
			
			else if(javaType.isAssignableFrom(Timestamp.class)) return cb.lessThanOrEqualTo((Path<Timestamp>) path, (Timestamp) value);
		}
		
		return null;
	}
    
    /**
     * 转换为Predicate
     *
     * @param root
     *            Root
     * @return Predicate
     */
    @SuppressWarnings("unchecked")
    private Predicate toOrPredicate(Root<T> root,CriteriaBuilder criteriaBuilder) {
        Predicate restrictions = criteriaBuilder.disjunction();
        if (root == null || CollectionUtils.isEmpty(andFilters)) {
            return restrictions;
        }
        for (Filter filter : orFilters) {
            if (filter == null) {
                continue;
            }
            String property = filter.getProperty();
            Filter.Operator operator = filter.getOperator();
            Object value = filter.getValue();
            Boolean ignoreCase = filter.getIgnoreCase();
            Path<?> path = getPath(root, property);
            if (path == null) {
                continue;
            }
            switch (operator) {
                case eq:
                    if (value != null) {
                        if (BooleanUtils.isTrue(ignoreCase) && String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                            restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.equal(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.equal(path, value));
                        }
                    } else {
                        restrictions = criteriaBuilder.or(restrictions, path.isNull());
                    }
                    break;
                case ne:
                    if (value != null) {
                        if (BooleanUtils.isTrue(ignoreCase) && String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                            restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.notEqual(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.notEqual(path, value));
                        }
                    } else {
                        restrictions = criteriaBuilder.or(restrictions, path.isNotNull());
                    }
                    break;
                case gt_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                    	restrictions = criteriaBuilder.or(restrictions,criteriaBuilder.greaterThan((Path<String>) path, (String)value));
                    }
                    break;
                case lt_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                    	restrictions = criteriaBuilder.or(restrictions,criteriaBuilder.lessThan((Path<String>) path, (String)value));
                    }
                    break;
                case ge_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.greaterThanOrEqualTo((Path<String>) path, (String) value));
                    }
                    break;
                case le_time:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.lessThanOrEqualTo((Path<String>) path, (String) value));
                    }
                    break;
                case gt:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.gt((Path<Number>) path, (Number) value));
                    }
                    break;
                case lt:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.lt((Path<Number>) path, (Number) value));
                    }
                    break;
                case ge:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.ge((Path<Number>) path, (Number) value));
                    }
                    break;
                case le:
                    if (Number.class.isAssignableFrom(path.getJavaType()) && value instanceof Number) {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.le((Path<Number>) path, (Number) value));
                    }
                    break;
                case like:
                    if (String.class.isAssignableFrom(path.getJavaType()) && value instanceof String) {
                        if (BooleanUtils.isTrue(ignoreCase)) {
                            restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.like(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                        } else {
                            restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.like((Path<String>) path, (String) value));
                        }
                    }
                    break;
                case in:
                    restrictions = criteriaBuilder.or(restrictions, path.in(value));
                    break;
                case isNull:
                    restrictions = criteriaBuilder.or(restrictions, path.isNull());
                    break;
                case isNotNull:
                    restrictions = criteriaBuilder.or(restrictions, path.isNotNull());
                    break;
			default:
				break;
            }
        }
        return restrictions;
    }

    /**
     * 转换为Order
     *
     * @param root
     *            Root
     * @return Order
     */
    private List<javax.persistence.criteria.Order> toOrders(Root<T> root,CriteriaBuilder criteriaBuilder) {
        List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
        if (root == null || CollectionUtils.isEmpty(orders)) {
            return orderList;
        }
        for (Order order : orders) {
            if (order == null) {
                continue;
            }
            String property = order.getProperty();
            Order.Direction direction = order.getDirection();
            Path<?> path = getPath(root, property);
            if (path == null || direction == null) {
                continue;
            }
            switch (direction) {
                case asc:
                    orderList.add(criteriaBuilder.asc(path));
                    break;
                case desc:
                    orderList.add(criteriaBuilder.desc(path));
                    break;
            }
        }
        return orderList;
    }

}