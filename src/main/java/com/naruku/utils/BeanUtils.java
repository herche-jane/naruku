package com.naruku.utils;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

/**
 * bean工具类
 * @author herche 
 * @data 2022/10/27
 */
public class BeanUtils {
	/**
	 * 相同类对象的深拷贝
	 * @param obj1 对象1 源对象
	 * @param obj2 对象2
	 */
	public static  void deepCopySameClass(Object obj1,Object obj2) throws IllegalAccessException {
		if (ObjectUtils.isEmpty(obj1) || ObjectUtils.isEmpty(obj2)){
			return;
		}
		if (obj1.getClass() != obj2.getClass()){
			return;
		}
		Field[] fields = obj2.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Object o = field.get(obj1);
			if (!ObjectUtils.isEmpty(o)){
				field.set(obj2,o);
			}
		}
	}
}
