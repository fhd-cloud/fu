package com.fhd.fileUploadService.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		//override the default spring boot config to fix the issue with json error response for the url with file extension
		configurer.favorPathExtension(false)
				.defaultContentType(MediaType.APPLICATION_JSON)
				.favorParameter(true)
				.parameterName("mediaType")
				.ignoreAcceptHeader(true);

	}
}
