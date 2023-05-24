package com.naruku.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

/**
 * 一个个判断太麻烦，用于类的判断
 * @author herche
 * @date 2022/10/31
 */
public class ValidateUtils {
	private static Logger logger = LoggerFactory.getLogger(ValidateUtils.class);
	/**
	 * 判断这个类是否为空 和 指定字段是否为空
	 * @param obj
	 * @param fieldNames
	 * @return
	 */
	public static boolean isEmptyOrValidateField(Object obj,String ... fieldNames){
		if (ObjectUtils.isEmpty(obj))return true;
		Class<?> aClass = obj.getClass();
		if (!ObjectUtils.isEmpty(fieldNames)){
			for (String fieldName : fieldNames) {
				try {
					Field declaredField = aClass.getDeclaredField(fieldName);
					declaredField.setAccessible(true);
					Object value = declaredField.get(obj);
					if (ObjectUtils.isEmpty(value))return true;
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					logger.error("没有这个字段，请输入正确的字符名");
					return true;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					logger.error("获取字段值出错了！");
					return true;
				}
			}
		}  else {
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field declaredField : declaredFields) {
				declaredField.setAccessible(true);
				Object value = null;
				try {
					value = declaredField.get(obj);
				} catch (IllegalAccessException e) {
					logger.error("获取字段值出错了！");
					return true;
				}
				if (ObjectUtils.isEmpty(value))return true;
			}
		}
		
		return false;
	}
}
