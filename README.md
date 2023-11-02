# myTime Repository

### 프로젝트 설명
myTime은 일정, 할일, 습관이 관리가 가능하도록 만든 캘린더 애플리케이션 입니다.   
프로젝트 인원구성이 오직 백엔드로만 이루어져 있어서, 애플리케이션의 프론트는 없는 상태입니다.

### 사용 기술
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white">

<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/h2-6DB33F?style=for-the-badge&logo=h2&logoColor=white"> <img src="https://img.shields.io/badge/JPA-FF6600?style=for-the-badge&logo=JPA&logoColor=white"> <img src="https://img.shields.io/badge/QueryDSL-FF6600?style=for-the-badge&logo=QueryDSL&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JDBC-FF6600?style=for-the-badge&logo=Spring Data JDBC&logoColor=white">

<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/Spring RestDocs-6DB33F?style=for-the-badge&logo=Spring RestDocs&logoColor=white"> <img src="https://img.shields.io/badge/Spring AOP-6DB33F?style=for-the-badge&logo=Spring AOP&logoColor=white">

<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JWT&logoColor=white"> <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> 

<img src="https://img.shields.io/badge/amazon aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/amazon rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/amazon s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/amazon Elasticache-232F3E?style=for-the-badge&logo=amazonelasticache&logoColor=white"> <img src="https://img.shields.io/badge/amazon IAM-FF9900?style=for-the-badge&logo=amazoniam&logoColor=white"> <img src="https://img.shields.io/badge/amazon vpc-DC382D?style=for-the-badge&logo=amazonvpc&logoColor=white"> <img src="https://img.shields.io/badge/amazon codedeploy-000000?style=for-the-badge&logo=amazoncodedeploy&logoColor=white"> 

### 기능 목록
- [API 명세서](https://github.com/CodeQuartette/myTime/wiki/API)
- [기능 명세서](https://github.com/CodeQuartette/myTime/wiki/%EA%B8%B0%EB%8A%A5-%EB%AA%85%EC%84%B8)

### 배포가 완료된 API DOCS 
|환경|url|ETC|
|---|----|---|
|AWS|http://43.202.211.239:8080/docs/index.html||

### 프로젝트 환경 구성
- 그림 첨부


### 배포 파이프라인(GITHUB ACTION)
![image](https://github.com/CodeQuartette/myTime/assets/45054467/d1a441a0-c0ff-4833-9252-39d94ef68857)



### Local로 실행 방법
#### Repository Clone
```
git clone https://github.com/CodeQuartette/myTime.git
```

#### JDK 설치
JDK17(이)가 설치되어 있는 경우 사용 가능. JDK17을 설치해주세요.

#### Docker로 설치해야하는 환경
- MySQL
- Redis

##### docker가 설치되어 있는 경우 사용 가능. docker를 설치해 주세요.

docker 설치: [Download Docker](https://www.docker.com/products/docker-desktop/)

```
cd mytime/
docker-compose up -d
```

#### Local 실행
```
./gradlew copyDocument //spring-rest-docs의 정적리소스를 먼저 컴파일해야지만 프로젝트 실행할 때 docs가 정상동작
./gradlew build --info //build되는 내용을 자세히 검토하고 싶을 때 사용하는 명령어
java -jar -Dspring.profiles.active=local ./build/libs/myTime-0.0.1-SNAPSHOP.jar
```

### 개발 방식
- [Convention](https://github.com/CodeQuartette/myTime/wiki/Convention)

### 프로젝트 팀원 소개
|이름|GitHub URL|소개|
|---|----------|---|
|Robin(윤혜지)|https://github.com/malaheaven| 안녕하세요. 마라탕을 좋아하는 개발자 윤혜지입니다.|
|이노(정인호)|https://github.com/eNoLJ| 안녕하세요. 햄버거를 좋아하는 개발자 정인호입니다.| 
|Tree(최희윤)|https://github.com/choitree| 안녕하세요. 돈가스를 좋아하는 개발자 최희윤입니다.|
|희건(이희건)|https://github.com/2heekeon|안녕하세요. 양꼬치를 좋아하는 개발자 이희건입니다.|








