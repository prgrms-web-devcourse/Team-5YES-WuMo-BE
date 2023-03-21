# 🍄 WuMo (우리들의 모임)  
> 5YES's Team Project WuMo BackEnd   
> 기획 및 프로젝트 기간 : 2023.02.13 ~ 2023.03.14

<img src="https://user-images.githubusercontent.com/39071638/220715210-18e29cdc-b12e-470a-8e11-ed2122091e15.png" width="90%"/>

### 🌿 우리들만의 모임을 관리하는 플랫폼, WuMo ! 🌿

```
💡 모임 일정 및 추억 관리를 위한 프라이빗 서비스   
```

평소 여행이나 모임을 계획할 때 어떻게 하시나요?  
보통은 아마...
- 단체방을 만들어서 모임에 대한 이야기를 나눈다
- 가고 싶은 장소, 먹거리, 사진 등을 올린다
- 그러다가 딴 이야기가 나오면서 자연스레 모임/여행은 뒷전이 된다
- 다시 여행 이야기를 하려면 이전 기록을 보거나 검색을 다시 해야한다

<br/>

<img src="https://user-images.githubusercontent.com/39071638/220836993-deb40e92-753b-4832-bef9-0b163536a9e8.gif" style="witdh:300" height="600"/>  

그룹 채팅방에서는 여행과는 상관없는 이야기가 오갈 수 있어서, 모임에 필요한 장소나 사진 등이 잊혀지기 쉽습니다 

그렇다면 모임과 관련된 내용만 모아보면서     

가족, 지인들과 함께 모임, 여행을 계획하고 완성해가며 추억을 기록할 도구가 있다면 어떨까요?!  

<br/>

## 목차

