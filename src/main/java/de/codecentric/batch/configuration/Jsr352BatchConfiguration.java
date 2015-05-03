/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.codecentric.batch.configuration;

import javax.sql.DataSource;

import de.codecentric.batch.listener.AddListenerToJobService;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.jsr.JsrJobParametersConverter;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.batch.jsr352.CustomJsrJobOperator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * This configuration creates the components needed for starting JSR-352 style jobs.
 * 
 * @author Tobias Flohre
 */
@Configuration
public class Jsr352BatchConfiguration {
	
	@Autowired
	private JobExplorer jobExplorer;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private AddListenerToJobService addListenerToJobService;
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Bean
	public CustomJsrJobOperator jsrJobOperator(DataSource dataSource) throws Exception{
		CustomJsrJobOperator jsrJobOperator = new CustomJsrJobOperator(jobExplorer, jobRepository, jsrJobParametersConverter(), addListenerToJobService, platformTransactionManager);
		jsrJobOperator.setTaskExecutor(taskExecutor);
		return jsrJobOperator;
	}
	
	public JsrJobParametersConverter jsrJobParametersConverter() throws Exception{
		JsrJobParametersConverter jsrJobParametersConverter = new JsrJobParametersConverter(dataSource);
		jsrJobParametersConverter.afterPropertiesSet();
		return jsrJobParametersConverter;
	}

}
