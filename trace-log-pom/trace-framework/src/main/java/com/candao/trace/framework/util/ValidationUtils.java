package com.candao.trace.framework.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;

/**
 * 元数据验证工具类
 * 
 */
public class ValidationUtils {

	/**
	 * 验证数据
	 * 返回错误信息集合
	 * @param model
	 * @return
	 */
	public static List<String> validateModel(Object model){
		return validateModel(model, true);
	}
	
	/**
	 * 验证数据
	 * 返回错误信息集合
	 * @param model
	 * @param validNull
	 * @return
	 */
	public static List<String> validateModel(Object model, boolean validNull) {

		List<String> msgList = new ArrayList<String>();
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(model);

		if(constraintViolations != null && !constraintViolations.isEmpty()){
			
			Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();
			
			while (iter.hasNext()) {
				
				ConstraintViolation<Object> next = iter.next();
				Object invalidValue = next.getInvalidValue();
				
				if(validNull || invalidValue != null){
					String message = next.getMessage();
					msgList.add(message);
				}
			}
		}
		
		return msgList;
	}
	
	/**
	 * 验证数据并返回错误信息，多个以分号隔开
	 * @param model
	 * @return
	 */
	public static String validateModel2Msg(Object model) {
		
		return validateModel2Msg(model, true);
	}
	
	/**
	 * 验证数据并返回错误信息，多个以分号隔开
	 * @param model
	 * @param validNull
	 * @return
	 */
	public static String validateModel2Msg(Object model, boolean validNull) {
		
		List<String> msgList = validateModel(model, validNull);
		
		if(msgList != null && !msgList.isEmpty()){
			return StringUtils.join(msgList, ";");
		}
		
		return null;
	}

}
