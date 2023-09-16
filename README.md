# PlantBackend
식구하자(백엔드 커밋)
## 프로젝트 소개
✅ 당근 마켓을 벤치마킹하여 식집사들을 위한 식물, 식물 용품 중고 거래 및 식물 정보 공유 사이트✅
- 지금 식구하자에서 거래되고 있는 다양한 식물, 식물 용품을 구경해보세요.
- 식물에 대한 정확한 정보를 얻어가세요! 💬

 👉 식구하자 사이트 [바로가기]() <br/>
 👉 식구하자 노션 [바로가기](https://www.notion.so/513e3f3e40cf4b1c989de585de632618)

## 🔭 목차 | Contents
1️⃣ [개발 기간](#-개발-기간--project-period) <br/>
2️⃣ [아키텍처](#-아키텍처--architecture) <br/>
3️⃣ [주요 기능](#-주요-기능--main-function) <br/>
4️⃣ [기술 스택](#-기술-스택--technology-stack) <br/>
5️⃣ [기술적 의사결정](#-기술적-의사결정--technical-decision-making) <br/>
6️⃣ [트러블 슈팅](#-트러블-슈팅--trouble-shooting) <br/>
7️⃣ [팀원](#-팀원--member)



## 🛠 아키텍처 | Architecture
<img width="929" alt="스크린샷 2023-09-14 오후 6 03 35" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/f9e3ff99-7c36-48fb-825b-36a763d76379">


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
<table>
  <tbody>
    <tr>
      <td>
        Front-end
      </td>
      <td>
        검색기능
      </td>
      <td>
        <img src="https://user-images.githubusercontent.com/117730606/217253442-b0f1c07a-5940-403f-bfa0-890868f08d14.png"/>
      </td>
    </tr>
    <tr>
      <td>
        Front-end
      </td>
      <td>
        React Custom Hook
      </td>
      <td>
        <img src="https://user-images.githubusercontent.com/117730606/217255358-b0962e13-3c01-4075-bc16-b27d72c83fad.png"/>
      </td>
    </tr>
        <tr>
      <td>
        Back-end 
      </td>
      <td>
        RefreshToken 탈취 위협
      </td>
      <td>
        <img src="https://user-images.githubusercontent.com/117730606/217255758-c60853d0-27a9-4531-bc92-8d2e7aab40c6.png"/>
      </td>
    </tr>
        <tr>
      <td>
        Back-end 
      </td>
      <td>
        이미지 로딩 속도 개선
      </td>
      <td>
        <img src="https://user-images.githubusercontent.com/117730606/217256001-c24b2f42-73d6-478e-b95e-baadcde4c8db.png"/>
      </td>
    </tr>
  </tbody>
</table>



## 🎥 시연 GIF | Testing

|자체 회원가입(네이버 smtp를 이용한 이메일 본인인증)|소셜 로그인 및 자체 로그인|
|:---:|:---:|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%E1%84%80%E1%85%A1%E1%84%8B%E1%85%B5%E1%86%B8.gif" width="400px" height="220px">|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/%E1%84%92%E1%85%AA%E1%84%86%E1%85%A7%E1%86%AB-%E1%84%80%E1%85%B5%E1%84%85%E1%85%A9%E1%86%A8-2023-09-16-%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE-12.56.39.gif" width="400px" height="220px">|
|식물 검색 하기 및 정보 보기|거래 글 상세 보기 및 댓글 대댓글작성(비밀댓글)|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/ezgif.com-video-to-gif%20(1).gif" width="400px" height="220px">|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%87%E1%85%A9%E1%84%80%E1%85%B5.gif" width="400px" height="220px">|
|거래 완료(댓글 단 유저중에서 거래자 정하고 거래완료가 되면 화면 상태 바뀜)|거래 글 찜 하기및 찜 내역|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/%E1%84%80%E1%85%A5%E1%84%85%E1%85%A2%E1%84%8B%E1%85%AA%E1%86%AB%E1%84%85%E1%85%AD.gif" width="400px" height="220px">|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/%E1%84%8D%E1%85%B5%E1%86%B7%E1%84%82%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%A8.gif" width="400px" height="220px">|
|식물 판매 내역 및 구매 내역|
|<img src="https://github.com/LminWoo99/PlantBackend/blob/master/img/%E1%84%91%E1%85%A1%E1%86%AB%E1%84%86%E1%85%A2%E1%84%82%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%A8%2C%20%E1%84%80%E1%85%AE%E1%84%86%E1%85%A2%E1%84%82%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%A8.gif" width="400px" height="220px">
<!--
## 🙂 팀원 | Member
총 3명
<br>
BE+DEVOPS : [이민우](https://github.com/LminWoo99/)
<br>
BE+FE : [장진호](https://github.com/jinho0114)
<br>
FE: [한세현](https://github.com/Hanttogang)