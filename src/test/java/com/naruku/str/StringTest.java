package com.naruku.str;

import com.naruku.utils.StringUtl;
import org.junit.Test;

public class StringTest {
	@Test
	public void test(){
		String localHost = "8888";
		String localUserName = "herche";
		String localPassword = "123456";
		int localPort = 19;
		String lastString = "  \\\\\\\\\"job\\\\\\\\\": {\n" +
				"    \\\\\\\\\"content\\\\\\\\\": [\n" +
				"      {\n" +
				"        \\\\\\\\\"reader\\\\\\\\\": {\n" +
				"          \\\\\\\\\"name\\\\\\\\\": \\\\\\\\\"ftpreader\\\\\\\\\",\n" +
				"          \\\\\\\\\"parameter\\\\\\\\\": {\n" +
				"            \\\\\\\\\"protocol\\\\\\\\\": \\\\\\\\\"sftp\\\\\\\\\",\n" +
				"            \\\\\\\\\"host\\\\\\\\\": \\\\\\\\\"192.168.5.51\\\\\\\\\",\n" +
				"            \\\\\\\\\"port\\\\\\\\\": 99,\n" +
				"            \\\\\\\\\"username\\\\\\\\\": \\\\\\\\\"root\\\\\\\\\",\n" +
				"            \\\\\\\\\"password\\\\\\\\\": \\\\\\\\\"Data2020!\\\\\\\\\",\n" +
				"            \\\\\\\\\"path\\\\\\\\\": [\n" +
				"              \\\\\\\\\"/usr/kbox/ccc.xlsx\\\\\\\\\"\n" +
				"            ],\n" +
				"            \\\\\\\\\"column\\\\\\\\\": [\n" +
				"              {\n" +
				"                \\\\\\\\\"index\\\\\\\\\": 0,\n" +
				"                \\\\\\\\\"type\\\\\\\\\": \\\\\\\\\"string\\\\\\\\\"\n" +
				"              },\n" +
				"              {\n" +
				"                \\\\\\\\\"index\\\\\\\\\": 1,\n" +
				"                \\\\\\\\\"type\\\\\\\\\": \\\\\\\\\"string\\\\\\\\\"\n" +
				"              },\n" +
				"              {\n" +
				"                \\\\\\\\\"index\\\\\\\\\": 2,\n" +
				"                \\\\\\\\\"type\\\\\\\\\": \\\\\\\\\"string\\\\\\\\\"\n" +
				"              },\n" +
				"              {\n" +
				"                \\\\\\\\\"index\\\\\\\\\": 3,\n" +
				"                \\\\\\\\\"type\\\\\\\\\": \\\\\\\\\"string\\\\\\\\\"\n" +
				"              },\n" +
				"              {\n" +
				"                \\\\\\\\\"index\\\\\\\\\": 4,\n" +
				"                \\\\\\\\\"type\\\\\\\\\": \\\\\\\\\"string\\\\\\\\\"\n" +
				"              },\n" +
				"              {\n" +
				"                \\\\\\\\\"index\\\\\\\\\": 5,\n" +
				"                \\\\\\\\\"type\\\\\\\\\": \\\\\\\\\"string\\\\\\\\\"\n" +
				"              },\n" +
				"              {\n" +
				"                \\\\\\\\\"index\\\\\\\\\": 6,\n" +
				"                \\\\\\\\\"type\\\\\\\\\": \\\\\\\\\"string\\\\\\\\\"\n" +
				"              }\n" +
				"            ],\n" +
				"            \\\\\\\\\"fieldDelimiter\\\\\\\\\": \\\\\\\\\",\\\\\\\\\",\n" +
				"            \\\\\\\\\"encoding\\\\\\\\\": \\\\\\\\\"utf-8\\\\\\\\\",\n" +
				"            \\\\\\\\\"fileCode\\\\\\\\\": \\\\\\\\\"UTF-8\\\\\\\\\",\n" +
				"            \\\\\\\\\"fileType\\\\\\\\\": 2,\n" +
				"            \\\\\\\\\"rowNum\\\\\\\\\": 1,\n" +
				"            \\\\\\\\\"skipHeader\\\\\\\\\": true\n" +
				"          }\n" +
				"        },\n" +
				"        \\\\\\\\\"writer\\\\\\\\\": {\n" +
				"          \\\\\\\\\"name\\\\\\\\\": \\\\\\\\\"txtfilewriter\\\\\\\\\",\n" +
				"          \\\\\\\\\"parameter\\\\\\\\\": {\n" +
				"            \\\\\\\\\"path\\\\\\\\\": \\\\\\\\\"/data/tmp/datax\\\\\\\\\",\n" +
				"            \\\\\\\\\"fileName\\\\\\\\\": \\\\\\\\\"qieke_qsd\\\\\\\\\",\n" +
				"            \\\\\\\\\"writeMode\\\\\\\\\": \\\\\\\\\"truncate\\\\\\\\\",\n" +
				"            \\\\\\\\\"dateFormat\\\\\\\\\": \\\\\\\\\"yyyy-MM-dd HH:mm:ss\\\\\\\\\",\n" +
				"            \\\\\\\\\"fieldDelimiter\\\\\\\\\": \\\\\\\\\"\\u0001\\\\\\\\\",\n" +
				"            \\\\\\\\\"column\\\\\\\\\": [\n" +
				"              \\\\\\\\\"MANDT\\\\\\\\\",\n" +
				"              \\\\\\\\\"BUKRS\\\\\\\\\",\n" +
				"              \\\\\\\\\"BELNR\\\\\\\\\",\n" +
				"              \\\\\\\\\"GJAHR\\\\\\\\\",\n" +
				"              \\\\\\\\\"BUZEI\\\\\\\\\",\n" +
				"              \\\\\\\\\"BUZID\\\\\\\\\",\n" +
				"              \\\\\\\\\"AUGDT\\\\\\\\\"\n" +
				"            ],\n" +
				"            \\\\\\\\\"preSql\\\\\\\\\": [\n" +
				"              null,\n" +
				"              \\\\\\\\\"truncate table test.BSEG_1\\\\\\\\\"\n" +
				"            ],\n" +
				"            \\\\\\\\\"postSql\\\\\\\\\": [\n" +
				"              null\n" +
				"            ]\n" +
				"          }\n" +
				"        }\n" +
				"      }\n" +
				"    ],\n" +
				"    \\\\\\\\\"setting\\\\\\\\\": {\n" +
				"      \\\\\\\\\"speed\\\\\\\\\": {\n" +
				"        \\\\\\\\\"channel\\\\\\\\\": 1\n" +
				"      },\n" +
				"      \\\\\\\\\"errorLimit\\\\\\\\\": {\n" +
				"        \\\\\\\\\"percentage\\\\\\\\\": 0\n" +
				"      }\n" +
				"    }\n" +
				"  }\n" +
				"}\\\\\\\" > ./qetl.json \n" +
				"python ${DATAX_HOME}/bin/datax.py ./qetl.json && echo -e '\n" +
				"———————————————— 无写入前sql ————————————————' \n" +
				"echo -e '\n" +
				"———————————————— 开始清空目标表 ————————————————' \n" +
				"echo -e '\n" +
				"———————————————— 清空目标表完毕 ————————————————' \n" +
				" \n" +
				"echo -e '\n" +
				"———————————————— 开始执行doris抽数 ————————————————' \n" +
				"echo -e 'curl --location-trusted -u root:root -T /data/tmp/datax/qieke_qsd* -H \\\\\\\"columns:MANDT,BUKRS,BELNR,GJAHR,BUZEI,BUZID,AUGDT\\\\\\\" -H \\\\\\\"timeout:3600\\\\\\\" -H \\\\\\\"column_separator:\\x01\\\\\\\" -H \\\\\\\"max_filter_ratio:0\\\\\\\" http://192.168.5.61:8030/api/test/BSEG_1/_stream_load > /tmp/qieke_qsd && cat /tmp/qieke_qsd ' \n" +
				"curl --location-trusted -u root:root -T /data/tmp/datax/qieke_qsd* -H \\\\\\\"columns:MANDT,BUKRS,BELNR,GJAHR,BUZEI,BUZID,AUGDT\\\\\\\" -H \\\\\\\"timeout:3600\\\\\\\" -H \\\\\\\"column_separator:\\x01\\\\\\\" -H \\\\\\\"max_filter_ratio:0\\\\\\\" http://192.168.5.61:8030/api/test/BSEG_1/_stream_load > /tmp/qieke_qsd && cat /tmp/qieke_qsd  \n" +
				"echo -e '\n" +
				"———————————————— doris抽数执行完毕 ————————————————' \n" +
				" \n" +
				"echo -e '\n" +
				"———————————————— 开始删除临时文件 ————————————————' \n" +
				"echo -e 'rm -rf /data/tmp/datax/qieke_qsd*;' \n" +
				"rm -rf /data/tmp/datax/qieke_qsd*; \n" +
				"echo -e '\n" +
				"———————————————— 删除临时文件完毕 ————————————————' \n" +
				" \n" +
				"echo -e '\n" +
				"———————————————— 无写入后sql ————————————————' \n ";
		String localFilePath = "xxx";
		lastString = StringUtl.replaceString(lastString,"job.content.reader.parameter.host:" + localHost,"job.content.reader.parameter.port:"+localPort,"job.content.reader.parameter.username:"+localUserName,"job.content.reader.parameter.password:"+localPassword,"job.content.reader.parameter.path:"+localFilePath,"job.content.writer.parameter.fieldDelimiter:,");
		System.out.println(lastString);
	}
	
	@Test
	public void pathTest(){
		String path = "[1111222]";
		int i = path.indexOf("[");
		int i1 = path.indexOf("]");
		System.out.println(path.substring(i + 2,i1 -1));
	}
}
