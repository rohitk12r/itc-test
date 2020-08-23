package com.itc.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itc.dao.GeologicalClassEntityRepository;
import com.itc.dao.JobRepository;
import com.itc.entity.GeologicalClassEntity;
import com.itc.entity.JobEntity;
import com.itc.entity.SectionEntity;
import com.itc.exception.XLSServiceException;
import com.itc.helper.XLSHelper;
import com.itc.model.GeologicalClass;
import com.itc.model.Section;
import com.itc.tf.GeologicalClassEntityToGeologicalClassTf;
import com.itc.tf.SectionEntityToSectionTf;
import com.itc.tf.SectionToSectionEntityTf;

import lombok.extern.slf4j.Slf4j;

/**
 * This is implementation of business logic for geological sections.
 * 
 * @author RohitSharma
 */
@Slf4j
@Service
public class XLSServiceImpl implements XLSService {

	/**
	 * This is the reference of {@link JobRepository}
	 */
	@Autowired
	private JobRepository jobRepository;

	/**
	 * This is the reference of {@link GeologicalClassEntityRepository}
	 */
	@Autowired
	private GeologicalClassEntityRepository geologicalClassEntityRepository;

	/**
	 * This is the reference of {@link GeologicalClassEntityToGeologicalClassTf}
	 */
	@Autowired
	private GeologicalClassEntityToGeologicalClassTf geologicalClassEntityToGeologicalClass;

	/**
	 * This is the reference of {@link SectionToSectionEntityTf}
	 */
	@Autowired
	private SectionToSectionEntityTf sectionToSectionEntityTf;

	/**
	 * This is the reference of {@link SectionEntityToSectionTf}
	 */
	@Autowired
	private SectionEntityToSectionTf sectionEntityToSectionTf;
	/**
	 * This is the reference of {@link XLSHelper}
	 */
	@Autowired
	private XLSHelper xlsHelper;

	/**
	 * This method for parsing xls file in json format.
	 * 
	 * @param file the file import from api in xls format.
	 * @return {@link Long}
	 * @throws XLSServiceException
	 */
	@Override
	public Long registerJob(MultipartFile file) throws XLSServiceException {
		try {
			Long jobId = 0L;
			log.debug("Call registerJob method.");
			List<Section> sections = xlsHelper.parseJob(file.getInputStream());
			if (CollectionUtils.isNotEmpty(sections)) {
				jobId = register(sections);
			} else
				throw new XLSServiceException("There is no data in xls file");
			log.info("Successfully register job with ID : " + jobId);
			return jobId;
		} catch (Exception e) {
			log.error("Failed to register Job in Database." + e.getMessage());
			throw new XLSServiceException(e.getLocalizedMessage(), e.getMessage(), e);
		}
	}

	private Long register(List<Section> sections) {
		log.debug("Call register method");
		JobEntity jobEntity = new JobEntity();
		sectionToSectionEntityTf.setJobEntity(jobEntity);
		List<SectionEntity> sectionEntityEntities = CollectionUtils.collect(sections, this.sectionToSectionEntityTf,
				new ArrayList<SectionEntity>());
		jobEntity.setSectionEntity(sectionEntityEntities);
		JobEntity updatedJobEntity = jobRepository.save(jobEntity);
		return updatedJobEntity.getId();
	}

