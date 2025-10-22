package egovframework.theimc.login.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.login.vo.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private LoginMapper loginMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            MbrVO user = loginMapper.findByUsername(username);
            
            if (user == null) {
            	if (log.isErrorEnabled()) {
            		log.error("[ERROR] CustomUserDetailsService loadUserByUsername UsernameNotFoundException: 사용자를 찾을 수 없습니다: {}", username);
            	}
            	throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
            }
            
            // authOwnrInfo → List<String> 변환
            if (user.getAuthOwnrInfo() != null) {
                List<String> list = Arrays.stream(user.getAuthOwnrInfo().split(","))
                                          .map(String::trim)
                                          .collect(Collectors.toList());
                user.setAuthOwnrList(list);
            } else {
                user.setAuthOwnrList(Collections.emptyList());
            }
            
            // 기존 Spring Security User 대신 CustomUserDetails 사용
            return new CustomUserDetails(user);
            
        } catch (DataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomUserDetailsService loadUserByUsername DataAccessException: ", e);
            }
            throw new UsernameNotFoundException("데이터베이스 접근 오류");
        } catch (NullPointerException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomUserDetailsService loadUserByUsername NullPointerException: ", e);
            }
            throw new UsernameNotFoundException("데이터 누락 오류");
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomUserDetailsService loadUserByUsername IllegalArgumentException: ", e);
            }
            throw new UsernameNotFoundException("인자 오류");
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] CustomUserDetailsService loadUserByUsername Exception: ", e);
            }
            throw new UsernameNotFoundException("사용자 조회 중 오류 발생");
        }
    }
}