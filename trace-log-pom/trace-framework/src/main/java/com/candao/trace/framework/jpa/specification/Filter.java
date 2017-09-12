package com.candao.trace.framework.jpa.specification;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Filter implements Serializable {
	
	private static final long serialVersionUID = -8712382358441065075L;
	
	private static final String mark_percent = "%";

	/**
	 * 运算符
	 */
	public enum Operator {
		
		/** 等于 */
		eq(" = "),

		/** 不等于 */
		ne(" != "),

		/** 大于 */
		gt(" > "),

		/** 小于 */
		lt(" < "),

		/** 大于等于 */
		ge(" >= "),

		/** 小于等于 */
		le(" <= "),
		
		/** 大于 时间比较 */
		gt_time(" > "),
		
		/** 小于 时间比较*/
		lt_time(" < "),

		/** 大于等于 时间比较*/
		ge_time(" >= "),

		/** 小于等于 时间比较*/
		le_time(" <= "),

		/** 类似 */
		like(" like "), leftLike(" leftLike "), rightLike(" rightLike "),
		
		greaterThan(" greaterThan "), greaterThanOrEqualTo(" greaterThanOrEqualTo "), lessThan(" lessThan "),  lessThanOrEqualTo(" lessThanOrEqualTo "), 

		/** 包含 */
		in(" in "),
		
		/** 不包含 */
		notIn(" not in "),
		
		/** 为Null */
		isNull(" is NULL "),

		/** 不为Null */
		isNotNull(" is not NULL ");

		Operator(String operator) {
			this.operator = operator;
		}

		private String operator;

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}
	}

	/** 默认是否忽略大小写 */
	private static final boolean DEFAULT_IGNORE_CASE = false;

	/** 属性 */
	private String property;

	/** 运算符 */
	private Filter.Operator operator;

	/** 值 */
	private Object value;

	/** 是否忽略大小写 */
	private Boolean ignoreCase = DEFAULT_IGNORE_CASE;

	/**
	 * 构造方法
	 */
	public Filter() {}

	/**
	 * 构造方法
	 *
	 * @param property
	 *            属性
	 * @param operator
	 *            运算符
	 * @param value
	 *            值
	 */
	public Filter(String property, Filter.Operator operator, Object value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
	}

	/**
	 * 构造方法
	 *
	 * @param property
	 *            属性
	 * @param operator
	 *            运算符
	 * @param value
	 *            值
	 * @param ignoreCase
	 *            忽略大小写
	 */
	public Filter(String property, Filter.Operator operator, Object value, boolean ignoreCase) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * 返回等于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 等于筛选
	 */
	public static Filter eq(String property, Object value) {
		return new Filter(property, Filter.Operator.eq, value);
	}

	/**
	 * 返回等于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @param ignoreCase
	 *            忽略大小写
	 * @return 等于筛选
	 */
	public static Filter eq(String property, Object value, boolean ignoreCase) {
		return new Filter(property, Filter.Operator.eq, value, ignoreCase);
	}

	/**
	 * 返回不等于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 不等于筛选
	 */
	public static Filter ne(String property, Object value) {
		return new Filter(property, Filter.Operator.ne, value);
	}

	/**
	 * 返回不等于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @param ignoreCase
	 *            忽略大小写
	 * @return 不等于筛选
	 */
	public static Filter ne(String property, Object value, boolean ignoreCase) {
		return new Filter(property, Filter.Operator.ne, value, ignoreCase);
	}

	/**
	 * 返回大于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于筛选
	 */
	public static Filter gt(String property, Object value) {
		return new Filter(property, Filter.Operator.gt, value);
	}

	/**
	 * 返回小于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于筛选
	 */
	public static Filter lt(String property, Object value) {
		return new Filter(property, Filter.Operator.lt, value);
	}

	/**
	 * 返回大于等于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于等于筛选
	 */
	public static Filter ge(String property, Object value) {
		return new Filter(property, Filter.Operator.ge, value);
	}

	/**
	 * 返回小于等于筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于等于筛选
	 */
	public static Filter le(String property, Object value) {
		return new Filter(property, Filter.Operator.le, value);
	}
	
	/**
	 * 返回大于筛选,仅作为时间处理
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于筛选
	 */
	public static Filter gt_time(String property, Object value) {
		return new Filter(property, Filter.Operator.gt_time, value);
	}

	/**
	 * 返回小于筛选,仅作为时间处理
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于筛选
	 */
	public static Filter lt_time(String property, Object value) {
		return new Filter(property, Filter.Operator.lt_time, value);
	}

	/**
	 * 返回大于等于筛选,仅作为时间处理
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于等于筛选
	 */
	public static Filter ge_time(String property, Object value) {
		return new Filter(property, Filter.Operator.ge_time, value);
	}

	/**
	 * 返回小于等于筛选,仅作为时间处理
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于等于筛选
	 */
	public static Filter le_time(String property, Object value) {
		return new Filter(property, Filter.Operator.le_time, value);
	}
	
	/**
	 * 比较运算查询
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于筛选
	 */
	public static Filter greaterThan(String property, Object value) {
		return new Filter(property, Filter.Operator.greaterThan, value);
	}
	
	/**
	 * 比较运算查询
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于等于筛选
	 */
	public static Filter greaterThanOrEqualTo(String property, Object value) {
		return new Filter(property, Filter.Operator.greaterThanOrEqualTo, value);
	}
	
	/**
	 * 比较运算查询
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于筛选
	 */
	public static Filter lessThan(String property, Object value) {
		return new Filter(property, Filter.Operator.lessThan, value);
	}
	
	/**
	 * 比较运算查询
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于等于筛选
	 */
	public static Filter lessThanOrEqualTo(String property, Object value) {
		return new Filter(property, Filter.Operator.lessThanOrEqualTo, value);
	}
	
	/**
	 * 返回相似筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 相似筛选
	 */
	public static Filter like(String property, String value) {
		return new Filter(property, Filter.Operator.like, mark_percent + value + mark_percent);
	}
	
	/**
	 * 返回相似筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 相似筛选
	 */
	public static Filter leftLike(String property, String value) {
		return new Filter(property, Filter.Operator.like, mark_percent + value);
	}
	
	/**
	 * 返回相似筛选
	 *
	 * @param property 属性
	 * @param value 值
	 * @return 相似筛选
	 */
	public static Filter rightLike(String property, String value) {
		return new Filter(property, Filter.Operator.like, value + mark_percent);
	}

	/**
	 * 返回包含筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 包含筛选
	 */
	public static Filter in(String property, Object value) {
		return new Filter(property, Filter.Operator.in, value);
	}
	
	/**
	 * 返回不包含筛选
	 *
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 包含筛选
	 */
	public static Filter notIn(String property, Object value) {
		return new Filter(property, Filter.Operator.notIn, value);
	}
	
	/**
	 * 返回为Null筛选
	 *
	 * @param property
	 *            属性
	 * @return 为Null筛选
	 */
	public static Filter isNull(String property) {
		return new Filter(property, Filter.Operator.isNull, null);
	}

	/**
	 * 返回不为Null筛选
	 *
	 * @param property
	 *            属性
	 * @return 不为Null筛选
	 */
	public static Filter isNotNull(String property) {
		return new Filter(property, Filter.Operator.isNotNull, null);
	}

	/**
	 * 返回忽略大小写筛选
	 *
	 * @return 忽略大小写筛选
	 */
	public Filter ignoreCase() {
		this.ignoreCase = true;
		return this;
	}

	/**
	 * 获取属性
	 *
	 * @return 属性
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * 设置属性
	 *
	 * @param property
	 *            属性
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 获取运算符
	 *
	 * @return 运算符
	 */
	public Filter.Operator getOperator() {
		return operator;
	}

	/**
	 * 设置运算符
	 *
	 * @param operator
	 *            运算符
	 */
	public void setOperator(Filter.Operator operator) {
		this.operator = operator;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 设置值
	 *
	 * @param value
	 *            值
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 获取是否忽略大小写
	 *
	 * @return 是否忽略大小写
	 */
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * 设置是否忽略大小写
	 *
	 * @param ignoreCase
	 *            是否忽略大小写
	 */
	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	/**
	 * 重写equals方法
	 *
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Filter other = (Filter) obj;
		return new EqualsBuilder().append(getProperty(), other.getProperty()).append(getOperator(), other.getOperator()).append(getValue(), other.getValue()).isEquals();
	}

	/**
	 * 重写hashCode方法
	 *
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getProperty()).append(getOperator()).append(getValue()).toHashCode();
	}
}