package com.itc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * This is hold's GeologicalClass entity
 * 
 * @author RohitSharma
 *
 */
@Data
@Entity
@Table(name = "GeologicalClassEntity")
public class GeologicalClassEntity implements Serializable {

	/**
	 * The serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is primary key of GeologicalClass, which is auto generation.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * The name of GeologicalClass
	 */
	@Column
	private String name;
	/**
	 * the code of GeologicalClass
	 */
	@Column
	private String code;

	/**
	 * It's hold relation of {@link SectionEntity} to {@link GeologicalClassEntity}}
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "section_id", nullable = false)
	private SectionEntity sectionEntity;
}
