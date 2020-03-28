package com.company.config;

import com.company.repositories.FileJdbcTemplateImpl;
import com.company.repositories.FileRepository;
import com.company.repositories.UserJdbcTemplateImpl;
import com.company.repositories.UserRepository;
import com.company.services.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.sql.DataSource;
import java.util.Objects;

@Component
@Configuration
@ComponentScan(basePackages = "com.company")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class ApplicationContextConfig {
    @Autowired
    private Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SignUpService signUpService() {
        return new SignUpServiceImpl(signUpPool(), userRepository(), emailSender());
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public TemplateResolver templateResolver() {
        return new TemplateResolverImpl();
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftlh");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(Objects.requireNonNull(freeMarkerConfiguration()));
        return freeMarkerConfigurer;
    }

    @Bean
    public freemarker.template.Configuration freeMarkerConfiguration() {
        return (freemarker.template.Configuration) freeMarkerConfigurationFactoryBean().getObject();
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
        FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
        freeMarkerFactoryBean.setTemplateLoaderPath("/WEB-INF/templates/");
        freeMarkerFactoryBean.setPreferFileSystemAccess(true);
        freeMarkerFactoryBean.setDefaultEncoding("UTF-8");
        return freeMarkerFactoryBean;
    }

    @Bean
    public StorageFilenameGenerator storageFilenameGenerator() {
        return new StorageFilenameGeneratorImpl();
    }

    @Bean
    public FileUploader fileUploader() {
        String directory = environment.getProperty("path.filesDirectory");
        return new FileUploaderImpl(directory, fileRepository(), storageFilenameGenerator());
    }

    @Bean
    public FileRepository fileRepository() {
        return new FileJdbcTemplateImpl(jdbcTemplate());
    }

    @Bean
    public UserRepository userRepository() {
        return new UserJdbcTemplateImpl(jdbcTemplate());
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSenderImpl();
    }

    @Bean
    public SignUpPool signUpPool() {
        return new SignUpPool(userRepository());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty("db.url"));
        config.setUsername(environment.getProperty("db.user"));
        config.setPassword(environment.getProperty("db.password"));
        config.setDriverClassName(environment.getProperty("db.driver"));
        return config;
    }

    @Bean
    public DataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }
}
