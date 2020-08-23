package com.itc.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "JobEntity")
public class JobEntity implements Serializable {
	/**
	 * The serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This is primary key of Job for register job in DB.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * It's hold relation between {@link JobEntity} to {@link SectionEntity}}
	 */
	@OneToMany(mappedBy = "jobEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SectionEntity> sectionEntity = new ArrayList<>();

}
