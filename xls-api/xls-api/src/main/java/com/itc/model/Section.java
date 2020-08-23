package com.itc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * This class is hold Section in Json
 * 
 * @author RohitSharma
 *
 */
@Data
public class Section {

	/**
	 * The name of Section
	 */
	@SerializedName("name")
	@JsonProperty("name")
	private String sectionName;

	/**
	 * It is hold reference of GeologicalClass
	 */
	private List<GeologicalClass> geologicalClasses;

}
