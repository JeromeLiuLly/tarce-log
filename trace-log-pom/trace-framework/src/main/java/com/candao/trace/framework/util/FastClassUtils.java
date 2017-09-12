package com.candao.trace.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

/**
 * class反射工具类
 *
 */
public class FastClassUtils {

	private static final String GET = "get";
	private static final String SET = "set";
	
	/**
	 * 实体转换为Map
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> obj2Map(Object obj){
		return obj2Map(obj, false, false);
	}
	
	/**
	 * 实体转换为Map
	 * @param obj
	 * @param getNull
	 * @return
	 */
	public static Map<String, Object> obj2Map(Object obj, boolean getNull){
		return obj2Map(obj, getNull, false);
	}
	
	/**
	 * 实体转换为Map
	 * @param obj
	 * @param getNull
	 * @param addStatic
	 * @return
	 */
	public static Map<String, Object> obj2Map(Object obj, boolean getNull, boolean addStatic){
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Field, Method> fieldGetMethods = findFieldGetMethods(obj.getClass());
		
		if(fieldGetMethods != null && !fieldGetMethods.isEmpty()){
			
			Iterator<Field> iterator = fieldGetMethods.keySet().iterator();
			
			while(iterator.hasNext()){
				
				try {
					
					Field field = iterator.next();
					Method method = fieldGetMethods.get(field);
					Object value = null;
					int fieldModifiers = field.getModifiers();
					
					if(Modifier.isStatic(fieldModifiers) && !addStatic) continue;
					
					boolean isAdd = false;
					if(Modifier.isPrivate(fieldModifiers) && method != null){
						
						int methodModifiers = method.getModifiers();
						if(Modifier.isPublic(methodModifiers)){
							isAdd = true;
							value = method.invoke(obj);
						}
						
					}
					
					else if(Modifier.isPublic(fieldModifiers)){
						isAdd = true;
						value = field.get(obj);
					}
					
					if(!getNull && value == null) isAdd = false;
					
					if(isAdd) map.put(field.getName(), value);
					
				} catch (Throwable e) {} 
			}
		}
		
		return map;
	}
	
	/**
	 * 获取一个类的所有属性
	 * @param clazz
	 * @return
	 */
	public static Collection<Field> getAllFields(Class<?> clazz) {
		
		Map<String, Field> resutlMap = new LinkedHashMap<String, Field>();
		
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				resutlMap.put(field.getName(), field);
			}
		}
		
		return resutlMap.values();
	}
	
	/**
	 * 获取当前class所有属性get方法
	 * @param clazz
	 * @return
	 */
	public static Map<Field, Method> findFieldGetMethods(Class<?> clazz){
		
		return findFieldGetMethods(getAllFields(clazz), clazz, null);
	}
	
	/**
	 * 获取当前class所有属性set方法
	 * @param clazz
	 * @return
	 */
	public static Map<Field, Method> findFieldSetMethods(Class<?> clazz){
		
		return findFieldSetMethods(getAllFields(clazz), clazz, null);
	}
	
	/**
	 * 获取当前class所有属性get方法
	 * @param clazz
	 * @param filter
	 * @return
	 */
	public static Map<Field, Method> findFieldGetMethods(Class<?> clazz, FieldMethodFilter filter){
		
		return findFieldGetMethods(getAllFields(clazz), clazz, filter);
	}
	
	/**
	 * 获取当前class所有属性set方法
	 * @param clazz
	 * @param filter
	 * @return
	 */
	public static Map<Field, Method> findFieldSetMethods(Class<?> clazz, FieldMethodFilter filter){
		
		return findFieldSetMethods(getAllFields(clazz), clazz, filter);
	}
	
	/**
	 * 获取当前class所有属性get方法
	 * @param fields
	 * @param clazz
	 * @param filter
	 * @return
	 */
	public static Map<Field, Method> findFieldGetMethods(Collection<Field> fields, Class<?> clazz, FieldMethodFilter filter){
		
		final Map<Field, Method> resutlMap = new LinkedHashMap<Field, Method>();
		
		fields.forEach(new Consumer<Field>() {
			@Override
			public void accept(Field t) {
				String name = t.getName();
				if(StringUtils.isBlank(name)) return;
				String methodName = new StringBuffer(GET).append(String.valueOf(name.charAt(0)).toUpperCase()).append(name.substring(1)).toString();
				Method method = null;
				try {
					method = clazz.getMethod(methodName);
				} catch (Throwable e) {
				}finally{
					if(filter == null || filter.valid(t, method)){
						resutlMap.put(t, method);
					}
				}
			}
		});
		
		return resutlMap;
	}
	
	/**
	 * 获取当前class所有属性set方法
	 * @param fields
	 * @param clazz
	 * @param filter
	 * @return
	 */
	public static Map<Field, Method> findFieldSetMethods(Collection<Field> fields, Class<?> clazz, FieldMethodFilter filter){
		
		final Map<Field, Method> resutlMap = new LinkedHashMap<Field, Method>();
		
		fields.forEach(new Consumer<Field>() {
			@Override
			public void accept(Field t) {
				String name = t.getName();
				if(StringUtils.isBlank(name)) return;
				String methodName = new StringBuffer(SET).append(String.valueOf(name.charAt(0)).toUpperCase()).append(name.substring(1)).toString();
				Method method = null;
				try {
					method = clazz.getMethod(methodName, t.getType());
				} catch (Throwable e) {
				}finally{
					if(filter == null || filter.valid(t, method)){
						resutlMap.put(t, method);
					}
				}
			}
		});
		
		return resutlMap;
	}
	
	/**
	 * findFieldGetMethods - 过滤器
	 * @author lion.chen
	 *
	 */
	public interface FieldMethodFilter{
		boolean valid(Field f, Method m);
	}
	
}
