package br.com.security.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * The type Mongo populator config.
 */
@Configuration
@RequiredArgsConstructor
public class MongoPopulatorConfig {

	/**
	 * The Resolver.
	 */
	private final ResourcePatternResolver resolver;
	/**
	 * The Mapper.
	 */
	private final ObjectMapper mapper;

	/**
	 * Mongo populator factory jackson 2 repository populator factory bean.
	 *
	 * @return the jackson 2 repository populator factory bean
	 * @throws IOException the io exception
	 */
	@Bean
	public Jackson2RepositoryPopulatorFactoryBean mongoPopulatorFactory() throws IOException {
		final Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
		factory.setResources(this.resolver.getResources("classpath:*.json"));
		factory.setMapper(this.mapper);
		return factory;
	}

}
