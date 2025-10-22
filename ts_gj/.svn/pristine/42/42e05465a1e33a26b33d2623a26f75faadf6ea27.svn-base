1. 패스워드 암호화
 - SpringSecurityConfig.java 파일에서 DelegatingPasswordEncoder을 설정
 - 서비스 중 암호화 알고리즘 변경 필요하면 application.properties 또는 application-prod.properties 에서 security.password.encoder.id 수정
 
2. profile
 - 서버배포시 Readme와 같은 경로에 있는 start-tomcat.sh에 prod로 등록되어있음.
 - 개발시 application.properties 
   운영시 application-prod.properties 가 자동으로 정해짐
 
3. 배포
 - 첨부된 start-tomcat.sh, stop-tomcat.sh 참고
 
4. remember-me 보안처리
 - PersistentTokenRepository를 하기 위해 테이블 생성 필요
   CREATE TABLE `persistent_logins` (
		`username` VARCHAR(64) NOT NULL COLLATE 'utf8_general_ci',
		`series` VARCHAR(64) NOT NULL COLLATE 'utf8_general_ci',
		`token` VARCHAR(64) NOT NULL COLLATE 'utf8_general_ci',
		`last_used` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		PRIMARY KEY (`series`) USING BTREE
	);

5. AOP
 - 반복적인 try-catch 작성을 줄이고 작업 효율성 증대
 - service에서 exception 발생시 에러로그 출력 후 빈값을 리턴
 - controller에서 exception 발생시 에러로그 출력 후 에러페이지 이동
 - 특수하게 exception을 가지고 개발에 활용해야하는 경우 ExceptionHandlingAspect에서 예외처리
 
6. StringUtil
 - 반복적인 null Check 등을 모아놓고 사용
  
7. 샘플 게시물
 - 기본적인 crud 샘플
 - BbsController 참고
 - 시리얼 넘버는 암호화(복호화 가능) 처리
 
8. 페이지 정보
 - title, 메뉴명 등 페이지정보는 controller에서 셋팅하고 프론트에서 출력

9. XSS 방지
 - controller > 프론트로 츨력되는 string은 타임리프을 사용하여 출력한다. th:text or [[...]]
 
10. csrf 방지 (아직안함)
 - spring security 활용
 - header에 form태그마다 csrf 토큰을 생성
 - header에 ajax마다 csrf 토큰을 생성
 
11. 페이지 이용통계
 - @CustomStat 어노테이션을 등록하면 인터셉터를 통해 이용통계를 등록한다.
 - BbsController.bbsList 메서드 참고

