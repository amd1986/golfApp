package fr.greta.golf.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // To solver URL like:
    // /SomeContextPath/en/something
    // /SomeContextPath/es/something
    // /SomeContextPath/fr/something
    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver(){
        return new UrlLocaleResolver();
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageResource()  {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();

        // Read i18n/messages_xxx.properties file.
        // For example: i18n/messages_en.properties
        messageResource.setBasename("classpath:i18n/messages");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UrlLocaleInterceptor localeInterceptor = new UrlLocaleInterceptor();

        registry.addInterceptor(localeInterceptor).addPathPatterns("/en/*", "/fr/*", "/es/*");
    }
}
