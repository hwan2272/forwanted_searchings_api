# test case 요구사항 정리

### 1. [GET] "/search?query={검색어}"
* 설명 :: <회사명 자동완성>

  회사명의 일부만 들어가도 검색이 되어야 합니다.
  header의 x-wanted-language 언어값에 따라 해당 언어로 출력되어야 합니다.
* 메소드명 :: test_company_name_autocomplete
* 사용 예시 :: resp = api.get("/search?query=링크", headers=[("x-wanted-language", "ko")])
 
### 2. [GET] "/companies/{회사명}"
* 설명 :: <회사 이름으로 회사 검색>

  header의 x-wanted-language 언어값에 따라 해당 언어로 출력되어야 합니다.
* 메소드명 :: test_company_search
* 사용 예시 :: resp = api.get(
  "/companies/Wantedlab", headers=[("x-wanted-language", "ko")]
  )

### 3. [POST] "/companies"
* 설명 :: <새로운 회사 추가>

  새로운 언어(tw)도 같이 추가 될 수 있습니다.
  저장 완료후 header의 x-wanted-language 언어값에 따라 해당 언어로 출력되어야 합니다.
* 메소드명 :: test_new_company
* 사용 예시 :: resp = api.post(
  "/companies",
  json={
  "company_name": {
  "ko": "라인 프레쉬",
  "tw": "LINE FRESH",
  "en": "LINE FRESH",
  },
  "tags": [
  {
  "tag_name": {
  "ko": "태그_1",
  "tw": "tag_1",
  "en": "tag_1",
  }
  },
  {
  "tag_name": {
  "ko": "태그_8",
  "tw": "tag_8",
  "en": "tag_8",
  }
  },
  {
  "tag_name": {
  "ko": "태그_15",
  "tw": "tag_15",
  "en": "tag_15",
  }
  }
  ]
  },
  headers=[("x-wanted-language", "tw")],
  )

### 4. [GET] "/tags?query={검색어}"
* 설명 :: <태그명으로 회사 검색>

  태그로 검색 관련된 회사가 검색되어야 합니다.
  다국어로 검색이 가능해야 합니다.
  일본어 태그로 검색을 해도 language가 ko이면 한국 회사명이 노출이 되어야 합니다.
  ko언어가 없을경우 노출가능한 언어로 출력합니다.
  동일한 회사는 한번만 노출이 되어야합니다.
* 메소드명 :: test_search_tag_name
* 사용 예시 :: resp = api.get("/tags?query=タグ_22", headers=[("x-wanted-language", "ko")])
  searched_companies = json.loads(resp.data.decode("utf-8"))

### 5. [PUT] "/companies/{회사명}/tags"
* 설명 :: <회사 태그 정보 추가>

  저장 완료후 header의 x-wanted-language 언어값에 따라 해당 언어로 출력되어야 합니다.
* 메소드명 :: test_new_tag
* 사용 예시 :: resp = api.put(
  "/companies/원티드랩/tags",
  json=[
  {
  "tag_name": {
  "ko": "태그_50",
  "jp": "タグ_50",
  "en": "tag_50",
  }
  },
  {
  "tag_name": {
  "ko": "태그_4",
  "tw": "tag_4",
  "en": "tag_4",
  }
  }
  ],
  headers=[("x-wanted-language", "en")],
  )

### 6. [DELETE] "/companies/{회사명}/tags/{태그명}"
* 설명 :: <회사 태그 정보 삭제>

  저장 완료후 header의 x-wanted-language 언어값에 따라 해당 언어로 출력되어야 합니다.
* 메소드명 :: test_delete_tag
* 사용 예시 :: resp = api.delete(
   "/companies/원티드랩/tags/태그_16",
   headers=[("x-wanted-language", "en")],
   )

---------------
# 요구사항 분석에 따른 설계

### [주로 사용되는 데이터]
=> company_name, tag_name

### [company_name은 경우에 따라 null 일 수 있으나 반드시 한 language에서 사용함]
=> DB 칼럼 설정때 null허용이 들어가야함

### [x-wanted-language가 request에 항상 들어옴]
=> request 들어올시 header를 체크해야 하며, (header에 해당 키가 없을 시도 생각해주어야 할것 같은데 이 경우에는 language를 ko로 default하게 세팅, 개인적으로 optional하게 추가)

### [PUT, POST api에서 x-wanted-language가 "ko", "en", "jp" 만 들어가는 것이 아님]
=> language를 유동적으로 받아 조정할 수 있어야 함

### [PUT api에서 request body의 ko, en, jp가 항상 짝을 맞춰 들어오진 않음]
=> test case에 따른 설계 조정 필요

### ["(시니어)서버팀 과제.pdf" 에서 "회사명 자동완성-회사명의 일부만 들어가도 검색이 되어야 합니다." 라는 문구가 있음]
=> 기본적으로 like 검색을 하되, 필요에 따라 java단으로 가져와서 contains 처리도 고려

### ["(시니어)서버팀 과제.pdf" 에서 "태그명으로 회사 검색-다국어로 검색이 가능해야 합니다" 라는 문구가 있음]
=> 다국어 like 검색 가능해야 함, (경우에 따라) mariadb 옵션 확인 or 추가설정 필요

### ["(시니어)서버팀 과제.pdf" 에서 "Python flask 또는 fastapi를 이용해서 개발해야 합니다." 라는 문구가 있음]
=> 그러나 메일에는 "Python flask, fastapi말고 편하신 언어로 가능합니다. test case는 파이썬 코드이지만, 이해하는데 무리는 없으실거라 판단합니다. 편하신 테스트 라이브러리를 이용하셔서 테스트 코드를 작성해주시면 되겠습니다." 로 되어있기 때문에 java 사용

### ["(시니어)서버팀 과제.pdf" 에서 "ORM 사용해야 합니다." 라는 문구가 있음]
=> jpa 사용 (Spring Data JPA)

### ["(시니어)서버팀 과제.pdf" 에서 "database는 RDB를 사용해야 합니다" 라는 문구가 있음]
=> mariadb 사용

### ["(시니어)서버팀 과제.pdf" 에서 "database table 갯수는 제한없습니다." 라는 문구가 있음]
 => 테이블이 꼭 1개일 필요는 없다고 판단. company 정보와 tag 정보를 나누어 설계. language테이블과 join하는 식으로 

### ["(시니어)서버팀 과제.pdf" 에서 "Docker로 개발하면 가산점이 있습니다." 라는 문구가 있음]
=> 개발후 시간 남을시 docker화 예정

### [POST, PUT, DELETE api들도 처리후 결과를 return해서 보여주어야 함]
=> request body를 들고 있다가 return해줄때 response로 넣어주어야 함

---------------
# 구축순서
* TestControllerTest 클래스에서 TDD를 해가며 완료후 본패키지로 이동 방식 적용
