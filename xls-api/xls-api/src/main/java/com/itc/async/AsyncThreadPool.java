package com.itc.async;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.itc.model.Section;

/**
 * This class is used for create Thread pool.
 * 
 * @author RohitSharma
 *
 */
@Component
public class AsyncThreadPool {

	/**
	 * This is core size of Thread pool
	 */
	@Value("${thread.corePoolSize}")
	private int corePoolSize;

	/**
	 * this is for maximum size of Thread pool
	 */
	@Value("${thread.maximumPoolSize}")
	private int maximumPoolSize;

	/**
	 * This is for alive Thread time
	 */
	@Value("${thread.keepAliveTime}")
	private long keepAliveTime;

	/**
	 * This is count of blockngQueue
	 */
	@Value("${thread.blockingQueueCount}")
	private int blockingQueueCount;

	/**
	 * It is used to create Thread Pool to call asynchronous for parsing xls data.
	 * 
	 * @param workbook          it is the workbook of xls
	 * @param cellsInRow        it for reading cells
	 * @param sectionCollection for adding all section
	 * @return {@link List}
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<Section> asyncCall(Workbook workbook, Iterator<Cell> cellsInRow, List<Section> sectionCollection)
			throws InterruptedException, ExecutionException {
		BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(blockingQueueCount);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
				TimeUnit.SECONDS, blockingQueue);
		Future<List<Section>> future = executor.submit(new AsyncProcess(workbook, cellsInRow, sectionCollection));
		return future.get();
	}
}
