# delivery-coupon
<div style="display:flex; flex-direction:column; align-items:flex-start;">
      <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white"> 
      <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=spring boot&logoColor=white">
      <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white">
      <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
      <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=amazon ec2&logoColor=white"> 
      <img src="https://img.shields.io/badge/Amazon rds-527FFF?style=for-the-badge&logo=amazon rds&logoColor=white">
      <img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=Amazon%20S3&logoColor=white">
      <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
      <img src="https://img.shields.io/badge/Github%20Actions-282a2e?style=for-the-badge&logo=githubactions&logoColor=367cfe">
      <img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white">
      <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
</div>
<br/>
<br/>

## 📣 프로젝트 개요
해당 프로젝트는 배달의 민족을 모티브로 한 음식 주문 서비스로, 쿠폰 발급 및 관리 기능에 중점을 두고 있습니다. 관리자는 다양한 방식으로 쿠폰을 발급할 수 있으며, 사용자는 발급받은 쿠폰을 주문 시 적용하여 할인을 받을 수 있습니다.

<br/>
<br/>

## ⚒️ 기술스택
### 개발 환경
- Java 17
- Spring Boot 3.2.8
- Spring Security 6.2.5
- JPA
- AWS RDS(MySQL 8.0.35)
- AWS EC2, S3, Code Deploy
- gradle 8.8
- Jacoco

<br/>

### 기타
- ERDCloud
- Postman
- Rest Docs
- Mysql WorkBench
- Github Action

<br/>
<br/>

## 패키지 구조
```
**coupon**
├─docs
│  └─asciidoc
├─main
│  ├─java
│  │  └─clone
│  │      └─coding
│  │          └─coupon
│  │              ├─config
│  │              ├─coupon
│  │              ├─controller
│  │              ├─dto
│  │              │  ├─brand
│  │              │  ├─coupon
│  │              │  ├─couponwallet
│  │              │  ├─customer
│  │              │  ├─menu
│  │              │  ├─order
│  │              │  ├─ordermenu
│  │              │  └─store
│  │              ├─entity
│  │              │  ├─admin
│  │              │  ├─coupon
│  │              │  ├─customer
│  │              │  ├─orderhistory
│  │              │  ├─refresh
│  │              │  └─store
│  │              ├─global
│  │              │  ├─exception
│  │              │  └─jwt
│  │              ├─repository
│  │              └─service
│  │              
│  └─resources
└─test
    └─java
        └─clone
            └─coding
                └─coupon
                    ├─controller
                    └─restdocs
```

<br/>
<br/>

## 📄 ERD
<img width="1430" alt="쿠폰 ERD" src="https://github.com/user-attachments/assets/87b6893a-8228-4351-bde8-a434bb92df1d">
<img width="1138" alt="image" src="https://github.com/user-attachments/assets/606ca7e5-4b1f-4d04-805c-a4676248b5f6">

- API 명세서
  <br/>
  http://ec2-3-38-255-101.ap-northeast-2.compute.amazonaws.com/docs/asciidoc/index.html


<br/>
<br/>


## ⚙ 아키텍처
![쿠폰 배경](https://github.com/user-attachments/assets/38ffd72e-e250-4b47-ade0-4b1d2ef8ee53)