- [🌼 주요 기능 및 타 서비스와의 차별성](#-주요-기능-및-타-서비스와의-차별성) 

- [🌠 팀 소개](#-팀-소개)    

- [🛠 기술 스택](#-기술-스택) 

- [📝 프로젝트 중점사항](#-프로젝트-중점사항)    
   
- [⚙ Infra Structure](#-infra-structure)    
 
- [🎫 ERD](#-erd)   

- [🌱 협업툴 Jira](#-협업툴-jira)

- [🍃 Branch 전략](#-branch-전략)   

- [🌿 Commit Convention](#-commit-convention)   

- [🧾 프로젝트 관련 문서](#-프로젝트-관련-문서)    

- [🌳 서비스 화면](#-서비스-화면)

<br/>

## 🌼 주요 기능 및 타 서비스와의 차별성

![wumo_service](https://user-images.githubusercontent.com/103614357/225658277-6cbb8ac0-9bf4-43ed-8b7d-416e1c189c68.png)

![wumo_service2](https://user-images.githubusercontent.com/103614357/225658100-c65b524c-b744-4d1b-b1fa-75af68520848.png)


<br/>

### 타 서비스와의 차별성   
- 여행 일정을 관리하는 서비스는 있으나, 하루 약속까지 관리할 수 있는 서비스는 없음   
- 다수의 인원이 함께 기록해나가는 서비스는 없음   

<br/>

👉🏻 [**WuMo 서비스 구경가기**](https://5yes-wumo.vercel.app/landing)  

👉🏻 [**API 명세서 보러가기**](https://wumo.site/swagger-ui/index.html)    

<br/><br/>

## 🌠 팀 소개      
    
<br/>
    
Leader|Developer|Developer|
---|---|---|
![taehee]|![sup]|![gyu]|
[김태희](https://github.com/ttaehee)|[김보섭](https://github.com/boompatron)|[김창규](https://github.com/Kim-Changgyu)|
회원(Member), 일정(Route)|후보지(Location), 댓글(Comment)|모임(Party), 모임멤버(PartyMember) , 초대(Invitation), 좋아요(Likes)|
-JWT 토큰 기반 인증 및 OAuth 인증 <br/> -커버링 인덱스를 통한 일정 검색 최적화 <br/> -캐싱으로 일정 조회 성능 개선 <br/> -메일 전송 이벤트 및 비동기 처리| -인덱스를 통한 조회 성능 개선 <br/> -JPA 상속을 통한 테이블 최적화 | -Base62 기반 초대코드 생성 <br/> -분산 락을 활용한 동시성 제어 <br/> -좋아요 집계 스케줄링 |

[taehee]: https://avatars.githubusercontent.com/u/103614357?v=4
[sup]: https://avatars.githubusercontent.com/u/39071638?v=4
[gyu]: https://avatars.githubusercontent.com/u/65993842?v=4

- 🎨 [**참고) FrontEnd Repository**](https://github.com/prgrms-web-devcourse/Team-5YES-WuMo-FE)

<br/><br/>

## 🛠 기술 스택

<h3 align="center"> 개발 / 테스트 </h3>

<p align="center">
<img src="https://img.shields.io/badge/Java 17-008FC7?style=flat-square&logo=Java&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"/></img>
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000.svg?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/></img>
</p>

<p align="center">
<img src="https://img.shields.io/badge/Spring-58CC02?style=flat-square&logo=Spring&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Boot 2.7.8-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Data JPA-ECD53F?style=flat-square&logo=JPA&logoColor=white"/></img>
</p>

<p align="center">
<img src="https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white"/></img>
<img src="https://img.shields.io/badge/TestContainers-01334B?style=flat-square&logo=TestContainers&logoColor=white"/></img>
</p>

<h3 align="center"> DB </h3>

<p align="center">
<img src="https://img.shields.io/badge/MySQL 8.0-4479A1?style=flat-square&logo=MySQL&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Flyway-CC0200?style=flat-square&logo=Flyway&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/>
</p>

<h3 align="center"> Infra </h3>

<p align="center">

<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat-square&logo=GitHub Actions&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=Amazon EC2&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat-square&logo=Amazon RDS&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=Amazon S3&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon CodeDeploy-00A98F?style=flat-square&logo=Amazon CodeDeploy&logoColor=white"/>

</p>

<h3 align="center"> 문서 / 협업 </h3>

<p align="center">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white"/>
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/>

<img src="https://img.shields.io/badge/Jira Software-0052CC?style=flat-square&logo=Jira Software&logoColor=white"/>
<img src="https://img.shields.io/badge/Git-F05032.svg?style=flat-square&logo=Git&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717.svg?style=flat-square&logo=GitHub&logoColor=white"/>
<img src="https://img.shields.io/badge/Salck-4A154B?style=flat-square&logo=Slack&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37.svg?style=flat-square&logo=Postman&logoColor=white"/>

</p>

<br/><br/>

## 📝 프로젝트 중점사항

- 인증/인가를 위한 JWT
- Redis Cache를 이용한 조회 성능 개선
- Github action과 CodeDeploy, S3를 사용한 CI/CD 자동화
- Mysql에서 인덱스 설정 쿼리 튜닝
- 분산 락을 활용한 동시성 제어
- 백엔드와 프론트 협업 경험

<br/><br/>

## ⚙ Infra Structure

![Group 5](https://user-images.githubusercontent.com/103614357/225020982-8e4787c1-f900-463c-8b33-419889f74cc5.png)

<br/><br/>

## 🎫 ERD

![WuMo erd](https://user-images.githubusercontent.com/103614357/225019257-7f59f842-2300-492e-b9e9-f96feffefd04.png)   

🔗[ERD 보러가기](https://www.erdcloud.com/d/c3RbJe8yM3hncaMRh)

<br/><br/>

## 🌱 협업툴 Jira   

- Github과 Jira 연동 후 이슈 자동화 관리   

![5yes_wumo_2023-03-14_06 50pm](https://user-images.githubusercontent.com/103614357/225067628-2956e206-10bf-43d8-b50a-24926036a4f0.png)   

<br/><br/>

## 🍃 Branch 전략
- GitHub Flow 변형    
 프로젝트의 크기와 팀에 맞추어 변형하여 사용

![image](https://user-images.githubusercontent.com/103614357/225021551-2777303b-15cc-4a07-8baf-1d869d0691cd.png)

- `Main` : 배포 브랜치
- `Develop` : 개발 브랜치
- `Feature` : 작업 브랜치 (feature/[WUMO-이슈번호])
- `Fix` : 버그 관련 작업 브랜치 (fix/[WUMO-이슈번호])

<br/><br/>

## 🌿 Commit Convention

| keyword | description |
| --- | --- |
| Chore | 빌드 업무 수정, 패키지 매니저 수정 |
| Feat | 새로운 기능 추가 |
| Fix | 버그 수정 |
| Docs | 문서 수정 |
| Style | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| Refactor | 코드 리펙토링 |
| Test | 테스트 코드, 리펙토링 테스트 코드 추가 |

<br/><br/>

## 🧾 프로젝트 관련 문서

- [노션](https://www.notion.so/backend-devcourse/05-5YES-3f17f0d96f1e43deb4b262aa3b0fb459)

- [최종 회고](https://www.notion.so/backend-devcourse/87a3d29f1f9e4023baceafa06382dd9a)

- [학습한 내용 공유](https://www.notion.so/backend-devcourse/e954ab69d6be481c8fd1e87a0a21bfdc)

- [트러블 슈팅 공유](https://www.notion.so/backend-devcourse/e9edda393f1c4a8e813ff461a0191f36)

<br/>

## 🌳 서비스 화면

![wumo1](https://user-images.githubusercontent.com/103614357/225654663-f283ce1a-6a35-452b-9d84-4ac5b872937f.png)

![wumo2](https://user-images.githubusercontent.com/103614357/225654675-95cbe106-08d3-4d7b-8976-4ed694eb95d1.png)

 <details>
 <summary><h4>이메일 회원가입 및 로그인</h4></summary>
 <img width="40%" src="https://user-images.githubusercontent.com/63575891/225982229-65db4553-f08c-4f6a-a636-250e34e1a91d.gif" alt="이메일 회원가입 및 로그인" />
 </details>
 <details>
 <summary><h4>모임 추가 및 관리</h4></summary>
 <img width="40%" src="https://user-images.githubusercontent.com/63575891/225985663-f4c0ae2e-cc8a-4662-ae6d-faa5226e9ce4.gif" alt="모임 추가 및 관리"/>
 </details>
 <details>
 <summary><h4>초대 및 후보지 추가</h4></summary>
 <img width="40%" src="https://user-images.githubusercontent.com/63575891/225984576-2644357e-858f-4cc7-82b6-77add4b6bc28.gif" alt="초대 및 후보지 추가" />
 </details>
 <details>
 <summary><h4>일정 관리 및 피드</h4></summary>
 <img width="40%" src="https://user-images.githubusercontent.com/63575891/225986588-e84deef4-c99b-4aab-9164-6161698f0298.gif" alt="일정 관리 및 피드">
 </details>
 <details>
 <summary><h4>베스트 루트 조회 및 관심 목록</h4></summary>
 <img width="40%" src="https://user-images.githubusercontent.com/63575891/225985170-7012e7ce-790c-4d20-bfdf-b0bf5ed7edd2.gif" alt="베스트 루트 조회 및 관심 목록"/>
 </details>

<br/>   
