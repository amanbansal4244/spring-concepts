package com.spring.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionListenerImpl implements JobExecutionListener {

	private Logger logger = LoggerFactory.getLogger(JobExecutionListenerImpl.class);

	/**
	 * Write logic which we want to execute before starting the Job
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("Job Started");
	}

	/**
	 * Write logic which we want to execute after completion the Job
	 */
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("Job Completed");
		}
	}
}
