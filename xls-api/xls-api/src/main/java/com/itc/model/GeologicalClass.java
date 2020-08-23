package com.itc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * This is hold GeologicalClass object
 * 
 * @author RohitSharma
 *
 */
@Data
public class GeologicalClass {

	/**
	 * The name of GeologicalClass
	 */
	private String name;

	/**
	 * The code of GeologicalClass
	 */
	@SerializedName("Code")
	@JsonProperty("Code")
	private String code;
}
