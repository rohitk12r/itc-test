package com.itc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.itc.entity.GeologicalClassEntity;

/**
 * This is the persistence logic for getting {@link GeologicalClassEntity} from
 * Database.
 * 
 * @author RohitSharma
 *
 */
public interface GeologicalClassEntityRepository extends JpaRepository<GeologicalClassEntity, Long> {

	/**
	 * This is used to search GeologicalClass by name and code
	 * 
	 * @param name the name of GeologicalClass
	 * @param code the Code of GeologicalClass
	 * @return {@link GeologicalClassEntity}
	 */
	List<GeologicalClassEntity> searchByNameOrCode(@Param("name") String name, @Param("code") String code);

}
