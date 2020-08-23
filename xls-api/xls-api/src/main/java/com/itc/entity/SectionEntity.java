package com.itc.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "SectionEntity")
public class SectionEntity implements Serializable {

	/**
	 * The serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is primary key of Section, which is auto generation.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String sectionName;

	/**
	 * It's hold relation of {@link SectionEntity} to {@link GeologicalClassEntity}}
	 */
	@OneToMany(mappedBy = "sectionEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<GeologicalClassEntity> geologicalClasses = new ArrayList<>();

	/**
	 * It's hold relation of {@link SectionEntity} to {@link JobEntity}}
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "job_id", nullable = false)
	private JobEntity jobEntity;

}
