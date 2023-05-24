package com.naruku.pdfTest.dao;

import com.itextpdf.awt.geom.Rectangle2D;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.util.ObjectUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * pdf数据库交互类
 *
 * @author herche
 * @date 2022/11/05
 */
public class PdfInfoDao {
	

	
	public boolean savePdfInfo(String moduleId, int i, List<Map<String, Rectangle2D.Float>> rows, Connection connection) {
		if (ObjectUtils.isEmpty(rows)) {
			return false;
		}
		QueryRunner runner = new QueryRunner();
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		for (Map<String, Rectangle2D.Float> row : rows) {
			row.forEach((k, v) -> {
				Object[] obj = {UUID.randomUUID().toString().replace("-", "").substring(0, 32), moduleId, i, k.trim(), v.x, v.y, v.width, v.height, date, date};
				try {
					runner.update(connection, "insert into t_naruku_pdf_info (id,module_id,page,info_value,info_x,info_y,width,heigth,create_time,update_time) values(?,?,?,?,?,?,?,?,?,?)", obj);
				} catch (SQLException throwables) {
					throwables.printStackTrace();
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		}
		return true;
	}
}
