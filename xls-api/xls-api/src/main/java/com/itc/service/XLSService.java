package com.itc.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.itc.exception.XLSServiceException;
import com.itc.model.GeologicalClass;
import com.itc.model.Section;

/**
 * This is interface which has declare method signature of business logic for
 * geological sections.
 * 
 * @author RohitSharma
 */
public interface XLSService {

	/**
	 * The prefix name of xls sheet.
	 */
	String FILENAME = "job";
	/**
	 * The extension of file.
	 */
	String FILENAME_EXTENSION = ".xlsx";

	/**
	 * This method for parsing xls file in json format.
	 * 
	 * @param file the file import from api in xls format.
	 * @return {@link Long}
	 * @throws XLSServiceException
	 */
	Long registerJob(MultipartFile file) throws XLSServiceException;

	/**
	 * This method for export file in xls.
	 * 
	 * @return {@link Resource}
	 */
	ResponseEntity<Resource> exportXls(Long jobId) throws XLSServiceException;

	/**
	 * This method for getting list of {@link Section} by using job ID.
	 * 
	 * @param jobId it for getting Job from Database.
	 * @return {@link List}
	 */
	List<Section> getJobById(Long jobId) throws XLSServiceException;

	/**
	 * This method for search {@link GeologicalClass} using these parameter
	 * 
	 * @param name this is for search parameter
	 * @param code this is for search parameter.
	 * @return {@link GeologicalClass}
	 */
	List<GeologicalClass> searchByNameAndCode(String name, String code) throws XLSServiceException;

	/**
	 * This Api for searching GeologicalClass for using name or code.
	 * 
	 * @param jobId   This ID used to fetch Job to add section
	 * @param section this is for adding section in existing Job
	 * @return {@link List}
	 */
	List<Section> addSectionInJob(Long jobId, Section section) throws XLSServiceException;
}
