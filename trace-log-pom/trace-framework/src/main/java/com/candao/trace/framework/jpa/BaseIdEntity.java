package com.candao.trace.framework.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.candao.trace.framework.spring.bean.FastActionContext;
import com.candao.trace.framework.util.FastClassUtils;
import com.candao.trace.framework.util.FastClassUtils.FieldMethodFilter;
import com.candao.trace.framework.util.FastStringUtils;

/**
 * 数据库封装父级类
 * 	-适用于int id主键、create_time表结构
 * @author lion.chen
 * @version 1.0.0 2017年6月16日 下午6:28:20
 */
public class BaseIdEntity implements Serializable{
	
	private static final long serialVersionUID = 3011056011505594561L;
	
	// 设置保存当前账号信息字段配置
	private static final String setAccountInfoFieldNames = "createBy,updateBy,createTime,updateTime";
	private static final String field_name_createBy = "createBy";
	private static final String field_name_updateBy = "updateBy";
	private static final String field_name_createTime = "createTime";
	private static final String field_name_updateTime = "updateTime";
	private static final String k_id = "id";
	
	public static final String CUST_DECODE_KEY = "irms-db-data-decode-key";
	
	private static final FieldMethodFilter accountInfoFieldNameFieldMethodFilter = new FieldMethodFilter() {
		@Override
		public boolean valid(Field f, Method m) {
			return f != null && setAccountInfoFieldNames.indexOf(f.getName()) > -1;
		}
	};
	
	/**
	 * 超级管理角色ID
	 */
	public static final int super_role_id = 1;
	
	/**
	 * 超级管理员账号
	 */
	public static final int super_account_id = 1;
	
	/**
	 * 系统管理 运营商ID
	 */
	public static final int sys_platform_id = 1;
	
	/**
	 * 麦当劳运营商ID
	 */
	public static final int mcd_platform_id = 2;
	
	public static final String defalut_pwd = "123456a";
	
	/**
	 * 系统管理 组织结构ID
	 */
	public static final int sys_org_id = 1;
	
	@JSONField(serialize = false, deserialize = false)
	private Boolean isAdd = null;
	
	/**
	 * 是否为超级管理角色
	 * @param roleId
	 * @return
	 */
	public static boolean isSuperRole(Integer roleId){
		
		return roleId != null && super_role_id == roleId; 
	}
	
	/**
	 * 是否为超级管理员账号
	 * @return
	 */
	public static boolean isSuperAdmin(){
		
		FastActionContext ctx = FastActionContext.getActionContext();
		
		if(ctx != null){
			return isSuperAdmin(ctx.getCurrentAccountId());
		}
		
		return false;
	}
	
	/**
	 * 是否为超级管理员账号
	 * @param accountId
	 * @return
	 */
	public static boolean isSuperAdmin(Integer accountId){
		
		return accountId != null && accountId == super_account_id; 
	}
	
	/**
	 * 是否为超级管理员账号
	 * @param accountId
	 * @return
	 */
	public static boolean isSuperOrg(Integer orgId){
		
		return orgId != null && orgId == sys_org_id; 
	}
	
	/**
	 * 是否为超级管理员角色
	 * @param roleIds
	 * @return
	 */
	public static boolean isSuperRole(List<Integer> roleIds){
		
		return FastStringUtils.isNotEmpty(roleIds) && roleIds.contains(super_role_id); 
	}
	
	/**
	 * 是否为系统运营商
	 * @param platformId
	 * @return
	 */
	public static boolean isSysPlatform(Integer platformId){
		
		return platformId != null && platformId == sys_platform_id;
	}
	
	/**
	 * 是否为系统运营商
	 * @return
	 */
	public static boolean isSysPlatform(){
		
		FastActionContext ctx = FastActionContext.getActionContext();
		
		if(ctx != null){
			return isSysPlatform(ctx.getCurrentPlatformId());
		}
		
		return false;
	}
	
