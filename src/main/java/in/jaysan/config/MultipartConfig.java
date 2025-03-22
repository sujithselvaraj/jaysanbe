package in.jaysan.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {
    @Bean
    public MultipartConfigFactory multipartConfigFactory() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("10MB"));
        factory.setMaxRequestSize(DataSize.parse("10MB"));
        return factory;
    }
}
