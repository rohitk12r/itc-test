package com.itc.tf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itc.entity.GeologicalClassEntity;
import com.itc.entity.JobEntity;
import com.itc.entity.SectionEntity;
import com.itc.model.Section;

/**
 * This is transformer class for converting {@link Section} to
 * {@link SectionEntity}
 * 
 * @author RohitSharma
 */
@Component
public class SectionToSectionEntityTf implements Transformer<Section, SectionEntity> {

	/**
	 * The jobEntity
	 */
	private JobEntity jobEntity;

	/**
	 * This is the reference of {@link GeologicalClassToGeologicalClassEntityTf}
	 */
	@Autowired
	private GeologicalClassToGeologicalClassEntityTf geologicalClassToGeologicalClassEntityTf;

	/**
	 * This is transform method.
	 */
	@Override
	public SectionEntity transform(Section input) {
		SectionEntity sectionEntity = null;
		if (Objects.nonNull(input)) {
			sectionEntity = new SectionEntity();
			sectionEntity.setSectionName(input.getSectionName());
			geologicalClassToGeologicalClassEntityTf.setSectionEntity(sectionEntity);
			List<GeologicalClassEntity> geologicalClassEntities = CollectionUtils.collect(input.getGeologicalClasses(),
					this.geologicalClassToGeologicalClassEntityTf, new ArrayList<GeologicalClassEntity>());
			sectionEntity.setJobEntity(jobEntity);
			sectionEntity.setGeologicalClasses(geologicalClassEntities);

		}
		return sectionEntity;
	}

	public void setJobEntity(JobEntity jobEntity) {
		this.jobEntity = jobEntity;
	}

}
