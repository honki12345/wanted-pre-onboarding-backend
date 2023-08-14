#### 사용자

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
    
    