# 🍄 WuMo (우리들의 모임)  
> 5YES's Team Project WuMo BackEnd

 
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

그룹 채팅방에서는 여행과는 상관없는 이야기가 오갈 수 있어서, 여행에 필요한 장소, 사진 등은 잊히기 일수입니다    

그렇다면 가족, 지인들과 함께 모임, 여행을 계획하고 완성해가며 추억을 기록할 도구가 있다면 어떨까요?!  

<br/>

## 목차

- [🌼 주요 기능](#-주요-기능) 

- [🤹 팀 소개](#-팀-소개)    

- [🛠 기술 스택](#-기술-스택) 

- [📝 관련 이슈](#-관련-이슈)    
   
- [⚙️ Infra Structure](#-infra-structure)    
 
- [🎫 ERD](#-erd)   

- [🌱 협업툴 Jira](#-협업툴-jira)

- [🍃 Branch 전략](#-branch-전략)   


- [🌿 Commit Convention](#-commit-convention)   

- [🧾 프로젝트 관련 문서](#-프로젝트-관련-문서)    

- [🌳 화면](#-화면)

<br/>

## 🌼 주요 기능

- 이메일 이용 간편 회원가입
- 가족, 지인끼리 모임 생성 및 초대
- 가고 싶은 장소를 후보지로 등록
- 모아둔 후보지들 중 마음에 드는 곳들로 일정 생성
- 사진 등록으로 추억 저장
- 마음에 들었던 일정 공개 가능
- 공개 된 일정 참고 후 마음에 든다면 좋아요

<br/>

👉🏻 [**WuMo 서비스 구경가기**](https://5yes-wumo.vercel.app/landing)  

👉🏻 [**API 명세서 보러가기**](https://wumo.site/swagger-ui/index.html)    

<br/><br/>

## 🤹🏻‍♀️ 팀 소개      
    
<br/>
    
Leader|Developer|Developer|
---|---|---|
![taehee]|![sup]|![gyu]|
[김태희](https://github.com/ttaehee)|[김보섭](https://github.com/boompatron)|[김창규](https://github.com/Kim-Changgyu)|
회원(Member), 일정(Route)|후보지(Location), 댓글(Comment)|모임(Party), 모임멤버(PartyMember) , 초대(Invitation)|
-JWT 토큰 기반 인증 및 OAuth 인증 <br/> -커버링 인덱스를 통한 검색 기능 최적화 <br/> -캐싱으로 목록 조회 성능 개선 <br/> -메일 전송 이벤트 및 비동기 처리| -인덱스를 통한 조회 성능 개선 <br/> -JPA 상속을 통한 테이블 최적화 | -Base62 기반 초대코드 생성 <br/> -분산 락을 활용한 동시성 제어 <br/> -좋아요 집계 스케줄링 |

[taehee]: https://avatars.githubusercontent.com/u/103614357?v=4
[sup]: https://avatars.githubusercontent.com/u/39071638?v=4
[gyu]: https://avatars.githubusercontent.com/u/65993842?v=4

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

## 📝 관련 이슈

(추가 예정)

<br/><br/>

## ⚙️ Infra Structure

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
- `Develop` : 개발 안정화 브랜치
- `Feature` : 작업 시 하나의 브랜치 생성(feature/[WUMO-이슈번호])
- `Fix` : 버그 관련 작업 브랜치

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

- [회고] (추가 예정)

- [학습한 내용 공유](https://www.notion.so/backend-devcourse/e954ab69d6be481c8fd1e87a0a21bfdc)

- [트러블 슈팅 공유](https://www.notion.so/backend-devcourse/e9edda393f1c4a8e813ff461a0191f36)

<br/>

## 🌳 화면

(추가 예정)

![wumo_signup](https://user-images.githubusercontent.com/103614357/225016401-6452e696-16fa-4f69-8f08-e82a263f3c16.png)

<br/> 

- 🎨 [**참고) FrontEnd Repository**](https://github.com/prgrms-web-devcourse/Team-5YES-WuMo-FE)

<br/>   
