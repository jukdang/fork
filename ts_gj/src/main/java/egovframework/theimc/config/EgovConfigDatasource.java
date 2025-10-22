package egovframework.theimc.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class EgovConfigDatasource {

	private final DataSourceProperties properties;

    // Spring Boot가 자동으로 설정한 DataSourceProperties를 주입받음
    public EgovConfigDatasource(DataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean(name="dataSource")
    public DataSource dataSource() {
        // application.properties의 설정을 사용하여 DataSource 생성
        return properties.initializeDataSourceBuilder().build();
    }
}
