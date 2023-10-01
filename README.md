# PlantBackend
식구하자(백엔드)

## 프로젝트 소개
✅ 당근 마켓을 벤치마킹하여 식집사들을 위한 식물, 식물 용품 중고 거래 및 식물 정보 공유 사이트✅
- 지금 식구하자에서 거래되고 있는 다양한 식물, 식물 용품을 구경해보세요.
- 식물에 대한 정확한 정보를 얻어가세요! 💬
  <br/>
  <u> 현재 서비스는 aws 무료기간이 지나 종료하였습니다</u>

 👉 식구하자 노션 [바로가기](https://www.notion.so/513e3f3e40cf4b1c989de585de632618)<br/>
 👉 식구하자 프론트 [바로가기](https://github.com/Hanttogang/Plant_Frontend)
## 🔭 목차 | Contents
1️⃣ [개발 기간 및 팀원](#-개발-기간--project-period) <br/>
2️⃣ [아키텍처](#-아키텍처--architecture) <br/>
3️⃣ [주요 기능](#-주요-기능--main-function) <br/>
4️⃣ [내 역할](#-내-역할--position) <br/>
5️⃣ [기술 스택](#-기술-스택--technology-stack) <br/>
6️⃣ [ERD](#-ERD--erd) <br/>
7️⃣ [기술적 의사결정](#-기술적-의사결정--technical-decision-making) <br/>
8️⃣ [트러블 슈팅](#-트러블-슈팅--trouble-shooting) <br/>
9️⃣ [성능 튜닝](#-성능-튜닝--performance-tuning) <br/>
🔟 [시연 GIF](#-시연-gif--testing) <br/>


## 👬 개발 기간 및 팀원 | Project Period

개발 기간 : 23.06~23.08 (23년 2월에 시작했다가 팀원 이탈로 6월에 다시 진행)
### 🙂 팀원 | Member
총 3명
<br>
BE+DEVOPS : [이민우](https://github.com/LminWoo99/)
<br>
BE+FE : [장진호](https://github.com/jinho0114)
<br>
FE: [한세현](https://github.com/Hanttogang)

## 🛠 아키텍처 | Architecture
<img width="929" alt="스크린샷 2023-09-14 오후 6 03 35" src="https://github.com/LminWoo99/PlantBackend/blob/master/img/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202023-10-01%20%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB%2012.19.32.png">


## 🌱 주요 기능 | Main Function

✔️ 식구 도감
  - 100여종의 식물 검색
  - 여라가지 필터 (초보자, 잎, 열매, 꽃, 다육/선인장)
  - 키우는 방법 가이드
  - 농사로 api 를 통한 정확한 정보 전달

✔️ 중고거래 커뮤니티
  -  식물, 식물용품 거래 게시글 올리기
      - 식물 작성 폼 : 거래 제목, 거래 정보, 거래 이미지(Amazon S3를 통해 이미지 저장)
      - CRUD
  -  거래 게시글 페이징
  -  거래 게시글 조회수, 찜
  -  거래 의사는 댓글, 대댓글로 표현(개인정보를 위해 비밀댓글 선택가능!)
  -  댓글, 대댓글 구매자한해 구매자 결정

✔️ 유저 기능
  - 로그인, 소셜로그인 기능(카카오), 로그아웃  
  - 회원가입
      - 회원 가입시 네이버 smtp 이메일 본인 인증 
  - 아이디, 비밀번호 찾기
  - 내 찜 목록
  - 구매 내역, 판매 내역

## 👨‍🏫 내 역할 | Position

### Backend

✔️ 중고거래 커뮤니티
  -  식물, 식물용품 거래 게시글 올리기
      - 식물 작성 폼 : 거래 제목, 거래 정보, 거래 이미지(Amazon S3를 통해 이미지 저장)
      - CRUD
  -  거래 게시글 페이징
  -  거래 게시글 조회수, 찜
  -  거래 의사는 댓글, 대댓글로 표현(개인정보를 위해 비밀댓글 선택가능!)
  -  댓글, 대댓글 구매자한해 구매자 결정

✔️ 유저 기능(스프링 시큐리티 & JWT)
  - 로그인, 소셜로그인 기능(카카오), 로그아웃  
  - 회원가입
      - 회원 가입시 네이버 smtp 이메일 본인 인증 
  - 아이디, 비밀번호 찾기
  - 내 찜 목록
  - 구매 내역, 판매 내역
    
### Devops

 ✔️ AWS EC2를 통한 배포 
 <br/>
 ✔️ nginx를 이용한 무중단 배포 👉[블로그 참고](https://velog.io/@mw310/NginxSpring-Boot-%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC-%EA%B5%AC%EC%B6%95%ED%95%98%EA%B8%B0)
   - 8081,8082 두개로 배포시에도 서비스가 이용가능하게 구현<br/>
   
 ✔️ Github Actions을 통한 CI/CD 파이프 라인 구축 👉[블로그 참고](https://velog.io/@mw310/Spring-Boot-GitHub-Actions-AWS-CodeDeploy%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-CICD-%EA%B5%AC%EC%B6%95)
  

## ⚙ 기술 스택 | Technology Stack
### Front-End
<div>
 <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
 <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">
 <img src="https://img.shields.io/badge/React Router-CA4245?style=for-the-badge&logo=React Router&logoColor=white">
 <img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white">
 <img src="https://img.shields.io/badge/styledComponents-DB7093?style=for-the-badge&logo=styledComponents&logoColor=white">
</div>

### Back-End
<div>
  <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=Java&logoColor=white">
  <img src="https://img.shields.io/badge/Jpa-007396?style=for-the-badge&logo=Jpa&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
  <img src="https://img.shields.io/badge/QueryDSL-000000?style=for-the-badge&logo=QueryDSL&logoColor=white">
  <img src="https://img.shields.io/badge/Spring security-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Spring Boot&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon CodeDeploy-4454D6?style=for-the-badge&logo=Amazon CodeDeploy&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
  <img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white">
  <img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">
</div>

### 개발 환경 | Environment
<div>
  <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white">
  <img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">
  <img src="https://img.shields.io/badge/KakaoTalk-FFCD00?style=for-the-badge&logo=KakaoTalk&logoColor=white">
</div>

## 💻 ERD | ERD
<img width="590" alt="스크린샷 2023-09-14 오후 11 38 47" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/88224c26-3997-48a1-9456-6938eb3f1918">

## 📑 기술적 의사결정 | Technical Decision-Making
<table>
  <tbody>
      <tr>
      <td>Jpa</td>
      <td> 객체 중심으로 애플리케이션 개발하기 위해 Jpa를 사용하였고 QueryDsl은 직접적인 연관관계를 맺지 않는 Entity의 Join이 어려워 적절히 jpql도 적재적소에 맞게 사용하였습니다. </td>
    </tr>
    <tr>
      <td>Querydsl</td>
      <td>대댓글 구현을 위한 동적 쿼리 작성을 고려하여 Querydsl을 사용하였습니다.</td>
    </tr>
    <tr>
      <td>github action</td>
      <td>효율적인 협업을 위해서 github에서 지원하는 github action을 사용하여 자동 배포를 진행하였다.</td>
    </tr>
    <tr>
      <td>nginx</td>
      <td>서버 확장에 용이하고 리버스 프록시를 통해 보안적으로 뛰어나며 무중단 배포를 통한 사용자의 편의성 또한 제공하고 이용자가 많아졌을 경우의 확장성을 생각하여 사용하였습니다.</td>
    </tr>
      <tr>
      <td>Amazon S3</td>
      <td>중고 거래 상품 이미지 등록과 CI/CD를 위해 Amazon S3를 사용하였습니다.</td>
    </tr>
  </tbody>
</table>


## 🛠 트러블 슈팅 | Trouble Shooting

👉 트러블 슈팅 자세히 보기 [바로가기](https://www.notion.so/8c1e232ab7484eaeaf87614eeda15eab)<br/>

-------

## 🛠 성능 튜닝 및 개선 | Performance Tuning

### 해당 거래 게시글 댓글 대댓글 조회 n+1문제 해결을 통한 성능 튜닝
https://github.com/LminWoo99/PlantBackend/blob/85f9d0576b595d587bd1017ca7d7f48094682e5a/src/main/java/Plant/PlantProject/repository/CommentRepositoryImpl.java#L13-L26

FetchType.LAZY로 설정되어 있어서 부모 댓글을 실제로 사용할 때만 로딩하도록 한다면, 각 Comment 엔티티를 가져올 때마다 부모 댓글을 가져오기 위해 별도의 쿼리가 실행되어 N+1 문제가 발생. 그러나 Querydsl fetchJoin()을 사용하면 필요한 모든 데이터를 한 번에 가져오므로 이러한 문제를 해결. 따라서 leftJoin(comment.parent).fetchJoin()를 통해 N+1 문제를 효과적으로 해결함으로서 성능 향상 기대

### 벌크 연산을 통한 성능 튜닝
https://github.com/LminWoo99/PlantBackend/blob/81bd0baeb55021b36d4b0f80335dad556bd0c153/src/main/java/Plant/PlantProject/repository/TradeBoardRepository.java#L19-L36

벌크 연산을 통해 여러 엔터티를 한 번에 수정하거나 삭제함으로써 데이터베이스 연산 횟수를 감소시켜 네트워크 지연 및 데이터베이스 부하가 감소함에 따라 성능 향상

### JWT Access Token의 짧은 수명으로 인한 유저의 불편한 경험개선

프로젝트 마무리후 회고 단계에서 , Access Token의 짧은 시간이 불편하다는 피드백을 받아 성능 개선을 시켰습니다.
일단 기본적으로 Access Token의 경우 localStorage등 클라이언트(브라우저)의 저장공간에서 보관합니다.  
이에 따라서 수명이 길면 탈취당했을때 문제가 발생할 수 있습니다.  
JWT Refresh Token을 활용하여 유저의 사용경험이 불편하지 않도록 개선했습니다.

- 토큰의 유효시간의 경우 Access Token은 20분, Refresh Token은 14일로 지정
- Refresh Token도 localStorage에 보관하게 되면 탈취에 문제가 있으므로,  쿠키와 DB에 저장
- 서버에서 401 Unauthorized 에러 반환시 Refresh Token을 활용한 재요청 및 Access Token 재발급

```React
cookies.set('bbs_refresh_token', resp.data.refresh_token, { path: '/' });
```
- 로그인시 Refresh Token 쿠키에 저장
  
https://github.com/LminWoo99/PlantBackend/blob/b39fd9210307fa151da6aaaa8d7410046c55a37a/src/main/java/Plant/PlantProject/controller/MemberController.java#L93-L137
- AccessToken 만료가 되면 클라이언트에서 RefreshToken을 이용하여 AccessToken 재발급 요청
- 서버에서 DB에 저장된 RefreshToken과 요청온 RefreshToken을 비교하여 일치하면 새로운 AccessToken 발급
  

## 🎥 시연 GIF | Testing

|자체 회원가입(네이버 smtp를 이용한 이메일 본인인증)|소셜 로그인 및 자체 로그인|
|:---:|:---:|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/af0490a177b15c456b69a164e1a306dd3f9af6be/img/%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%E1%84%80%E1%85%A1%E1%84%8B%E1%85%B5%E1%86%B8.gif" width="400px" height="220px">|<img src="https://github.com/LminWoo99/PlantBackend/blob/decbf41b154a561f00070e8802c96358a7437e88/img/%E1%84%92%E1%85%AA%E1%84%86%E1%85%A7%E1%86%AB-%E1%84%80%E1%85%B5%E1%84%85%E1%85%A9%E1%86%A8-2023-09-16-%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE-12.56.39.gif" width="400px" height="220px">|
|식물 검색 하기 및 정보 보기|거래 글 상세 보기 및 댓글 대댓글작성(비밀댓글)|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/ezgif.com-video-to-gif%20(1).gif" width="400px" height="220px">|<img src="https://github.com/LminWoo99/PlantBackend/blob/af0490a177b15c456b69a164e1a306dd3f9af6be/img/%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%87%E1%85%A9%E1%84%80%E1%85%B5.gif" width="400px" height="220px">|
|거래 완료(댓글 단 유저중에서 거래자 정하고 거래완료가 되면 화면 상태 바뀜)|거래 글 찜 하기및 찜 내역|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/af0490a177b15c456b69a164e1a306dd3f9af6be/img/%E1%84%80%E1%85%A5%E1%84%85%E1%85%A2%E1%84%8B%E1%85%AA%E1%86%AB%E1%84%85%E1%85%AD.gif" width="400px" height="220px">|<img src="https://github.com/LminWoo99/PlantBackend/blob/af0490a177b15c456b69a164e1a306dd3f9af6be/img/%E1%84%8D%E1%85%B5%E1%86%B7%E1%84%82%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%A8.gif" width="400px" height="220px">|
|식물 판매 내역 및 구매 내역|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/af0490a177b15c456b69a164e1a306dd3f9af6be/img/%E1%84%91%E1%85%A1%E1%86%AB%E1%84%86%E1%85%A2%E1%84%82%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%A8%2C%20%E1%84%80%E1%85%AE%E1%84%86%E1%85%A2%E1%84%82%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%A8.gif" width="400px" height="220px">
<!--

