## 프로젝트 소개
학원 과정에서 기초 JDBC를 사용해 CRUD를 구현하고,  
프레임워크 도움 없이 **자바 코드로 직접 서블릿을 등록하며 웹 요청·응답 흐름을 경험**해본 이후,  
페이징, 조회 조건 처리 등 기본적인 웹 기능들을 하나씩 직접 구현해왔습니다.

이 프로젝트는 그러한 기초 학습 경험과  
이후 쌓은 **실무 경험 및 학습한 Spring 기반 기술들을 바탕으로**,  
웹 서비스의 구조와 흐름을 다시 한 번 정리하며 새로 만들어보는 개인 웹 프로젝트입니다.

과도한 기능 추가보다는,  
각 기능이 왜 필요한지와 어떤 역할을 가지는지를 이해하는 데에 초점을 두고 개발하고 있습니다.

## 프로젝트 목표
학원 과정에서 구현했던 **세션 기반 인증 방식**을 출발점으로,  
Spring Security와 JWT를 활용한 인증 구조로 전환하며  
현대적인 웹 애플리케이션의 인증 흐름을 직접 설계하고 구현하는 것을 목표로 합니다.

데이터 접근 방식에 있어서는  
Spring Data JPA를 중심으로 사용하되,  
상황에 따라 MyBatis를 함께 활용하며  
각 기술의 장단점과 적용 기준을 비교·정리하는 데에 중점을 둡니다.

또한 OAuth2 인증을 직접 도입하여  
외부 인증 제공자와 연동되는 흐름을 경험하고,  
기존 로그인 구조와 어떻게 공존하거나 대체될 수 있는지를 고민합니다.

이 프로젝트는 단순히 기능을 많이 구현하는 것이 아니라,  
**기존 방식에서 새로운 방식으로 전환하는 과정과 그 이유를 명확히 이해하는 것**을 목표로 합니다.

## 기술 스택

### Backend
- Java 21
- Spring Boot 3.4.4
- Spring Web
- Spring Validation
- Spring Security
- Spring Data JPA
- QueryDSL 5.0.0
- MyBatis
- JWT (jjwt 0.13.0)
- OAuth2 Client
- Lombok
- Swagger 2.7.0

### Database
- MySQL (운영 DB)
- H2 (테스트 DB)

### Infra / DevOps
- AWS EC2
- AWS Route 53
- Nginx
- Docker
- Jenkins

### Build / Tooling
- Gradle
- IntelliJ IDEA
