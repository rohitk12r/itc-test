package com.itc.tf;

import java.util.Objects;

import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

import com.itc.entity.GeologicalClassEntity;
import com.itc.model.GeologicalClass;

/**
 * This is transformer class for converting {@link GeologicalClassEntity} to
 * {@link GeologicalClass}
 * 
 * @author RohitSharma
 */
@Component
public class GeologicalClassEntityToGeologicalClassTf implements Transformer<GeologicalClassEntity, GeologicalClass> {

	/**
	 * This is transform method.
	 */
	@Override
	public GeologicalClass transform(GeologicalClassEntity input) {
		GeologicalClass geologicalClass = null;
		if (Objects.nonNull(input)) {
			geologicalClass = new GeologicalClass();
			geologicalClass.setCode(input.getCode());
			geologicalClass.setName(input.getName());
		}
		return geologicalClass;
	}
}
