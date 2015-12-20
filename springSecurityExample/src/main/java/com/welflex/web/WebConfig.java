package com.welflex.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.welflex.dao.DefaultDaoConfig;
import com.welflex.model.Airport;
import com.welflex.model.Error;
import com.welflex.model.Flight;
import com.welflex.model.FlightSearchCriteria;
import com.welflex.model.Flights;
import com.welflex.model.Reservation;
import com.welflex.model.Reservations;
import com.welflex.model.Ticket;
import com.welflex.service.DefaultServiceConfig;
import com.welflex.web.controller.ControllerConfig;
import com.welflex.web.security.SecurityConfig;

/**
 * This is the final config that defines Web Application configuration and imports Service, Dao and
 * Controller Configuration Rather than doing an import, one can choose to do classpath scanning if
 * thats what is desired via:
 * 
 * <pre>
 *   @ComponentScan(basePackages = "com.welflex")
 * </pre>
 * 
 * The @EnableMvc is a hook-in into the Spring Servlet
 */
@EnableWebMvc
@Configuration
@Import({ ControllerConfig.class, DefaultServiceConfig.class, DefaultDaoConfig.class,
    SecurityConfig.class })
public class WebConfig extends WebMvcConfigurerAdapter {
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addFormatter(new DateFormatter("MM-dd-yyyy"));
  }

  public static final class DateFormatter implements Formatter<Date> {

    private String pattern;

    public DateFormatter(String pattern) {
      this.pattern = pattern;
    }

    public String print(Date date, Locale locale) {
      if (date == null) {
        return "";
      }
      return getDateFormat(locale).format(date);
    }

    public Date parse(String formatted, Locale locale) throws ParseException {
      if (formatted.length() == 0) {
        return null;
      }
      return getDateFormat(locale).parse(formatted);
    }

    protected DateFormat getDateFormat(Locale locale) {
      DateFormat dateFormat = new SimpleDateFormat(this.pattern, locale);
      dateFormat.setLenient(false);
      return dateFormat;
    }
  }

  @Override
  public Validator getValidator() {
    return new LocalValidatorFactoryBean();
  }

  @Bean
  public ViewResolver getViewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setViewClass(JstlView.class);
    resolver.setPrefix("/WEB-INF/pages/");
    resolver.setSuffix(".jsp");

    return resolver;
  }

  private static final Class<?>[] JAXB_CLASSES_TO_BE_BOUND = { Airport.class, Reservations.class,
      Reservation.class, Ticket.class, Flight.class, FlightSearchCriteria.class, Flights.class,
      Error.class };

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new StringHttpMessageConverter());
    Jaxb2Marshaller m = new Jaxb2Marshaller();
    m.setClassesToBeBound(JAXB_CLASSES_TO_BE_BOUND);

    MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();

    converter.setMarshaller(m);
    converter.setUnmarshaller(m);
    converters.add(converter);
    
    converters.add(new MappingJacksonHttpMessageConverter());
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }
}
