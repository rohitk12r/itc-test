package com.itc.tf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itc.entity.SectionEntity;
import com.itc.model.GeologicalClass;
import com.itc.model.Section;

/**
 * This is transformer class for converting {@link SectionEntity} to {@link Section}
 * 
 * @author RohitSharma
 *
 */
@Component
public class SectionEntityToSectionTf implements Transformer<SectionEntity, Section> {

	/**
	 * This is the reference of {@link GeologicalClassEntityToGeologicalClassTf}
	 */
	@Autowired
	private GeologicalClassEntityToGeologicalClassTf geologicalClassEntityToGeologicalClass;

	/**
	 * This is transform method.
	 */
	@Override
	public Section transform(SectionEntity input) {
		Section section = null;
		if (Objects.nonNull(input)) {
			section = new Section();
			section.setSectionName(input.getSectionName());
			List<GeologicalClass> geologicalClasses = CollectionUtils.collect(input.getGeologicalClasses(),
					this.geologicalClassEntityToGeologicalClass, new ArrayList<GeologicalClass>());
			section.setGeologicalClasses(geologicalClasses);
		}
		return section;
	}

}
