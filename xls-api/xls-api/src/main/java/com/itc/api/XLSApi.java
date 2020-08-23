package com.itc.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itc.exception.XLSServiceException;
import com.itc.helper.XLSHelper;
import com.itc.model.GeologicalClass;
import com.itc.model.Section;
import com.itc.service.XLSService;
import com.itc.service.XLSServiceImpl;
import com.itc.utils.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * This class exposed CRUD rest api to get sections.
 * 
 * @author RohitSharma
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/xls")
public class XLSApi {

	/**
	 * This is the reference of {@link XLSServiceImpl}
	 */
	@Autowired
	private XLSService xlsService;

	/**
	 * The ResponseUtils
	 */
	@Autowired
	private ResponseUtils responseUtils;

	/**
	 * This is expose api for parsing xls and store in database and return the
	 * JobID.
	 * 
	 * @param file the XLS file.
	 * @return {@linkplain JobId}
	 */
	@PostMapping("/registerJob")
	public ResponseEntity<?> registerJob(@RequestParam("file") MultipartFile file) {
		if (XLSHelper.hasExcelFormat(file)) {
			try {
				Long jobId = xlsService.registerJob(file);
				return responseUtils.buildOk(jobId);
			} catch (XLSServiceException e) {
				log.error("Getting exception for register Job. " + e.getMessage());
				return responseUtils.build(e);
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	/**
	 * This Api is exposed to get List of section by using to Job ID.
	 * 
	 * @param jobId This ID used to fetch Job from DB
	 * @return {@link List}
	 */
	@GetMapping("/sections/{jobId}")
	public ResponseEntity<?> getJobByID(@PathVariable("jobId") Long jobId) {
		try {
			List<Section> sections = xlsService.getJobById(jobId);
			if (sections.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			return responseUtils.buildOk(sections);
		} catch (XLSServiceException e) {
			log.error("Getting exception for register Job. " + e.getMessage());
			return responseUtils.build(e);
		}
	}

	/**
	 * This Api is used to download data in xls format.
	 * 
	 * @return {@link Resource}
	 */
	@GetMapping("/exportJob/{jobId}")
	public ResponseEntity<?> exportJob(@PathVariable("jobId") Long jobId) {
		try {
			return xlsService.exportXls(jobId);
		} catch (XLSServiceException e) {
			log.error("Getting exception for export job. " + e.getMessage());
			return responseUtils.build(e);
		}
	}

	/**
	 * This Api for searching GeologicalClass for using name or code.
	 * 
	 * @param name the name of GeologicalClass
	 * @param code then code of GeologicalClass
	 * @return {@link List}
	 */
	@GetMapping("/searchByNameAndCode")
	public ResponseEntity<?> searchByNameAndCode(@RequestParam("name") String name, @RequestParam("code") String code) {
		try {
			List<GeologicalClass> geologicalClasses = xlsService.searchByNameAndCode(name, code);
			if (geologicalClasses.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			return responseUtils.buildOk(geologicalClasses);
		} catch (XLSServiceException e) {
			log.error("Getting exception for search GeologicalClass by name or code.  " + e.getMessage());
			return responseUtils.build(e);
		}
	}

	/**
	 * This Api for adding section in Job
	 * 
	 * @param jobId   This ID used to fetch Job to add section
	 * @param section this is for adding section in existing Job
	 * @return {@link List}
	 */
	@PostMapping("/addSectionInJob/{jobId}")
	public ResponseEntity<?> addSectionInJob(@PathVariable("jobId") Long jobId, @RequestBody Section section) {
		try {
			List<Section> sections = xlsService.addSectionInJob(jobId, section);
			return new ResponseEntity<>(sections, HttpStatus.OK);
		} catch (XLSServiceException e) {
			log.error("Getting exception for adding section in Job. " + e.getMessage());
			return responseUtils.build(e);
		}
	}
}