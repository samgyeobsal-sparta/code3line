# 코드 세 줄
코드 세 줄에서 전 세계 개발자들과 소통하며, 당신의 코딩 아이디어를 공유하세요!  
코드 세 줄로 개발자 커뮤니티에 참여하고, 최신 기술 트렌드를 빠르게 만나보세요!  
코드 세 줄과 함께라면, 매일매일 새로운 개발 아이디어와 인사이트가 기다립니다!  

## 👥 삼🥓겹🥓살🥓 팀 소개
팀 소개 문구!!  
[🥓팀 노션🥓](https://www.notion.so/teamsparta/c9451bbd45cb4097a00f5e7b154fe6da?pvs=4)

| 이름                                        | 블로그                                 |
|-------------------------------------------|-------------------------------------|
| [김혜은(팀장)](https://github.com/MetroDefro) | https://mountain-noroo.tistory.com/ |
| [김민식](https://github.com/minsik0)         | https://velog.io/@minsik097/posts   | 
| [이나영](https://github.com/LeeNaYoung240)   | https://leenayoung240.github.io/    |
| [이민호](https://github.com/2minus)          | https://velog.io/@2_minus/posts     |
| [조규성](https://github.com/Imnotcoderdude)  | https://blog.naver.com/topsolo7     |

## 🔧 기술 스택
- jdk 17

## 🎯 구현 기능
<details>
<summary>🥉 필수 구현</summary>
<div markdown="1">

- [x] 1 단계
- [ ] 2 단계
- [ ] 3 단계
- [ ] 4 단계
</div>
</details>

<details>
<summary>🥈 추가 구현</summary>
<div markdown="1">

- [x] 1 단계
- [ ] 2 단계
- [ ] 3 단계
- [ ] 4 단계
</div>
</details>

<details>
<summary>🥇 명예의 전당</summary>
<div markdown="1">

- [x] 1 단계
- [ ] 2 단계
- [ ] 3 단계
- [ ] 4 단계
</div>
</details>

## 📑 ERD
erd

## 📑 API
api

## 브랜치 규칙
- main, dev, feature 브랜치 사용.
- feature 브랜치에서 기능 개발 완료시 dev 브랜치로 merge
- 프로젝트 완료시 main 브랜치로 merge
- **feature/#이슈번호**
> ex)  
> feature/#36

## 커밋 규칙
- **[#이슈번호] '작업 타입' : '작업 내용'**
> ex)  
> [#36] ✨ feat : 회원가입 기능 추가

| 작업 타입 | 작업내용 |
| --- | --- |
| ✨ feat | 새로운 기능을 추가 |
| 🐛 bugfix | 버그 수정 |
| ♻️ refactor | 코드 리팩토링 |
| 🩹 fix | 코드 수정 |
| 🚚 move | 파일 옮김/정리 |
| 🔥 del | 기능/파일을 삭제 |
| 🍻 test | 테스트 코드를 작성 |
| 🎨 readme | readme 수정 |
| 🙈 gitfix | gitignore 수정 |
| 🔨script | package.json 변경(npm 설치 등) |

## 코드 컨벤션
### DTO
> - `@Data` 어노테이션 사용

### Controller
> - `return ResponseEntity<>.status(HttpStatus.{HttpStatusCode}).body()` 형태로 반환하기
> - `UserPrincipal` 변수를 `User`로 전환해서 `Service` 계층에 전달
