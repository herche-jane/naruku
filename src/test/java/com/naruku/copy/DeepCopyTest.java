package com.naruku.copy;

import com.naruku.dbReserved.entity.TableInfo;
import com.naruku.utils.BeanUtils;
import org.junit.Test;

/**
 * 深拷贝测试
 */
public class DeepCopyTest {
	@Test
	public void deepCopyTest() throws IllegalAccessException {
		TableInfo tableInfo = new TableInfo();
		tableInfo.setInsert(Boolean.TRUE);
		tableInfo.setCreate(Boolean.TRUE);
		TableInfo tableInfo1 = new TableInfo();
		BeanUtils.deepCopySameClass(tableInfo,tableInfo1);
		System.out.println(tableInfo1);
	}
}
