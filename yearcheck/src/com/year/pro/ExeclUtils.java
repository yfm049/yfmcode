package com.year.pro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ExeclUtils {

	private Map<String, List<Info>> msl;

	public ExeclUtils() {
		msl = new HashMap<String, List<Info>>();
	}

	public Map<String, List<Info>> ReadFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			int sheets = workbook.getNumberOfSheets();
			for (int i = 0; i < sheets; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				String name = sheet.getSheetName();
				List<Info> li = new ArrayList<Info>();
				int row = sheet.getPhysicalNumberOfRows();
				for (int r = 1; r < row; r++) {
					HSSFRow xr = sheet.getRow(r);
					int cell = xr.getPhysicalNumberOfCells();
					Info info = new Info();
					if (cell >= 3) {
						HSSFCell hccode = xr.getCell(0);
						if (hccode.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							info.setCode(new DecimalFormat("#").format(hccode
									.getNumericCellValue()));
						} else {
							info.setCode(hccode.getStringCellValue());
						}
						HSSFCell hcgongsi = xr.getCell(1);
						info.setGongsi(hcgongsi.getStringCellValue());
						HSSFCell hcfaren = xr.getCell(2);
						info.setFaren(hcfaren.getStringCellValue());
						info.setSfz("440901197709194316");
						info.setPhone("15068857765");
						li.add(info);
					} else {
					}
					msl.put(name, li);

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msl;

	}

	public static void main(String[] args) {
		ExeclUtils eu = new ExeclUtils();
		eu.ReadFile(new File("H://data2.xls"));
	}

}
