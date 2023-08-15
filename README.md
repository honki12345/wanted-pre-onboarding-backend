# wanted-pre-onboarding-backend

- 원티드 프리온보딩 백엔드 인턴십 - 선발 과제

---

# - 지원자의 성명

김훈기

# - 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)

##### 0. 실행환경 준비하기

- 애플리케이션 실행을 위해서는 `git`, 빌드도구 `gradle`, `java 17` 과 `docker`, `docker compose` 가 필요합니다
- 엔드포인트 호출을 위해서 `curl` 을 사용합니다

##### 1. git clone

- 다음의 명령어를 통해 리포지토리를 로컬에 복사해주세요 
    `git clone https://github.com/honki12345/wanted-pre-onboarding-backend.git`

##### 2. 빌드

- 프로젝트 루트 리포지토리에서 `./gradlew build` 를 수행합니다.

##### 2. 환경변수파일 `.env` 만들기

- 데이터베이스 비밀번호와 같은 민감한 정보는 환경변수 파일로 관리합니다.

- 클론한 프로젝트 루트 디렉터리에 `.env` 파일을 만들고 다음과 같은 정보를 설정해주세요
    ```
    MYSQL_ROOT_PASSWORD=
    MYSQL_DATABASE_NAME=
    MYSQL_USER_NAME=
    MYSQL_USER_PASSWORD=
    ```

##### 3. 도커컴포즈 실행 

- 생성/실행 `docker-compose up`

- 중지 `docker-compose stop`

##### 4. 엔드포인트 호출

- 회원가입
    ```
    curl -X POST http://{SERVER_URL}/members \
      -H "Content-Type: application/json" \
      -d '{ 
          	"email": "aaa@bbb.com", 
            "pwd": "password" 
          }'
    ```
    
- 로그인
    ```
    curl -X POST http://{SERVER_URL}/session \
      -H "Content-Type: application/json" \
      -d '{ 
          	"email": "aaa@bbb.com", 
            "pwd": "password" 
          }'
    ```
    
- 게시글 생성
    ```
    curl -X POST http://{SERVER_URL}/posts \
      -H "Authorization: Bearer {TOKEN}" \
      -H "Content-Type: application/json" \
      -d '{ 
          	"title": "제목", 
            "content": "내용" 
          }'
    ```
    
- 게시글 목록조회
    ```
    curl -X GET http://{SERVER_URL}/posts?page={PAGE_NUMBER}
    ```
    
- 게시글 상세조회
    ```
    curl -X GET http://{SERVER_URL}/posts/{id}
    ```
    
- 게시글 수정
    ```
    curl -X PATCH http://{SERVER_URL}/posts/{id} \
      -H "Authorization: Bearer {API_KEY}" \
      -H "Content-Type: application/json" \
      -d '{ 
          	"title": "제목", 
            "content": "내용" 
          }'
    ```
    
- 게시글 삭제
    ```
    curl -X DELETE http://{SERVER_URL}/posts/{id} \
      -H "Authorization: Bearer {API_KEY}"
    ```
    
    

# - 데이터베이스 테이블 구조

![erd](./document/erd.png)

# - 구현한 API 의 동작을 촬영한 데모 영상 링크

