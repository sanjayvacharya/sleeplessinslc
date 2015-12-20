package com.welflex.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.server.ApacheDSContainer;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

/**
 * Security Configuration using Java Config. This is the class where the entire 
 * system security behavior is defined. Filters, strategy for login, Authentication 
 * providers etc.
 * 
 * @author Sanjay Acharya
 */
@Configuration
public class SecurityConfig {
  private static final int EMBEDDED_LDAP_SERVER_PORT = 33388;
  
  /**
   * This bean starts an embedded LDAP Server. Note that <code>start</code>
   * is not called on the server as the same is done as part of the bean life cycle's
   * afterPropertySet() method.
   *
   * @return The Embedded Ldap Server 
   * @throws Exception
   */
  @Bean(name = "ldap-server")
  public ApacheDSContainer getLdapServer() throws Exception {
    ApacheDSContainer container = new ApacheDSContainer("o=welflex",
        "classpath:flightcontrol.ldiff");
    container.setPort(EMBEDDED_LDAP_SERVER_PORT);
    return container;
  }
  
  /**
   * @return The Spring Security Context for the LdapUserDetails
   */
  @Bean(name = "contextSource")
  public DefaultSpringSecurityContextSource getDefaultSpringSecurityContextSource() {
    DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
        "ldap://127.0.0.1:" + EMBEDDED_LDAP_SERVER_PORT + "/o=welflex");

    contextSource.setPassword("password");
    contextSource.setUserDn("uid=admin,ou=system");
    contextSource.setAnonymousReadOnly(true);

