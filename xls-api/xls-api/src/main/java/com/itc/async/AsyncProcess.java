package com.itc.async;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.gson.Gson;
import com.itc.model.Section;

/**
 * It is hold's Thread.
 * 
 * @author RohitSharma
 *
 */
public class AsyncProcess implements Callable<List<Section>> {

	/**
	 * This is for global object to convert JSON to Object.
	 */
	private static final Gson GSON = new Gson();
	/**
	 * This is for the reference of Workbook
	 */
	private Workbook workbook;
	/**
	 * This is for the reference of cellsInRow
	 */
	private Iterator<Cell> cellsInRow;
	/**
	 * This is for the reference of sectionCollection
	 */
	private List<Section> sectionCollection;

	/**
	 * This is constructor for initialization of the Object
	 */
	public AsyncProcess(Workbook workbook, Iterator<Cell> cellsInRow, List<Section> sectionCollection) {
		this.cellsInRow = cellsInRow;
		this.workbook = workbook;
		this.sectionCollection = sectionCollection;
	}

	/**
	 * This call function for calling automatically by Threads.
	 */
	@Override
	public List<Section> call() throws Exception {
		return parser();
	}

	private List<Section> parser() throws IOException {
		while (cellsInRow.hasNext()) {
			Cell currentCell = cellsInRow.next();
			String json = currentCell.getStringCellValue();
			sectionCollection.add(convertJsonToObject(json));
		}
		workbook.close();
		return sectionCollection;
	}

	private Section convertJsonToObject(String json) {
		Section section = GSON.fromJson(json, Section.class);
		return section;
	}
}
