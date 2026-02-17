package com.gametransaction.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * Web Application Initializer - replaces web.xml
 * Pure Java configuration, no XML files needed
 * Spring Data JPA automatically configured via @EnableJpaRepositories
 */
public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        // Create root application context for database and services
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(DataSourceConfig.class);

        // Create servlet application context for MVC
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebMvcConfig.class);

        // Register ContextLoaderListener
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Register DispatcherServlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "dispatcher",
                new DispatcherServlet(dispatcherContext)
        );
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // Register Character Encoding Filter
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter(
                "characterEncodingFilter",
                new CharacterEncodingFilter()
        );
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
        characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");

        // Configure session tracking
        servletContext.getSessionCookieConfig().setHttpOnly(true);
    }
}
