package com.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.batch.entity.Student;
import com.spring.batch.repository.StudentRepository;

@Configuration
public class BatchConfig {

	/**
	 * Spring provides JobRepository automatically
	 */
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private StudentRepository studentRepository;

	/**
	 * Creating reader step of spring batch
	 */
	@Bean
	FlatFileItemReader<Student> reader() {
		/**
		 * Using FlatFileItemReader to read the CSV file.
		 */
		FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
		/**
		 * Path from where we are reading the CSV file
		 */
		itemReader.setResource(new FileSystemResource("src/main/resources/students.csv"));
		/**
		 * Giving any name to reader
		 */
		itemReader.setName("csvReader");
		/**
		 * We want to skip first line of Header of our CSV
		 */
		itemReader.setLinesToSkip(1);

		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	/**
	 * Creating processor step of spring batch
	 */
	@Bean
	StudentProcessor processor() {
		return new StudentProcessor();
	}

	/**
	 * Creating writer step of spring batch
	 */
	@Bean
	RepositoryItemWriter<Student> writer() {
		/**
		 * Using RepositoryItemWriter to write data into database.
		 */
		RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
		/**
		 * what is our repository name, we want to use.
		 */
		writer.setRepository(studentRepository);
		/**
		 * repository method which we want to use.
		 */
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	Step step1() {
		return new StepBuilder("csvImport", jobRepository)
				/**
				 * chunk means: batch size transactionManager: to manage the transactions
				 * 
				 * <Student, Student> : this is input and output types
				 */
				.<Student, Student>chunk(1000, platformTransactionManager)

				/**
				 * Steps has three steps: ItemReader ItemProcessor ItemWriter
				 */
				.reader(reader()) // reader: this is created above in this class
				.processor(processor()) // processor: this is created above in this class
				.writer(writer()) // writer: this is created above in this class

				.taskExecutor(taskExecutor()).build();
	}

	/**
	 * Creating Job Bean which is the first step for spring-batch
	 */
	@Bean
	Job runJob(JobRepository jobRepository, JobExecutionListenerImpl jobExecutionListener) {
		/**
		 * Job takes the JobRepository. so we have to configure it JobRepository: This
		 * repository responsible for persistence of batch meta-data entities.
		 */
		return new JobBuilder("importStudents", jobRepository)
				/**
				 * we want to run some code before or after executing the job. So we are
				 * configuring it.
				 * 
				 * This is custom class.
				 */
				.listener(jobExecutionListener)
				/**
				 * Job is made of steps. So, we need to configure steps.
				 */
				.start(step1())
				// .next(step2) // like this we can provide multiple steps
				.build();

	}

	@Bean
	TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		/**
		 * how many parallel threads we want to run.
		 * by defaults: its using only one thread.
		 */
		asyncTaskExecutor.setConcurrencyLimit(3);
		return asyncTaskExecutor;
	}

	/**
	 * Goal of this LineMapper will map eash line of our file here to a Student
	 * object
	 */
	private LineMapper<Student> lineMapper() {
		DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		/**
		 * our CSV file is separated by comma
		 */
		lineTokenizer.setDelimiter(",");
		/**
		 * not strict
		 */
		lineTokenizer.setStrict(false);
		/**
		 * Column names of our CSV file
		 */
		lineTokenizer.setNames("firstName", "lastName", "age");

		/**
		 * This BeanWrapperFieldSetMapper is an object that will allow us and help us to
		 * transform above each line that will read from the CSV file to a Student
		 * object.
		 */
		BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Student.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
}
