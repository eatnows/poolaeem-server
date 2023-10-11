# 풀내임 (Poolaeem)
<div align="center">

  [https://poolaeem.com](https://poolaeem.com "단어퀴즈 내고 풀고, 풀내임")

  <img width="300" alt="poolaeem_image" src="https://github.com/eatnows/poolaeem-server/assets/59526368/82a500b9-2f5a-4a8a-812b-5a6e3ec6bf45">
  <br> 
  단어퀴즈 내고 풀고, 풀내임
</div>

## 목차
- **[서비스 소개](#서비스-소개)**
- **[개발 정보](#개발-정보)**


## 서비스 소개

|<img height="300" alt="poolaeem_list" src="https://github.com/eatnows/poolaeem-server/assets/59526368/085a5cde-9f5c-4ad5-9b29-778d3043e10e"> |  <img height="300" alt="poolaeem_problem" src="https://github.com/eatnows/poolaeem-server/assets/59526368/2a9f043c-a3e6-4918-b1c1-8b8e24dcd74c"> | <img height="300" alt="poolaeem_solve" src="https://github.com/eatnows/poolaeem-server/assets/59526368/6ccd70e8-199e-46f3-a7b1-ee26435de12f"> | <img height="300" alt="poolaeem_result" src="https://github.com/eatnows/poolaeem-server/assets/59526368/d2a87409-a722-46fa-8341-39e0ca433940"> |
|:----------:|:----------:|:---------:|:--------:|
|문제 목록|문제 만들기|문제 풀이|풀이 결과|
<br>

학교, 스터디, 자격증 등 공부한 지식을 문제로 만들고, 출제된 문제를 여러 사람이 풀 수 있는 웹 서비스입니다.


- **풀고 싶은 문제가 있는지 확인해보세요!** <br>
  내가 만들거나, 다른 사람들이 만든 문제집들을 확인할 수 있어요. <br>
  내가 찾고 있던 문제집이 있는지 찾아보세요.
- **남기고 싶거나 복습하고 싶은 지식을 문제로 만들어보세요!** <br>
  꼭 알아야하는 중요한 문제나 이해가 잘 안되는 지식들을 내가 원하는 때에 다시 풀 수 있게 문제로 만들어 보세요. <br>
  만든 문제는 다른 사람들에게 공유하여 여러 사람들이 풀 수 있어요.
- **여러 문제를 풀고 지식을 채워보세요!** <br>
  가입을 하지 않고도 문제를 풀 수 있어요. <br>
  언제 어디서든 공개된 여러 문제들을 풀고 틀린 문제를 확인하여 나의 부족한 부분을 채워보세요.
<br>

## 만든이
### 팀 구공일
UX/UI, FrontEnd, BackEnd 각각 포지션 별 한 명씩 맡고있는 팀으로 다양한 웹, 앱 서비스를 선보일 예정입니다.
<br> <br>

## 개발 정보
<p align="center">
<img width="600" alt="poolaeem_architecture" src="https://github.com/eatnows/poolaeem-server/assets/59526368/43204abf-c118-4dcd-a178-7e52a849c2ae">
</p>

### 기술 스택
UX/UI: `Figma` <br>
FrontEnd: `TypeScript`, `React`, `Next.js`, `React-query`, `Emotion` <br>
BackEnd: `Java 17`, `SpringBoot 3.1`, `SpringSecurity`, `JPA`, `MySQL 8.0.32`, `Testcontainers`, `flyway` <br>
Infra / DevOps: `AWS`, `Terraform`, `GitHub Actions`, `Grafana Cloud(Grafana, Prometheus, Loki)` <br>
etc: `Jira`, `Confluence`
<br> 
<br>

#### 인증
자체 로그인 없이 OAuth2.0으로 로그인 후 JWT를 이용하여 인증을 진행합니다.
#### 배포
Github Actions와 AWS CodeDeploy를 이용하여 blue/green 무중단 배포가 진행됩니다.
#### 모니터링
Grafana Cloud를 이용하여 Promethus로 메트릭 수집, Loki로 로그를 수집하여 모니터링에 사용하고 있습니다.


