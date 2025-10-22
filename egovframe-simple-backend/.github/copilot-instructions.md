# eGov Framework Simple Backend - AI Coding Instructions

## Project Overview
This is a **Korean e-Government Framework (eGovFrame) v4.3.0** Spring Boot application - a government standard framework widely used in Korean public sector development. This is currently a **minimal skeleton project** awaiting feature implementation.

## Tech Stack & Architecture
- **Spring Boot 2.7.18** with Java 17
- **eGov Framework 4.3.0** - Korean government's standardized development framework
- **MyBatis** for data access with XML mappers
- **MariaDB** database (configured for `localhost:3306/egovframe`)
- **Thymeleaf** for web views
- **Spring Security** configured
- **Gradle 8.1** build system

## Critical Framework Conventions

### Package Structure
```
egovframework.theimc.{module}.{layer}
```
- Use `egovframework.theimc` as root package (Korean convention)
- Follow eGov's layered architecture: `web` → `service` → `service.impl` → `dao`
- MyBatis mappers scan: `egovframework.theimc.*.service.impl`

### eGov-Specific Patterns
- **EgovMap**: Use `org.egovframe.rte.psl.dataaccess.util.EgovMap` instead of regular Map for DB results
- **Service Implementation**: Business logic goes in `service.impl` packages, interfaces in `service`
- **Mapper Configuration**: XML mappers in `src/main/resource/sqlmap/mappers/`
- **ID Generation**: Use eGov's `org.egovframe.rte.fdl.idgnr` for unique IDs

### Database Access
- MyBatis XML mappers are preferred over annotations
- Use `egovMap` type alias (configured in `sql-mapper-config.xml`)
- Mapper interface beans automatically discovered via `@MapperScan`

## Development Commands

### Build & Run
```bash
# Development mode with hot reload
./gradlew bootRun

# Build JAR
./gradlew bootJar

# Run tests
./gradlew test
```

### Database Setup
- MariaDB required at `localhost:3306`
- Database: `egovframe`
- Credentials: `root/root` (dev environment)

## Configuration Notes

### Logging
- **Log4j2** exclusively (Spring Boot's default logging excluded)
- eGov Framework excludes commons-logging to prevent conflicts

### Security
- Spring Security enabled but not configured
- Session timeout: 3600 seconds
- Context path: `/` (root)

### Development Features
- Spring DevTools enabled for hot reload
- Ant path matching strategy for MVC
- Profile: `dev` (active)

## Common Implementation Patterns

### Creating New Modules
1. Create package structure: `egovframework.theimc.{module}.{web|service|service.impl}`
2. Add controller in `web` package with `@Controller`
3. Create service interface and implementation
4. Add MyBatis mapper interface in `service.impl`
5. Create XML mapper in `sqlmap/mappers/{Module}Mapper.xml`

### Typical Controller Pattern
```java
@Controller
public class {Module}Controller {
    @Autowired
    private {Module}Service {module}Service;
    
    @RequestMapping("/egov/{module}/{action}.do")
    // Korean gov URLs typically use .do extension
}
```

### MyBatis Mapper Pattern
- Interface in `service.impl` package
- XML in `sqlmap/mappers/`
- Use `egovMap` for result types
- Follow eGov naming conventions

## File Locations
- **Main Application**: `src/main/java/egovframework/theimc/EgovApplication.java`
- **MyBatis Config**: `src/main/resource/sqlmap/sql-mapper-config.xml`
- **DB Config**: `src/main/resource/application.properties`
- **Mapper Beans**: Auto-discovered via `EgovConfigMapper.java`

## Next Development Steps
This is a minimal framework setup. Typical next steps:
1. Implement authentication/authorization
2. Create domain-specific modules (user, board, file, etc.)
3. Add proper error handling and logging configuration
4. Set up production database configuration profiles

## eGov Framework Dependencies
Uses official Korean eGov maven repository: `https://maven.egovframe.go.kr/maven/`
- MVC layer: `org.egovframe.rte.ptl.mvc`
- Data access: `org.egovframe.rte.psl.dataaccess` 
- ID generation: `org.egovframe.rte.fdl.idgnr`
- Properties: `org.egovframe.rte.fdl.property`