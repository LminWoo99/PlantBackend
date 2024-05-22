# 🍀 서비스 소개

식구하자 프로젝트는 반려식물 및 식물 용품을 중고 거래를 하고, 반려식물을 관리하기 위한 정보를 얻을수 있으며
내가 키우는 반려식물을 sns 형식으로 공유하는 플랫폼입니다.

# 📌프로젝트 결과물

**👉 [[식구하자 클릭]](https://www.notion.so/HTTPS-95fd3f7209ef4f43a9a8457eec112325?pvs=21)**

# 📜 개발 환경

- React
- Java
- SpringBoot, JPA, QueryDSL
- Spring cloud, gateway, eureka, config, feign client
- Kafka , rabbitmq, STOMP
- Prometheus & Grafana, circuitbreaker
- DB - Mysql, Redis, Mongo
- INFRA - docker, Jenkins, AWS EC2, S3, Route 53, CloudFront
- Collaboration - GitHub

# 🚨 시스템 아키텍쳐
<img width="1257" alt="시스템 아키텍쳐" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/a1752131-d883-43f3-8b6c-0e3769b2ed6a">


# 👨🏻‍💻 MSA 서비스 백엔드 아키텍쳐
<img width="984" alt="msa 설계도" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/2c80db0e-342a-4f1d-8bff-11ed6f3f4015">
<img width="1439" alt="스크린샷 2024-05-22 오후 3 06 24" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/da826aaa-37c1-476c-a93c-f74723fb6b6d">

# 마이크로 서비스 별 주요 기능 및 특징

## **Spring Cloud Eureka 서버 및 Gateway 구축**

- 서비스 디스커버리 및 API Gateway에서 인증,인가, 로드밸런싱 역할 수행

## **Spring Cloud Config 서버 구축**

- 서비스들의 설정 정보를 중앙 집중화하고 외부에서 동적으로 관리할 수 있도록 구성
- **Spring cloud bus**와 **rabbitMQ**를 통해 동적으로 **config 변경**을 적용하였습니다.

## CI/CD 파이프라인 구축

- Jenkins와 Docker를 활용하여 CI/CD 파이프라인 구축

## 유저, 식물 거래 게시판 및 식물 정보 제공 서비스 (plant-service)

### ✔️ 중고거래 커뮤니티

- 식물, 식물용품 중고 거래 게시글 CRUD
    - 중고 게시글 검색(제목, 내용, 키워드)
    - 중고 거래 게시글 조회수, 찜
    - 중고 거래 게시글 사진 업로드
        - S3 활용
    - 중고 거래 구매자 결정

### ✔️ 유저 기능(스프링 시큐리티 & JWT)

- 로그인, 소셜로그인 기능(카카오), 로그아웃
- 회원가입
    - 회원 가입시 네이버 smtp 이메일 본인 인증
- 아이디, 비밀번호 찾기
- 내 정보
    - 구매 내역, 판매 내역, 찜 내역, 페이 머니 조회, 쿠폰 내역, 키워드 설정정보
- JWT 토큰
    - Access Token 재발급을 위해 Refresh Token을 **Redis에** 저장

### ✔️ 식물 정보 제공 (API 사용)

- 농사로 API를 사용하여 100여가지 종의 식물 정보 제공
- 검색 기능에 **QueryDsl을** 활용한 **동적 쿼리** 적용
    - 식물 종류 별 검색
    - 식물 관리 난이도 별 검색

## 채팅 서비스 (plant-chat-service)

- 중고 거래시 판매자에게 **1:1 채팅**( **kafka+stomp를 활용**)
    - **채팅 읽음 여부 표시 기능 구현**
        - 채팅방에 접속한 회원의 데이터는 자주 변경이 되고 휘발되는 데이터이므로 **Redis**를 이용하여 관리하였습니다 .
        - 접속 정보를 **Redis를** 통한 관리로 채팅방에 모두 접속했을경우, **알림이** 가지 않도록 구현
- **SSE**를 이용한 실시간 알람이 발송(분리된 마이크로서비스에서 알림이 필요할 경우 **느슨한 결합**을 유지하기 위해 비동기식 **kafka 이벤트** 활용)
    - 내가 올린 SNS게시글에 댓글,좋아요가 달리거나
    - 키워드 등록을 통해 유저가 원하는 키워드이 거래 게시글이 올라왔을 경우
    - 내가 올림 거래 게시글에 상대방이 찜할 경우
    - 채팅방에 새로운 채팅이 있을 경우 SSE를 이용한 실시간 알람이 발송
- 읽기가 잦은 채팅 내역 데이터는 RDB 대신 **MongoDB**를 선택하여 사용했습니다.
- 거래 게시글이 삭제되면 해당 게시글에 속한 채팅방 및 채팅 내역들의 **연쇄적인 삭제**가 필요하다 생각이 들었습니다. 하지만 거래 게시글은 게시글 마이크로서비스이고 채팅 관련 데이터는 채팅 마이크로 서비스의 **단일 DB**에 저장이 되있으므로 **kafka**를 사용하여 이벤트 발생시 **데이터 동기화를** 진행하게 구현

## 결제 서비스(plant-pay-service)

*실제로 결제가 되지 않고, 테스트 환경입니다*

- 중고 식물 거래 시 , 페이 머니 충전을 통한 결제 기능
    - 실제 계좌 이체 연동은 대신, **iamport api** 사용한 결제 기능 구현
- 나의 **페이 머니 조회**
- 페이 머니 계좌 **환불** 기능(*실제로 계좌에 환불되지 않습니다*)

## 선착순 쿠폰 발급 서비스(plant-coupon-service)

- 중고 식물 거래시 사용 가능한 **3,000원** 할인 쿠폰을 매일 **13시에** 선착순 **100명에게** 제공하는 기능을 구현
- 매일 정오 시각에 사용 완료및 유효기간이 지난 **쿠폰 데이터**를 **scheduler**를 통해 삭제 할 수 있도록 기능을 구현
    - **유저 별 하루 1개의 쿠폰 발급**을 위해 중복 방지를 **Redis의 Set 자료구조**를 활용하였고 다수의 사용자가 쿠폰 발급을 요청하더라도 정확히 100개의 쿠폰 발급을 위해 **redis 싱글스레드 특성**을 이용하여 **원자적 연산**을 통한 **동시성 제어**
    - 쿠폰 발급시 RDB의 저하 이슈를 **Kafka 비동기 메시지 큐 시스템**으로 최적화
- **결제 시스템과의 통합**
    - 쿠폰 서비스는 결제 마이크로서비스와 통합되어, 사용자가 식물 중고 거래 시 쿠폰을 적용할 수 있도록 구현되었습니다. 이 과정에서, 사용자는 할인된 가격으로 거래를 진행할 수 있으며, 판매자는 할인 전 가격을 받습니다.
    - **분산 트랜잭션의** 제어를 위해 **SAGA Choreography** 패턴을 적용하였습니다.
- 유저별 쿠폰 조회

## sns 서비스 (plant-sns-service)

- 자신이 키우는 반려식물 **sns** 형식의 공유 서비스
- sns 게시글 **crud** 구현
- sns 게시글 **댓글, 대댓글** 기능 구현
- sns 게시글 **좋아요** 및 **조회수** 기능
    - 게시글 좋아요 기능에서 **Redisson** 분산락과 **Facade 패턴**을 사용하여 동시성 문제 해결 패턴을 사용하여 동시성 문제 해결
- 게시글에 이미지 업로드 기능(S3)
- **이주의 게시글, 이달의 게시글 조회**
- **인기 해시 태그 조회**
- **게시글 검색기능**
    - **QueryDSL**을 활용한 **동적 쿼리** 작성
        - 태그(완전일치)
        - 제목(포함)
        - 내용(포함)
        - 닉네임(완전일치

## **Prometheus & Grafana를 통한 마이크로 서비스 모니터링**

- MSA로 전환하는 만큼 각각의 마이크로 서비스의 상태를 모니터링 하기 위해 Prometheus & Grafana **사용하였습니다**
  
- |||
  |:---:|:---:|
  |<img width="1440" alt="총 처리, 성공 api" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/4842f88b-38a5-4c6a-b375-753e89151fd7">|<img width="1439" alt="cpu,memory, api status" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/2d8205af-3bb9-41d6-831d-4bf269ab026e">|
  |<img width="1438" alt="jvm 마이크로 서비스 등록" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/851e3560-6602-4e91-bf4c-173780229cb9">|<img width="1440" alt="jvm heap" src="https://github.com/LminWoo99/PlantBackend/assets/86508110/038bc629-5766-47a6-b77d-8676f01dc634">|




# 🔫 트러블 슈팅 && 리팩터링

- [**Jenkins CI/CD 적용**](https://velog.io/@mw310/%EC%8B%9D%EA%B5%AC%ED%95%98%EC%9E%90MSA)
- [**1:1 채팅 시 읽지 않은 채팅 관련 문제 해결**](https://velog.io/@mw310/%EC%8B%9D%EA%B5%AC%ED%95%98%EC%9E%90MSA-Stomp-Kafka%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%B1%84%ED%8C%85-%EB%B0%8F-SSE%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%95%8C%EB%A6%BC-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-3SseEmitterRedis)
- [**JPA open-in-view SSE Connection 고갈 문제 && 마이크로 서비스간 단일 DB 데이터 동기화 문제 해결**](https://velog.io/@mw310/%EC%8B%9D%EA%B5%AC%ED%95%98%EC%9E%90MSA-%EC%B1%84%ED%8C%85-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C-%EC%84%9C%EB%B9%84%EC%8A%A4-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85-%EC%A0%95%EB%A6%AC)
- [**선착순 쿠폰 발급시 동시성 제어 및 DB 성능 문제 해결**](https://velog.io/@mw310/%EC%8B%9D%EA%B5%AC%ED%95%98%EC%9E%90MSA-%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%BF%A0%ED%8F%B0-%EC%84%9C%EB%B9%84%EC%8A%A4-%EA%B5%AC%ED%98%84%ED%95%B4%EB%B3%B4%EC%9E%90)
- [**Redisson및 퍼사드 패턴을 활용한 SNS 서비스 좋아요 동시성 문제 해결**](https://velog.io/@mw310/%EC%8B%9D%EA%B5%AC%ED%95%98%EC%9E%90MSA-sns-%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%EC%84%9C-%EC%A2%8B%EC%95%84%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)
- [**마이크로 서비스간 분산 트랜잭션 제어 문제 해결 By Saga Pattern**](https://velog.io/@mw310/Saga%ED%8C%A8%ED%84%B4%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%B6%84%EC%82%B0-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%A0%9C%EC%96%B4%EC%BF%A0%ED%8F%B0-%EC%A0%81%EC%9A%A9%EA%B2%B0%EC%A0%9C-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4)
- [**로그인 기능 리팩터링-Redis를 이용한Refresh Token 재발급**](https://velog.io/@mw310/%EC%8B%9D%EA%B5%AC%ED%95%98%EC%9E%90msa-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B8%B0%EB%8A%A5-%EB%A6%AC%ED%8C%A9%ED%84%B0%EB%A7%81-Refresh-Token-%EC%9E%AC%EB%B0%9C%EA%B8%89)

# 👨‍⚖️ 기술적 의사 결정

|  |  |
| --- | --- |
| Kafka vs Feign client | 데이터 조회용으로는 Feign Client 를 사용하여 마이크로 서비스 간의 통신을 간편하게 하였습니다. Feign Client는 선언적인 방식으로 HTTP 요청을 보낼 수 있어 코드의 가독성과 유지보수성이 뛰어납니다. 데이터의 수정, 삭제, 삽입 작업에서는 Kafka를 활용한 이벤트 기반의 비동기 처리를 통해 데이터 정합성을 유지하였습니다. Kafka는 높은 처리량과 내구성을 제공하여 대규모 데이터 처리에 적합합니다. |
| Websocket vs Stomp | Stomp로 채팅을 구현하면, 사용자가 채팅방에 접속했을 때, 채팅방을 구독하기만 하면 누가 접속중인지 따로 관리하지 않더라도 메세지를 보낸 사용자가 포함된 채팅방의 모든 사람에게 메세지가 갈 수 있다는 장점이 있기 때문에 사용하였습니다 |
| Mongo vs Mysql | 데이터베이스는 MongoDB와 MySQL을 함께 사용하였습니다. MongoDB는 채팅 데이터에만 사용하였는데, 채팅 데이터는 수정될 필요가 없고 조회와 삽입 작업만 이루어지므로, NoSQL의 장점인 빠른 읽기 성능을 활용할 수 있었습니다. 나머지 데이터는 관계형 데이터베이스인 MySQL을 사용하여 데이터 무결성과 복잡한 쿼리 작업에 적합하도록 하였습니다. |
| github action vs jenkins | 이번 프로젝트는 여러 서버를 구성하고 관리해야 하는 MSA 프로젝트였기 때문에, Jenkins의 유연성과 확장성을 고려하여 CI/CD 도구로 선택하였습니다. 대규모 시스템의 경우 Github Actions보다 Jenkins가 빠른 실행되므로 jenkins로 선택하였습니다 |

# 📉 API 문서

**👉 [[Swagger API 문서 보러가기]](https://43.202.205.242:8443/webjars/swagger-ui/index.html)**


