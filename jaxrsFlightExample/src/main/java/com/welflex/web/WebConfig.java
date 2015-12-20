package com.welflex.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.welflex.dao.DefaultDaoConfig;
import com.welflex.service.DefaultServiceConfig;
import com.welflex.web.resource.ResourceConfig;
import com.welflex.web.security.SecurityConfig;

@Configuration
@Import({ ResourceConfig.class, DefaultServiceConfig.class, DefaultDaoConfig.class, SecurityConfig.class})
public class WebConfig {
}   
