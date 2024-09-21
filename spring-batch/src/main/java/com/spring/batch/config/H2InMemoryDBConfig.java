package com.spring.batch.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.annotation.JsonProperty;

@EnableAutoConfiguration
@Configuration
@EnableTransactionManagement
@ComponentScan("com.com.spring.batch.*")
@EnableJpaRepositories(basePackages = { "com.spring.batch.repository" })
@EntityScan("com.spring.batch.*")
class H2InMemoryDBConfig {

	@JsonProperty("inMemory")
	private boolean inMemory;

	@JsonProperty
	private DataSourceFactory database;

	public boolean isInMemory() {
		return inMemory;
	}

	public DataSourceFactory getDatabase() {
		return database;
	}
}
