package com.itc.tf;

import java.util.Objects;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

import com.itc.entity.GeologicalClassEntity;
import com.itc.entity.SectionEntity;
import com.itc.model.GeologicalClass;

/**
 * This is transformer class for converting {@link GeologicalClass} to
 * {@link GeologicalClassEntity}
 * 
 * @author RohitSharma
 */
@Component
public class GeologicalClassToGeologicalClassEntityTf implements Transformer<GeologicalClass, GeologicalClassEntity> {

	/**
	 * The Section Entity
	 */
	private SectionEntity sectionEntity;

	/**
	 * This is transform method.
	 */
	@Override
	public GeologicalClassEntity transform(GeologicalClass input) {
		GeologicalClassEntity geologicalClassEntity = null;
		if (Objects.nonNull(input)) {
			geologicalClassEntity = new GeologicalClassEntity();
			geologicalClassEntity.setCode(input.getCode());
			geologicalClassEntity.setName(input.getName());
			geologicalClassEntity.setSectionEntity(this.sectionEntity);
		}
		return geologicalClassEntity;
	}

	public void setSectionEntity(SectionEntity sectionEntity) {
		this.sectionEntity = sectionEntity;
	}
}
