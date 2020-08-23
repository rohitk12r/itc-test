package com.itc.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.itc.async.AsyncThreadPool;
import com.itc.exception.XLSServiceException;
import com.itc.model.Section;

import lombok.extern.slf4j.Slf4j;

/**
 * This is helper class for processing xls sheet
 * 
 * @author RohitSharma
 */
@Slf4j
@Component
public class XLSHelper {
	private static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static String[] HEADERs = { "section" };
	private static String SHEET = "section";

	/**
	 * This is reference for calling asynchronous call.
	 */
	@Autowired
	private AsyncThreadPool asyncThreadPool;

	/**
	 * It is for validation of XLS extension
	 * 
	 * @param file the XLS file
	 * @return {@link Boolean}
	 */
	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	/**
	 * It is parsing xls sheet.
	 * 
	 * @param inputStream input for xls sheet
	 * @return {@link List}
	 * @throws XLSServiceException
	 */
	public List<Section> parseJob(InputStream inputStream) throws XLSServiceException {
		List<Section> sectionCollection = new CopyOnWriteArrayList<Section>();
		try {
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();
			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cellsInRow = currentRow.iterator();
				try {
					sectionCollection = asyncThreadPool.asyncCall(workbook, cellsInRow, sectionCollection);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			log.error("Getting exception for parsing xls file.");
			throw new XLSServiceException("Failed to parse Excel file: " + e.getMessage());
		}
		return sectionCollection;
	}

	/**
	 * It is for export xls sheet.
	 * 
	 * @param sections this is json object
	 * @return {@link ByteArrayInputStream}
	 * @throws XLSServiceException
	 */
	public static ByteArrayInputStream exportJob(List<Section> sections) throws XLSServiceException {

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet(SHEET);
			// Header
			Row headerRow = sheet.createRow(0);
			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (Section section : sections) {
				Row row = sheet.createRow(rowIdx++);
				Gson gson = new Gson();
				String data = gson.toJson(section);
				row.createCell(0).setCellValue(data);
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.error("Getting exception for export xls file.");
			throw new XLSServiceException("Failed to export data to Excel file: " + e.getMessage());
		}
	}
}
