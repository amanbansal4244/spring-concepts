package com.spring.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.spring.batch.entity.Product;

//@Configuration
public class AnotherBatchConfiguration {

	/**
	 * Creating Job Bean which is the first step for spring-batch
	 */
	@Bean
	Job jobBean(JobRepository jobRepository, JobExecutionListenerImpl jobExecutionListener, Step steps) {
		 
				/**
				 * Job takes the JobRepository. so we have to configure it
				 * JobRepository: This repository responsible for persistence of batch meta-data entities.
				 */
		return	new JobBuilder("job", jobRepository)
				/**
				 * we want to run some code before or after executing the job. So we are configuring it.
				 * 
				 * This is custom class.
				 */
				.listener(jobExecutionListener)
				/**
				 * Job is made of steps. So, we need to configure steps.
				 */
				.start(steps)
				.build();
	}

	@Bean
	Step steps(JobRepository jobRepository, DataSourceTransactionManager transactionManager, ItemReader<Product> itemReader,
			ItemProcessor<Product, Product> itemProcessor, ItemWriter<Product> itemWriter) {
		return new StepBuilder("jobStep", jobRepository)
				/**
				 * chunk means: batch size
				 * transactionManager: to manage the transactions
				 * 
				 * <Product, Product> : this is input and output types
				 */
				.<Product, Product>chunk(5, transactionManager)
				/**
				 * Steps has three steps:
				 * 	ItemReader
				 *  ItemProcessor
				 *  ItemWriter
				 */
				.reader(itemReader) // itemReader: this is created below in this class
				.processor(itemProcessor) // itemProcessor: this is created below in this class
				.writer(itemWriter) // itemWriter: this is created below in this class
				.build();

	}

	/**
	 * Creating ItemReader step of spring batch
	 */
	@Bean
	FlatFileItemReader<Product> itemReader() {
				
				/**
				 * Using FlatFileItemReaderBuilder to read the CSV file.
				 */
		return new FlatFileItemReaderBuilder<Product>()
				/**
				 * Giving any name to reader
				 */
				.name("itemReader")
				/**
				 * Path from where we are reading the CSV file
				 */
				.resource(new ClassPathResource("data.csv"))
				/**
				 * Returns an instance of a {@link DelimitedBuilder} for building a
				 * {@link DelimitedLineTokenizer}.
				 */
				.delimited()
				/**
				 * In DelimitedBuilder: we can set the names as list
				 * 
				 * We are passing all fields of our CSV file.
				 */
				.names("productId", "title", "description", "price", "discount")
				/**
				 * we are targeting to our Entity class : Product
				 */
				.targetType(Product.class)
				.build();
	}

	/**
	 * Creating processor step of spring batch
	 */
	@Bean
	ItemProcessor<Product, Product> itemProcessor() {
		return new CustomItemProcessor();
	}

	/**
	 * Creating writer step of spring batch
	 */
	@Bean
	ItemWriter<Product> itemWriter(DataSource dataSource) {

		/**
		 * we are passing value as parameterized. These parameterized fields are matching exactly Product class field names.
		 */
		String sqlQuery = "insert into products(product_id,title,description,price,discount,discounted_price)values(:productId, :title, :description, :price, :discount, :discountedPrice)";
				/**
				 * Using JdbcBatchItemWriterBuilder to write data into database.
				 */
		return new JdbcBatchItemWriterBuilder<Product>()
				/**
				 * what query we want to use while inserting the data. Pass it here.
				 */
				.sql(sqlQuery)
				/**
				 * whatever datasource we have, pass it here.
				 * In this project: datasource is automatically created once we defined the properties in application.properties file
				 * but we can also create the datasource as well.
				 * 
				 * dataSource helps to create connection because this has URL, user-name and password.
				 */
				.dataSource(dataSource)
				/**
				 * we have used parameterized fields in SQL. To map correct value to parameterized fields we need to use below 'beanMapped'
				 */
				.beanMapped()
				.build();

	}

}
