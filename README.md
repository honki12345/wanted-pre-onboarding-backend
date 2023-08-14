# wanted-pre-onboarding-backend

- 원티드 프리온보딩 백엔드 인턴십 - 선발 과제

---

# - 지원자의 성명

김훈기

# - 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)

##### 0. 실행환경 준비하기

- 애플리케이션 실행을 위해서는 `git`, 빌드도구 `gradle` 과 `docker`, `docker compose` 가 필요합니다
- 엔드포인트 호출을 위해서 `curl` 을 사용합니다

##### 1. git clone

- 다음의 명령어를 통해 리포지토리를 로컬에 복사해주세요 
    `git clone https://github.com/honki12345/wanted-pre-onboarding-backend.git`

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
    curl -X POST https://{SERVER_URL}/members \
      -H "Content-Type: application/json" \
      -d '{ \
          	"email": "email", \
            "pwd": "password" \
          }'
    ```
    
- 로그인
    ```
    curl -X POST https://{SERVER_URL}/session \
      -H "Content-Type: application/json" \
      -d '{ \
          	"email": "email", \
            "pwd": "password" \
          }'
    ```
    
- 게시글 생성
    ```
    curl -X POST https://{SERVER_URL}/posts \
      -H "Authorization: Bearer {TOKEN}" \
      -H "Content-Type: application/json" \
      -d '{ \
          	"title": "제목", \
            "content": "내용" \
          }'
    ```
    
- 게시글 목록조회
    ```
    curl -X GET https://{SERVER_URL}/posts?page={PAGE_NUMBER}
    ```
    
- 게시글 상세조회
    ```
    curl -X GET https://{SERVER_URL}/posts/{id}
    ```
    
- 게시글 수정
    ```
    curl -X PATCH https://{SERVER_URL}/posts/{id} \
      -H "Authorization: Bearer {API_KEY}" \
      -H "Content-Type: application/json" \
      -d '{ \
          	"title": "제목", \
            "content": "내용" \
          }'
    ```
    
- 게시글 삭제
    ```
    curl -X DELETE https://{SERVER_URL}/posts/{id} \
      -H "Authorization: Bearer {API_KEY}"
    ```
    
    

# - 데이터베이스 테이블 구조

# - 구현한 API 의 동작을 촬영한 데모 영상 링크

# - 구현 방법 및 이유에 대한 간략한 설명

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
    
    
