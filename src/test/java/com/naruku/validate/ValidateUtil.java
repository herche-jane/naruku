package com.naruku.validate;

import com.naruku.dbReserved.entity.TableIndexInfo;
import com.naruku.utils.ValidateUtils;
import org.junit.Test;

public class ValidateUtil {
	@Test
	public void test(){
		TableIndexInfo tableIndexInfo = new TableIndexInfo();
		tableIndexInfo.setIndexFields("1");
//		tableIndexInfo.setIndexStyle("111");
		boolean emptyOrValidateField = ValidateUtils.isEmptyOrValidateField(tableIndexInfo, "indexFields", "indexStyle");
		if (emptyOrValidateField){
			System.out.println("空");
		} else {
			System.out.println("Ok");
		}
	}
}