	/**
	 * This method for export file in xls.
	 * 
	 * @return {@link ByteArrayInputStream}
	 * @throws XLSServiceException
	 */
	@Override
	public ResponseEntity<Resource> exportXls(Long jobId) throws XLSServiceException {
		try {
			log.debug("Call exportXls method");
			Optional<JobEntity> optionalJobEntity = jobRepository.findById(jobId);
			JobEntity jobEntity = optionalJobEntity.get();
			if (Objects.nonNull(jobEntity) && CollectionUtils.isNotEmpty(jobEntity.getSectionEntity())) {
				List<Section> sections = CollectionUtils.collect(jobEntity.getSectionEntity(),
						this.sectionEntityToSectionTf, new ArrayList<Section>());
				ByteArrayInputStream byteArrayInputStream = XLSHelper.exportJob(sections);
				InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);
				ResponseEntity<Resource> responseEntity = ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=" + FILENAME + jobId + FILENAME_EXTENSION)
						.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(inputStreamResource);
				log.info("Successfully export XLS file with name is : " + FILENAME + jobId + FILENAME_EXTENSION);
				return responseEntity;
			} else
				throw new XLSServiceException("There is no Job available in Database with this Job ID : " + jobId);

		} catch (Exception e) {
			log.error("Failed download the XLS file" + e.getMessage());
			throw new XLSServiceException(e.getLocalizedMessage(), e.getMessage(), e);
		}
	}

	/**
	 * This method for getting list of {@link Section} by using job ID.
	 * 
	 * @param jobId it for getting Job from Database.
	 * @return {@link List}
	 * @throws XLSServiceException
	 */
	@Override
	public List<Section> getJobById(Long jobId) throws XLSServiceException {
		try {
			log.debug("Call getJobById method");
			Optional<JobEntity> optionalJobEntity = jobRepository.findById(jobId);
			JobEntity jobEntity = optionalJobEntity.get();
			if (Objects.nonNull(jobEntity) && CollectionUtils.isNotEmpty(jobEntity.getSectionEntity())) {
				List<Section> sections = CollectionUtils.collect(jobEntity.getSectionEntity(),
						this.sectionEntityToSectionTf, new ArrayList<Section>());
				log.info("Successfully get the Job with ID : " + jobId);
				return sections;
			} else
				throw new XLSServiceException("There is no Job available in Database with this Job ID : " + jobId);
		} catch (Exception e) {
			log.error("Failed to load Job from database" + e.getMessage());
			throw new XLSServiceException(e.getLocalizedMessage(), e.getMessage(), e);
		}

	}

	/**
	 * This method for search {@link GeologicalClass} using these parameter
	 * 
	 * @param name this is for search parameter
	 * @param code this is for search parameter.
	 * @return {@link GeologicalClass}
	 * @throws XLSServiceException
	 */
	@Override
	public List<GeologicalClass> searchByNameAndCode(String name, String code) throws XLSServiceException {
		try {
			log.debug("Call searchByNameAndCode method");
			List<GeologicalClassEntity> geologicalClassEntities = geologicalClassEntityRepository
					.searchByNameOrCode(name, code);
			if (CollectionUtils.isNotEmpty(geologicalClassEntities)) {
				List<GeologicalClass> geologicalClasses = CollectionUtils.collect(geologicalClassEntities,
						geologicalClassEntityToGeologicalClass, new ArrayList<GeologicalClass>());
				log.info("Successfully search  GeologicalClass with name  : " + name + " and code :  " + code);
				return geologicalClasses;
			} else
				throw new XLSServiceException("There is no GeologicalClass available in Database with this Name : "
						+ name + " and Code :  " + code);
		} catch (Exception e) {
			log.error("Failed to search GeologicalClass by name and code" + e.getMessage());
			throw new XLSServiceException(e.getLocalizedMessage(), e.getMessage(), e);
		}

	}

	/**
	 * This Api for searching GeologicalClass for using name or code.
	 * 
	 * @param jobId   This ID used to fetch Job to add section
	 * @param section this is for adding section in existing Job
	 * @return {@link List}
	 * @throws XLSServiceException
	 */
	@Override
	public List<Section> addSectionInJob(Long jobId, Section section) throws XLSServiceException {
		try {
			log.debug("Call addSectionInJob method");
			Optional<JobEntity> optionalJobEntity = jobRepository.findById(jobId);
			JobEntity jobEntity = optionalJobEntity.get();
			if (Objects.nonNull(jobEntity) && CollectionUtils.isNotEmpty(jobEntity.getSectionEntity())) {
				List<SectionEntity> sections = jobEntity.getSectionEntity();
				SectionEntity sectionEntity = this.sectionToSectionEntityTf.transform(section);
				sectionEntity.setJobEntity(jobEntity);
				sections.add(sectionEntity);
				JobEntity updatedJobEntity = jobRepository.save(jobEntity);
				List<Section> sectionCollection = CollectionUtils.collect(updatedJobEntity.getSectionEntity(),
						this.sectionEntityToSectionTf, new ArrayList<Section>());
				log.info("Successfully add section in Job ");
				return sectionCollection;
			} else
				throw new XLSServiceException("There is no Job available in Database with this Job ID : " + jobId);

		} catch (Exception e) {
			log.error("Failed for adding section in Job " + e.getMessage());
			throw new XLSServiceException(e.getLocalizedMessage(), e.getMessage(), e);
		}
	}
}
