package com.mrtrakwon.oauthclient.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.zaxxer.hikari.HikariDataSource;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories("com.mrtrakwon.oauthclient.domain")
public class JpaConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource(){
		return DataSourceBuilder.create()
			.type(HikariDataSource.class)
			.build();
	}
}