	/**
	 * 对应数据库实体设置账号信息
	 * -针对BaseIdEntity实体
	 * -针对createBy、updateBy、createTime、updateTime设置数据
	 * @param obj
	 */
	public static void setSaveAccountInfo(BaseIdEntity obj){
		
		if(obj != null){
			
			FastActionContext ctx = FastActionContext.getActionContext();
			String currentLoginName = FastStringUtils.EMPTY;
			
			if(ctx != null){
				currentLoginName = ctx.getCurrentLoginName();
			}
			
			boolean isAdd = obj.isAdd();
			
			Map<Field, Method> fieldSetMethods = FastClassUtils.findFieldSetMethods(obj.getClass(), accountInfoFieldNameFieldMethodFilter);
			Map<Field, Method> fieldGetMethods = FastClassUtils.findFieldGetMethods(obj.getClass(), accountInfoFieldNameFieldMethodFilter);
			
			boolean isSetCreateBy = true;
			boolean isSetUpdateBy = true;
			if(fieldGetMethods != null && !fieldGetMethods.isEmpty()){
				Iterator<Field> iterator = fieldGetMethods.keySet().iterator();
				while(iterator.hasNext()){
					try{
						Field field = iterator.next();
						Method method = fieldGetMethods.get(field);
						if(field != null && method != null){
							String name = field.getName();
							
							// 创建人
							if(FastStringUtils.equalsIgnoreCase(field_name_createBy, name)){
								if(method.invoke(obj) != null){
									isSetCreateBy = false;
									continue;
								}
							}
							
							// 最后修改人
							if(FastStringUtils.equalsIgnoreCase(field_name_updateBy, name)){
								if(method.invoke(obj) != null){
									isSetUpdateBy = false;
									continue;
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			if(fieldSetMethods != null && !fieldSetMethods.isEmpty()){
				Iterator<Field> iterator = fieldSetMethods.keySet().iterator();
				while(iterator.hasNext()){
					try {
						Field field = iterator.next();
						Method method = fieldSetMethods.get(field);
						if(field != null && method != null){
							
							String name = field.getName();
							
							// 创建时间、修改时间
							if(StringUtils.equalsIgnoreCase(field_name_createTime, name) 
									|| StringUtils.equalsIgnoreCase(field_name_updateTime, name)){
								Class<?> type = field.getType();
								if(type.equals(Timestamp.class)){
									Timestamp timestamp = null;
									method.invoke(obj, timestamp);
								}else if(type.equals(Date.class)){
									Date date = null;
									method.invoke(obj, date);
								}else if(type.equals(String.class)){
									String time = null;
									method.invoke(obj, time);
								}
								continue;
							}
							
							String nullStr = null;
							
							// 创建人
							if(FastStringUtils.equalsIgnoreCase(field_name_createBy, name) && isSetCreateBy){
								method.invoke(obj, isAdd ? currentLoginName : nullStr);
								continue;
							}
							
							// 最后修改人
							if(FastStringUtils.equalsIgnoreCase(field_name_updateBy, name) && isSetUpdateBy){
								method.invoke(obj, isAdd ? StringUtils.EMPTY : currentLoginName);
								continue;
							}
						}
					
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		}
	}
	
	/**
	 * 是否为新增数据
	 * @return
	 */
	@JSONField(serialize = false, deserialize = false)
	public boolean isAdd(){
		
		if(isAdd == null){
			try{
				
				Class<?> clazz = getClass();
				
				Map<Field, Method> fieldGetMethods = FastClassUtils.findFieldGetMethods(clazz, new FieldMethodFilter() {
					public boolean valid(Field f, Method m) {
						return FastStringUtils.equalsIgnoreCase(k_id, f.getName());
					}
				});
				
				if(fieldGetMethods != null && !fieldGetMethods.isEmpty()){
					Iterator<Field> iterator = fieldGetMethods.keySet().iterator();
					if(iterator.hasNext()){
						Field next = iterator.next();
						Object idValue = null;
						if(next.isAccessible()){
							idValue = next.get(this);
						}else{
							Method method = fieldGetMethods.get(next);
							if(method != null){
								idValue = method.invoke(this);
							}
						}
						if(idValue == null){
							isAdd = true;
						}else{
							if(idValue instanceof Number){
								Number idNumberValue = (Number) idValue;
								if(idNumberValue.longValue() == 0){
									isAdd = true;
								}else{
									isAdd = false;
								}
							}else{
								isAdd = false;
							}
						}
					}
				}
			}catch(Throwable e){
				e.printStackTrace();
				isAdd = true;
			}
		}
		
		return isAdd.booleanValue();
	}
	
	/**
	 * 是否为修改
	 * @return
	 */
	@JSONField(serialize = false, deserialize = false)
	public boolean isUpdate(){
		return !isAdd();
	}
	
}