- [링크](https://youtu.be/MK7GFkF3o3w)

# - 구현 방법 및 이유에 대한 간략한 설명

- 패키지구조
  - 패키지 구조는 web 과 application 을 나누었습니다.
  - web 패키지가 프레젠테이션 계층을 맡고 application 패키지에서 비즈니스 계층과 데이터 계층을 나누어 구현하였습니다.
  - 레이어 간 역할을 명확하게 의존흐름을 정하고 테스트가 용이해지도록 하였습니다.
- API 디자인
  - REST API style 로 URI 는 resource 를 가리키고 resource 에 대한 행위는 HTTP method 로 표현하였습니다.
  - 예로 들면 로그인 같은 경우 session (혹은 token, jwt 도 가능하다) 이 `생성` 된다는 의미에서 URI 를 `POST /session` 으로 하였습니다. post method 가 생성의 의미에 맞게 연결되게 하였고 resource 를 명사로 표현할 수 있는 session 로 URI 를 정했습니다.
  - 결과적으로 원칙을 기반으로 URI 와 method 만으로도 내용과 의미를 읽기 쉬워지게 됩니다.

- 예외처리
  - 예외는 custome exception 으로 사용자에게 예외메세지를 불필요하게 보여지지 않도록 제어했습니다.
  - 예외를 ControllerAdvice 에서 처리하도록 하였고 에러메시지를 enum 클래스인 ErrorCode 로 사용하여 관리가 수월했습니다.
- 데이터접근기술
  - 데이터접근기술로는 JPA 를 사용하여 도메인 객체를 데이터베이스가 아닌 객체지향 중심적으로 설계할 수 있었습니다.
  -  페이지네이션이나 쿼리 생성 또한 JPA 를 이용하여 복잡한 로직 없이 구현할 수 있었습니다.

- 인증&인가
  - 인증와 인가의 검증에 대해서는 보안에 관해 체계적으로 많은 옵션을 제공해주는 Spring Security 를 사용하여 필터의 흐름에서 처리합니다.

- 테스트
  -  컨트롤러는 단위 테스트로 하였고 비즈니스 계층과 데이터 계층을 통합 테스트로 테스트하였습니다.
  - 테스트 덕분에 구조 변경과 같은 리팩토링에 부담이 덜했습니다.
  - 테스트 DB 환경은 H2 in-memory 로 설정하여 운영 DB 환경에 영향이 가지 않게 하였고 빠른 테스트가 가능하도록 하였습니다.

- 데이터베이스 설계
  - 데이터베이스 설계는 admin 권한과 user 권한 같은 권한의 다양성을 고려해서 권한 테이블을 만들고 회원과 권한 테이블의 다대다 관계로 연결해주었다.

# 클라우드 환경

![architecture](./document/architecture.png)

- 배포주소: `15.164.138.207:8080`

#### 배포된 API 주소
- 회원가입
    ```
    curl -X POST http://15.164.138.207:8080/members \
      -H "Content-Type: application/json" \
      -d '{ 
          	"email": "ccc@ddd.com", 
            "pwd": "password" 
          }'
    ```

- 로그인
    ```
    curl -X POST http://15.164.138.207:8080/session \
      -H "Content-Type: application/json" \
      -d '{ 
          	"email": "ccc@ddd.com", 
            "pwd": "password" 
          }'
    ```

- 게시글 생성
    ```
    curl -X POST http://15.164.138.207:8080/posts \
      -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYWFAYmJiLmNvbSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2OTI5MDE1MDR9.4KI9NoBM50WKILG7GJ_5o5sw4OO43KF5YnUb5-gcWvDic7Qs7VptLVl_dnv0JftHOgwimB91Z2U8A1hXE_jzHA" \
      -H "Content-Type: application/json" \
      -d '{ 
          	"title": "제목", 
            "content": "내용" 
          }'
    ```

- 게시글 목록조회
    ```
    curl -X GET http://15.164.138.207:8080/posts
    ```

- 게시글 상세조회
    ```
    curl -X GET http://15.164.138.207:8080/posts/2
    ```

- 게시글 수정
    ```
    curl -X PATCH http://15.164.138.207:8080/posts/2 \
      -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYWFAYmJiLmNvbSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2OTI5MDE1MDR9.4KI9NoBM50WKILG7GJ_5o5sw4OO43KF5YnUb5-gcWvDic7Qs7VptLVl_dnv0JftHOgwimB91Z2U8A1hXE_jzHA" \
      -H "Content-Type: application/json" \
      -d '{ 
          	"title": "수정제목", 
            "content": "수정내용" 
          }'
    ```

- 게시글 삭제
    ```
    curl -X DELETE http://15.164.138.207:8080/posts/2 \
      -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYWFAYmJiLmNvbSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2OTI5MDE1MDR9.4KI9NoBM50WKILG7GJ_5o5sw4OO43KF5YnUb5-gcWvDic7Qs7VptLVl_dnv0JftHOgwimB91Z2U8A1hXE_jzHA"
    ```
  
# - API 명세(request/response 포함)

#### - 사용자

- 회원가입 - `POST /members`

  - Request

    - body: {Object}

      | 이름  | 타입   | 필수 | 설명     |
      | ----- | ------ | ---- | -------- |
      | email | String | O    | 아이디   |
      | pwd   | String | O    | 비밀번호 |

  - Response
    - 상태코드: Created(201)

  - Exception

    - Bad_Request(400): 이메일이나 비밀번호의 값이 올바르지 않는 경우


    <hr/>

#### 로그인

- 로그인 - `POST /session`
  - Request

    - body: {Object}

      | 이름  | 타입   | 필수 | 설명     |
      | ----- | ------ | ---- | -------- |
      | email | String | O    | 아이디   |
      | pwd   | String | O    | 비밀번호 |

  - Response
    - status: Ok(200)
    - TODO: jwt 토큰값 반환

  - Exception

    - Bad_Request(400) 이메일이나 비밀번호의 값이 올바르지 않는 경우


    <hr/>

#### 게시글

- 게시글 생성 - `POST /posts`

  - Request

    - headers: {Authorization}

    - body: {Object}

      | 이름    | 타입   | 필수 | 설명        |
      | ------- | ------ | ---- | ----------- |
      | title   | String | O    | 게시글 제목 |
      | content | String | O    | 게시글 내용 |

  - Response

    - status: Created(201)

  - Exception

    - Unauthorized(401): 로그인 하지 않고 요청시
    - Bad_Request(400): 제목이나 내용의 값이 없는 경우

    <hr/>

- 게시글 목록조회 - `GET /posts`
  - Request
  - Response
    - status: Ok(200)
    - body: {List}

      | 이름    | 타입   | 필수 | 설명            |
      | ------- | ------ | ---- | --------------- |
      | id      | String | O    | 게시물 아이디값 |
      | title   | String | O    | 게시물 제목     |
      | author  | String | O    | 게시물 작성자   |
      | content | String | O    | 게시물 내용     |

  - Exception


  <hr/>

- 게시글 조회 - `GET /posts/{id}`

  - Request

  - Response

    - status: Ok(200)

    - body: {Object}

      | 이름    | 타입   | 필수 | 설명        |
      | ------- | ------ | ---- | ----------- |
      | title   | String | O    | 게시물 제목 |
      | content | String | O    | 게시물 내용 |

  - Exception

    - NotFound(404): 해당 게시글을 찾을 수 없습니다


    <hr/>

- 게시글 수정 - `PATCH /posts/{id}`

  - Request

    - headers: {Authorization}

    - body: {Object}

      | 이름    | 타입   | 필수 | 설명        |
      | ------- | ------ | ---- | ----------- |
      | title   | String | O    | 게시글 제목 |
      | content | String | O    | 게시물 내용 |

  - Response

    - status: Ok(200)

  - Exception

    - Unauthorized(401): 로그인 하지 않고 요청시
    - Forbidden(403): 권한이 없는 경우
    - NotFound(404): 해당 게시글을 찾을 수 없습니다


    <hr />

- 게시글 삭제 - `DELETE /posts/{id}`

  - Request
    - headers: {Authorization}
  - Response
    - status: NoContent(204)
  - Exception
    - NotFound(404): 해당 게시글을 찾을 수 없습니다


    <hr/>

