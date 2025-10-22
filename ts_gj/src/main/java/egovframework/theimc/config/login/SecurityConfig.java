package egovframework.theimc.config.login;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableScheduling
public class SecurityConfig {

    @Value("${security.remember-me.token-validity-seconds:604800}")
    private int rememberMeTokenValiditySeconds;

    @Autowired
    private UserDetailsService customUserDetailsService;

    // application.properties에서 기본 인코더 ID 설정값을 가져옴
    @Value("${security.password.encoder.id}")
    private String idForEncode;

    // application.properties에서 기본 인코더 ID 설정값을 가져옴
    @Value("${svc.repairAssist.api}")
    private String repairAssist;

    // PasswordEncoder 빈 설정 (기존과 동일)
    @Bean
    public static PasswordEncoder passwordEncoder(
            @Value("${security.password.encoder.id}") String idForEncode) {

        // 지원할 인코더 맵 생성
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        // 다양한 인코더 추가
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        // 기존 SHA-512 인코더 대신 KISA SHA-256 인코더 추가
        encoders.put("kisa-sha256", new KisaSHA256PasswordEncoder(10000));
        // null ID를 위한 인코더 추가 (기존 비밀번호 지원)
        encoders.put("null", new BCryptPasswordEncoder());

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
        // null prefix를 가진 비밀번호를 위한 기본 인코더 설정 (기존 비밀번호가 BCrypt라고 가정)
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());

        return delegatingPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws UsernameNotFoundException, IllegalArgumentException, GeneralSecurityException {
        try {
            auth.userDetailsService(customUserDetailsService)
                    .passwordEncoder(passwordEncoder(idForEncode));
        } catch (UsernameNotFoundException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SecurityConfig configureGlobal UsernameNotFoundException: ", e);
            }
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SecurityConfig configureGlobal IllegalArgumentException: ", e);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SecurityConfig configureGlobal Exception: ", e);
            }
        }
    }

    @Bean
    public LoginFailHandler loginFailHandler() {
        return new LoginFailHandler();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public LogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new CustomPersistentTokenRepository();
    }

    private static final String[] ORIGINS_WHITELIST = {
            "http://localhost:3000",
    };

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedOrigins(Arrays.asList(ORIGINS_WHITELIST));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws IOException, javax.servlet.ServletException {
        String cspPolicy = "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline'; " +
                "style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data: https://*.openstreetmap.org https://*.tile.openstreetmap.org; " +
                "connect-src 'self' https://*.openstreetmap.org " + repairAssist + "; " +
                "font-src 'self' data:; " +
                "object-src 'none'; " +
                "frame-src 'self';";

        try {
            http
                    .cors(cors -> cors
                            .configurationSource(corsConfigurationSource()))
                    .headers(headers -> headers
                            .contentSecurityPolicy(cspPolicy))
                    .csrf(csrf -> csrf
                            .ignoringAntMatchers("/api/rfid/**", "/mngr/**", "/operation/**") // RFID API만 CSRF 제외
                    // .ignoringAntMatchers("**") // 전체 CSRF 제외
                    )
                    // .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests((authorize) -> authorize
                            .antMatchers("/.well-known/**").permitAll()
                            // .antMatchers("*").permitAll()
                            .antMatchers("/api/rfid/**").permitAll()
                            // 정적 리소스는 모든 context path에서 접근 가능하도록
                            .antMatchers("/css/**", "/js/**", "/images/**", "/summernote-0.8.18-dist/**",
                                    "/favicon.ico")
                            .permitAll()
                            .antMatchers("**/css/**", "**/js/**", "**/images/**", "**/summernote-0.8.18-dist/**",
                                    "**/favicon.ico")
                            .permitAll()
                            // egovframework 이미지 경로 추가
                            .antMatchers("/images/egovframework/**", "**/images/egovframework/**").permitAll()
                            .antMatchers("/mbr/mbrAgree", "/mbr/mbrResult", "/mbr/mbrInsertForm", "/mbr/mbrSelfInsert",
                                    "/mbr/chkMbrId")
                            .permitAll()
                            .antMatchers("/login/loginForm").permitAll()
                            .antMatchers("/admin/**").hasAnyRole("ADMIN")
                            .antMatchers("/mngr/**").permitAll()
                            .antMatchers("/operation/**").permitAll()
                            .antMatchers("/dashboard/**").hasAnyRole("ADMIN", "MANAGER")
                            .anyRequest().authenticated())
                    .formLogin((form) -> form
                            .loginPage("/login/loginForm")
                            .loginProcessingUrl("/login/loginProc")
                            .usernameParameter("mbrId")
                            .passwordParameter("mbrPswd")
                            .defaultSuccessUrl("/")
                            .successHandler(loginSuccessHandler())
                            .failureHandler(loginFailHandler())
                            .permitAll())
                    .rememberMe(remember -> remember
                            .key("uniqueAndSecretKey")
                            .tokenRepository(persistentTokenRepository())
                            .tokenValiditySeconds(rememberMeTokenValiditySeconds) // 7일 (초 단위)
                    )
                    .logout((logout) -> logout
                            .logoutUrl("/logout")
                            .addLogoutHandler(customLogoutHandler()) // 새로운 로그아웃 핸들러 추가
                            .logoutSuccessUrl("/login/loginForm") // 로그아웃 성공 시 이동할 URL
                            .deleteCookies("JSESSIONID", "remember-me")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .permitAll());

            return http.build();
        } catch (IOException | javax.servlet.ServletException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SecurityConfig securityFilterChain IOException | javax.servlet.ServletException: ",
                        e);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] SecurityConfig securityFilterChain IllegalArgumentException: ", e);
            }
        }
        return null;
    }
}