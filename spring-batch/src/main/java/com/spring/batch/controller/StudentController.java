package com.spring.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

	private final JobLauncher jobLauncher;
	private final Job job;

	@PostMapping
	public void importCsvToDBJob() {
		log.info("Job contoller started:");
		/**
		 * we have to create JobParameters
		 */
		JobParameters jobParameters = new JobParametersBuilder()
				/**
				 * we tell when this job start.
				 * here, I want my job to be executed immediately.
				 */
				.addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			/**
			 * I want to run job with my job-parameters.
			 */
			jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
}