    return contextSource;
  }
  
  /**
   * Responsible for extracting User/Group information from the Ldap Server
   * 
   * @return LdapUserDetailsService 
   */
  @Bean
  public LdapUserDetailsService getLdapUserDetailService() {
    FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch("ou=users", "uid={0}",
        getDefaultSpringSecurityContextSource());

    DefaultLdapAuthoritiesPopulator defaultLdapAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(
        getDefaultSpringSecurityContextSource(), "ou=groups");
    defaultLdapAuthoritiesPopulator.setGroupSearchFilter("(uniqueMember={0})");
    defaultLdapAuthoritiesPopulator.setGroupRoleAttribute("cn");

    LdapUserDetailsService userDetailService = new LdapUserDetailsService(userSearch,
        defaultLdapAuthoritiesPopulator);

    return userDetailService;
  }

  /**
   * @return Cookie Service for management of Cookies
   */
  @Bean
  public CookieService getCookieService() {
    return new CookieService.Impl();
  }

  private static final String ANON_PROVIDER_KEY = "9000234288201316478";
  
  /**
   * @return The Authentication Manager to user
   */
  private ProviderManager getAuthenticationManager() {
    List<AuthenticationProvider> authManagers = new ArrayList<AuthenticationProvider>();
    DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();

    daoProvider.setUserDetailsService(getLdapUserDetailService());

    authManagers.add(daoProvider);

    authManagers.add(new AnonymousAuthenticationProvider(
      ANON_PROVIDER_KEY));

    ProviderManager providerManager = new ProviderManager(authManagers);

    return providerManager;
  }

  /**
   * The FilterProxyChain with the set of filters to apply.
   * 
   * @return The FilterProxyChain
   */
  @Bean(name = "springSecurityFilterChain")
  public FilterChainProxy getFilterChainProxy() {
    SecurityFilterChain chain = new SecurityFilterChain() {

      @Override
      public boolean matches(HttpServletRequest request) {
        // All goes through here
        return true;
      }

      @Override
      public List<Filter> getFilters() {
        List<Filter> filters = new ArrayList<Filter>();

        filters.add(getCookieAuthenticationFilter());
        filters.add(getLogoutFilter());
        filters.add(getUserNamePasswordAuthenticationFilter());
        filters.add(getSecurityContextHolderAwareRequestFilter());
        filters.add(getAnonymousAuthenticationFilter());
        filters.add(getExceptionTranslationFilter());
        filters.add(getFilterSecurityInterceptor());

        return filters;
      }
    };
    
    FilterChainProxy proxy = new FilterChainProxy(chain);

    return proxy;
  }

  // Filters
  
  /**
   * @return Filter that checks if a cookie exists for the user, if so loads the user 
   * details and sets the authentication context
   */
  private CookieAuthenticationFilter getCookieAuthenticationFilter() {
    return new CookieAuthenticationFilter(getLdapUserDetailService(), getCookieService());
  }
  
  /**
   * @return Filter for handling logout
   */
  private LogoutFilter getLogoutFilter() {
    return new LogoutFilter("/logoutSuccess.html", new CookieLogoutHandler(getCookieService()));
  }
  
  /**
   * @return Filter for authentication the user.
   */
  private UsernamePasswordAuthenticationFilter getUserNamePasswordAuthenticationFilter() {
    UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();

    filter.setAllowSessionCreation(false);
    filter.setAuthenticationManager(getAuthenticationManager());
    filter.setAuthenticationSuccessHandler(new AuthSuccessHandler(getCookieService()));
    filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login.html?login_error=1"));
    
    filter.setFilterProcessesUrl("/j_spring_security_check");

    return filter;
  }
  
  /**
   * @return Filter that manages the ROLE prefix
   */
  private SecurityContextHolderAwareRequestFilter getSecurityContextHolderAwareRequestFilter() {
    SecurityContextHolderAwareRequestFilter filter = new SecurityContextHolderAwareRequestFilter();
    filter.setRolePrefix("ROLE_");
    return filter;
  }

  /**
   * @return Anonymous Authentication Filter for IS_AUTHENTICATED_ANONYMOUSLY
   */
  private AnonymousAuthenticationFilter getAnonymousAuthenticationFilter() {
    return new AnonymousAuthenticationFilter("ClientApplication", "anonymousUser",
        AuthorityUtils.createAuthorityList(ANONYMOUS));
  }

  /**
   * @return Exception Translation filter
   */
  private ExceptionTranslationFilter getExceptionTranslationFilter() {
    LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint(
        "/login.html");
    AccessDeniedHandlerImpl errorHandler = new AccessDeniedHandlerImpl();
    errorHandler.setErrorPage("/login.html?login_error=1");

    ExceptionTranslationFilter filter = new ExceptionTranslationFilter(entryPoint,
        new NullRequestCache());

    filter.setAccessDeniedHandler(errorHandler);

    return filter;
  }
  
  private static final String ANONYMOUS = "IS_AUTHENTICATED_ANONYMOUSLY";
  
  /**
   * Performs security handling of HTTP resources via a filter implementation
   * 
   * @return A FilterSecurityInterceptor
   */
  private FilterSecurityInterceptor getFilterSecurityInterceptor() {
    FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
   
    // Map anonymous access URL's
    Collection<ConfigAttribute> configAttributeCollection = new ArrayList<ConfigAttribute>();

    configAttributeCollection.add(new org.springframework.security.access.SecurityConfig(
        ANONYMOUS));

    requestMap.put(new AntPathRequestMatcher("/login*"), configAttributeCollection);
    requestMap.put(new AntPathRequestMatcher("/logoutSuccess*"), configAttributeCollection);
    requestMap.put(new AntPathRequestMatcher("/scripts*"), configAttributeCollection);
    requestMap.put(new AntPathRequestMatcher("/styles*"), configAttributeCollection);

    // Map /* for all user based resources
    Collection<ConfigAttribute> configAttributeCollection2 = new ArrayList<ConfigAttribute>();
    configAttributeCollection2.add(new org.springframework.security.access.SecurityConfig(
        "ROLE_USER"));

    requestMap.put(new AntPathRequestMatcher("/*"), configAttributeCollection2);

    FilterInvocationSecurityMetadataSource metaSource = new DefaultFilterInvocationSecurityMetadataSource(
        requestMap);

    interceptor.setSecurityMetadataSource(metaSource);

    // Set auth manager for the filter
    interceptor.setAuthenticationManager(getAuthenticationManager());

    // Access Decision Manager 
    @SuppressWarnings("rawtypes")
    List<AccessDecisionVoter> accessDecisionVoters = new ArrayList<AccessDecisionVoter>();

    accessDecisionVoters.add(new RoleVoter());
    accessDecisionVoters.add(new AuthenticatedVoter());
    AffirmativeBased decisionManager = new AffirmativeBased(accessDecisionVoters);
    
    interceptor.setAccessDecisionManager(decisionManager);

    return interceptor;
  }
}
