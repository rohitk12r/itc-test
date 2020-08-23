package com.itc.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itc.entity.JobEntity;

/**
 * This is the persistence logic for getting {@link JobEntity} from Database.
 * 
 * @author RohitSharma
 */
public interface JobRepository extends JpaRepository<JobEntity, Long> {

}
