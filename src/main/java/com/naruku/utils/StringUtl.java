package com.naruku.utils;

import org.springframework.util.StringUtils;

public class StringUtl extends StringUtils {
	
	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
	 *
	 * @param str     指定字符串
	 * @param strings 需要检查的字符串数组
	 */
	public static boolean containAny(String str, String... strings) {
		if ("".equals(str) || strings == null || strings.length == 0) {
			return false;
		}
		for (String string : strings) {
			if (str.contains(string)) return true;
		}
		return false;
	}
	
	/**
	 * @param rowSciptJsonString 原字符串 含有json
	 * @param strings            需要被替换的字符串集合 格式为：eg: job.content.reader.parameter.host:100 ...
	 * @return
	 */
	public static String replaceString(String rowSciptJsonString, String... strings) {
		if (strings == null || strings.length == 0) {
			return rowSciptJsonString;
		}
		for (String string : strings) {
			rowSciptJsonString = replaceString(rowSciptJsonString, string);
		}
		return rowSciptJsonString;
	}
	
	private static String replaceString(String rowSciptJsonString, String string) {
		// 有没有值
		if (!string.contains(":")) {
			return rowSciptJsonString;
		}
		String[] split = string.split("\\.");
		if (split == null || split.length == 0) {
			return rowSciptJsonString;
		}
		return doReplaceString(rowSciptJsonString, split, ":");
	}
	
	/**
	 * 替换的真正部分
	 *
	 * @param rowSciptJsonString json
	 * @param split              需要替换的位置
	 * @return
	 */
	private static String doReplaceString(String rowSciptJsonString, String[] split, String spit) {
		String[] map = null;
		String string = rowSciptJsonString;
		for (int i = 0; i < split.length; i++) {
			// 开始找
			if (i == split.length - 1) {
				// 最后一个特殊处理
				map = split[i].split(spit);
				if (map != null && map.length == 2) {
					// 验空
					if (!"".equals(map[0]) && !"".equals(map[1])) {
						string = string.substring(string.indexOf(map[0]) - 1);
					} else return rowSciptJsonString;
				} else return rowSciptJsonString;
			} else {
				if (!string.contains(split[i]) || string.indexOf(split[i]) == 0) {
					return rowSciptJsonString;
				} else {
					string = string.substring(string.indexOf(split[i]) - 1);
				}
				
			}
		}

//		System.out.println(string);
//		// 拿到 最近的一个",
		string = string.substring(0, string.indexOf(","));
		String value = string.replace(" ", "").replace("\\", "").replace("\n", "");
		int i = value.indexOf(":");
		value = value.substring(i + 1);
		if (value.contains("[") && value.contains("]")) {
			int left = value.indexOf("[");
			int right = value.indexOf("]");
			
			value = value.substring(left + 2, right - 1);
		}
		// 分类
		int i1 = value.indexOf("\"");
//		"192.168.xxxx"
		if (i1 >= 0) {
			value = value.substring(i1 + 1, value.length() - 1);
		} else {
//			value = value.substring(i);
		}
		assert map != null;
		String replace = string.replace(value, map[1]);
		rowSciptJsonString = rowSciptJsonString.replace(string, replace);
//		// 此时 格式为 "key":"value"
//		int i = string.indexOf(":");
//		String substring = string.substring(i + 1);
//		substring = substring.substring(1, substring.length() - 1);
//		assert map != null;
//		String replace = string.replace(substring, map[1]);
//		rowSciptJsonString = rowSciptJsonString.replace(string,replace);
		return rowSciptJsonString;
	}
	
	public static String toStringOrNull(Object obj) {
		return null == obj ? null : obj.toString();
	}
	
	public static boolean equals(String str1, String str2) {
		if (!hasText(str1) || !hasText(str2)){
			return false;
		}
		if (str1.equalsIgnoreCase(str2)){
			return true;
		}else return false;
	}
}
