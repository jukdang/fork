package egovframework.theimc.config;

import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import egovframework.theimc.config.interceptor.StatisticsInterceptor;
import egovframework.theimc.pagination.EgovPaginationDialect;

@Configuration
@Import({
//		EgovConfigAspect.class,
		EgovConfigCommon.class,
		EgovConfigDatasource.class,
		EgovConfigIdGeneration.class,
		EgovConfigMapper.class,
		EgovConfigProperties.class,
		EgovConfigTransaction.class,
		EgovConfigValidation.class
})
public class EgovConfigWeb implements WebMvcConfigurer, ApplicationContextAware {

	private ApplicationContext applicationContext;

	public void setApplicationContext(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Value("${spring.thymeleaf.cache}")
    private boolean cacheable;
	
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setPrefix("classpath:/templates/thymeleaf");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(cacheable);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver());
	    templateEngine.setEnableSpringELCompiler(true);
	    // add custom tag
	    templateEngine.addDialect(new EgovPaginationDialect());
	    // Spring Security Dialect 추가
	    templateEngine.addDialect(securityDialect());
	    return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setTemplateEngine(templateEngine());
		return viewResolver;
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/egovframework/**")
               .addResourceLocations("classpath:/static/css/egovframework/")
               .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
               .resourceChain(true);
        registry.addResourceHandler("/js/egovframework/**")
	        .addResourceLocations("classpath:/static/js/egovframework/")
	        .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
	        .resourceChain(true);
        registry.addResourceHandler("/images/egovframework/**")
	        .addResourceLocations("classpath:/static/images/egovframework/")
	        .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
	        .resourceChain(true);
        registry.addResourceHandler("/summernote-0.8.18-dist/**")
               .addResourceLocations("classpath:/static/summernote-0.8.18-dist/")
               .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
    }

	@Bean
	public SessionLocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("language");
		return interceptor;
	}

	// StatisticsInterceptor 빈 등록
    @Bean
    public StatisticsInterceptor statisticsInterceptor(SqlSession sqlSession) {
        return new StatisticsInterceptor(sqlSession);
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(statisticsInterceptor(applicationContext.getBean(SqlSession.class)));
	}

//	@Override
//	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//		Properties prop = new Properties();
//		prop.setProperty("org.springframework.dao.DataAccessException", "egovSampleError");
//		prop.setProperty("org.springframework.transaction.TransactionException", "egovSampleError");
//		prop.setProperty("org.egovframe.rte.fdl.cmmn.exception.EgovBizException", "egovSampleError");
//		prop.setProperty("org.springframework.security.AccessDeniedException", "egovSampleError");
//		prop.setProperty("java.lang.Throwable", "egovSampleError");
//
//		Properties statusCode = new Properties();
//		statusCode.setProperty("egovSampleError", "400");
//		statusCode.setProperty("egovSampleError", "500");
//
//		SimpleMappingExceptionResolver smer = new SimpleMappingExceptionResolver();
//		smer.setDefaultErrorView("egovSampleError");
//		smer.setExceptionMappings(prop);
//		smer.setStatusCodes(statusCode);
//		resolvers.add(smer);
//	}
	
    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean(name = "jsonView")
    public MappingJackson2JsonView jsonView() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.setPrettyPrint(true);
        return jsonView;
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        resolver.setOrder(1);
        return resolver;
    }
}
