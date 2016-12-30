package com.qaservices.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelReader {

	private String workBookFilePath;
	private HSSFWorkbook workBook;
	private HSSFSheet sheet;
	private int rowCount;
	private int columnCount;

	public ExcelReader(String filePath) {
		workBookFilePath = filePath;
		if (!workBookFilePath.endsWith(".xls")) {
			throw new RuntimeException("File's extension must be '.xls'");
		}
		File file = new File(workBookFilePath);
		if (!file.exists() || file.isDirectory()) {
			throw new RuntimeException("File " + workBookFilePath + " does not exist (or) it is a directory");
		}
		try {
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(file);
			workBook = new HSSFWorkbook(poifsFileSystem);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ExcelReader(String filePath, String sheetName) {
		this(filePath);
		loadSheet(sheetName);
	}

	public void loadSheet(String sheetName) {
		if (workBook == null) {
			throw new RuntimeException("WorkBook is not initialized yet");
		}
		if (workBook.getNumberOfSheets() == 0) {
			throw new RuntimeException("WorkBook '" + workBookFilePath + "' does not contain any sheets");
		}
		sheet = workBook.getSheet(sheetName);
		if (sheet == null) {
			throw new RuntimeException("WorkBook '" + workBookFilePath + "' does not contain a sheet with name '" + sheetName + "'");
		}
		rowCount = 0;
		columnCount = 0;
		int visibleRows = sheet.getPhysicalNumberOfRows();
		int visibleColumns = 0;
		for (int i = 0; i < visibleRows || i < 10; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				visibleColumns = row.getPhysicalNumberOfCells();
				if (visibleColumns > 0) {
					rowCount++;
					if (columnCount < visibleColumns) {
						columnCount = visibleColumns;
					}
				}
			}
		}
	}

	private String convertCellToString(HSSFCell cell) {
		return cell != null ? cell.toString().trim() : "";
	}

	public HashMap<String, String> getRowData(String firstColumnValue) {
		HashMap<String, String> rowData = new HashMap<String, String>();
		HSSFRow headerRow = sheet.getRow(0);
		HSSFRow row;
		boolean found = false;
		for (int i = 1; i < rowCount; i++) {
			row = sheet.getRow(i);
			if (row != null && convertCellToString(row.getCell(0)).equals(firstColumnValue)) {
				if (found) {
					throw new RuntimeException("There are more than one rows with first column value as '" + firstColumnValue + "'");
				}
				found = true;
				for (int j = 0; j < columnCount; j++) {
					rowData.put(convertCellToString(headerRow.getCell(j)), convertCellToString(row.getCell(j)));
				}
			}
		}
		return rowData;
	}

	public List<HashMap<String, String>> getEntireSheetData() {
		List<HashMap<String, String>> sheetData = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rowData;
		HSSFRow headerRow = sheet.getRow(0);
		HSSFRow row;
		for (int i = 1; i < rowCount; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			rowData = new HashMap<String, String>();
			for (int j = 0; j < columnCount; j++) {
				rowData.put(convertCellToString(headerRow.getCell(j)), convertCellToString(row.getCell(j)));
			}
			sheetData.add(rowData);
		}
		return sheetData;
	}

}
