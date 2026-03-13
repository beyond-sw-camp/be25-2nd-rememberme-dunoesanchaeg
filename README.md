# REMEMBER-ME

## 👥 팀원 소개

<table style="width:100%;">
  <thead>
    <tr align="center">
      <th>양준석</th>
      <th>박재하</th>
      <th>모희주</th>
      <th>윤준상</th>
      <th>이슬이</th>
      <th>조하은</th>
     </tr>
  </thead>

   <!-- 이미지 추가(필수) 및 깃허브 링크 삽입(선택) -->  
   <!--
   <tbody>
    <tr align="center">
      <td>
       
      </td>
      <td>
       
      </td>
      <td>
       
      </td>
      <td>
       
      </td>
      <td>
       
      </td>
      </tr>
    </tbody>
    -->
</table>
<br>

## 📍 목차

1. [프로젝트 개요](#1-프로젝트-개요)
2. [배경 및 필요성](#2-배경-및-필요성)
3. [WBS](#3-WBS)
4. [기술 스택](#4-기술-스택)  
5. [시스템 아키텍처](#5-시스템-아키텍처)  
6. [요구사항 정의서](#6-요구사항-정의서)  
7. [테이블 정의서](#7-테이블-정의서)  
8. [ERD](#8-ERD)
9. [화면 및 기능 설계서](#9-화면-및-기능-설계서)
10. [API 명세서](#10-API-명세서)  
11. [테스트 계획서](#11-테스트-계획서)
12. [결과 보고서](#12-결과-보고서)
13. [회고](#13-회고)

<br>

## <a id="1-프로젝트-개요"></a> 1. 프로젝트 개요  

본 프로젝트는 치매 예방을 목적으로 하는 웹사이트를 개발하는 것을 목표로 한다.

<img width="645" height="280" alt="인구고령화 자료" src="https://github.com/user-attachments/assets/b9c52e23-bf9f-46b0-a856-14e1c06ceeb1" />


최근 인구 고령화가 빠르게 진행되면서 치매 환자 수 또한 지속적으로 증가하고 있으며, 이에 따라 개인과 사회가 부담해야 하는 경제적·사회적 비용 또한 점점 커지고 있다. 이러한 문제를 예방적인 관점에서 접근하기 위해, 사용자가 일상 속에서 간단한 두뇌 활동을 꾸준히 수행할 수 있도록 돕는 웹 기반 서비스를 기획하였다.

본 웹사이트는 사용자에게 매일 미션 3가지를 제공하여 지속적인 두뇌 활동을 유도한다. 제공되는 미션은 미니 게임, 개방형 질문, 하루 기록으로 구성되어 있으며, 각각의 활동을 통해 기억력과 사고력 등을 자극하고 자신의 일상생활을 돌아볼 수 있도록 설계하였다.

먼저 미니 게임은 문제의 지시사항을 읽고 그에 맞는 정답을 선택하는 방식으로 진행되며, 사용자가 간단한 문제 해결 과정을 통해 두뇌를 활용하도록 돕는다. 개방형 질문은 사용자가 단순한 단답형 답변이 아닌 자신의 과거 경험이나 기억을 떠올리며 서술하도록 유도하여 회상 능력과 사고 활동을 촉진한다. 마지막으로 하루 기록은 사용자가 자신의 하루를 돌아보며 오늘의 기분, 식사, 수면 등 생활 패턴을 기록하도록 하여 일상에 대한 인식과 자기 성찰의 시간을 갖는다.

이와 같은 기능을 통해 사용자가 일상 속에서 자연스럽게 두뇌 활동을 지속할 수 있도록 하여 치매 예방에 긍정적인 영향을 줄 수 있는 웹 서비스를 제공하고자 한다.

<br>

## <a id="2-배경-및-필요성"></a> 2. 배경 및 필요성

중앙치매센터[[1]](https://www.nid.or.kr/info/dataroom_view.aspx?bid=317)에 따르면, 전국 65세 이상 추정 치매 환자 수는 2019년 이후 매년 증가하는 추세를 보이며 2023년에는 약 87만 명으로 전년 대비 약 5.1% 증가한 것으로 나타났다.


<img width="645" height="486" alt="치매환자수 자료" src="https://github.com/user-attachments/assets/354e3c0c-7009-4762-8fd3-0fd007cca103" />


치매 환자의 증가와 함께 경제적 부담 또한 큰 문제로 대두되고 있다. 치매 환자 1인당 연간 관리 비용은 연간 가구 소득의 약 43.8%에 해당하는 수준으로 나타나 개인과 가족에게 상당한 부담이 되고 있다. 향후 고령 인구의 증가와 함께 치매 환자 수 역시 지속적으로 증가할 것으로 예상되기 때문에, 치매를 치료하는 것뿐만 아니라 사전에 예방하는 것이 매우 중요하다.


<img width="645" height="384" alt="치매치료비 자료" src="https://github.com/user-attachments/assets/bfd7fa62-85ca-457d-ade4-6784766a12fa" />


치매 예방을 위해서는 낱말 맞추기, 글쓰기, 문화 및 취미 활동과 같이 뇌세포를 지속적으로 자극할 수 있는 두뇌 활동을 꾸준히 수행하는 것이 중요한 것으로 알려져 있다. 특히 이러한 활동을 즐겁고 지속적으로 수행하는 것이 장기적인 예방 효과에 긍정적인 영향을 줄 수 있다.

이에 본 프로젝트에서는 사용자가 일상생활 속에서 부담 없이 참여할 수 있는 웹 기반 치매 예방 서비스를 기획하였다. 사용자가 매일 간단한 미션을 수행하면서 지속적인 두뇌 활동을 유도하고 치매 예방에 도움을 줄 수 있는 환경을 제공하고자 한다.

<br>

## <a id="3-WBS"></a> 3. WBS

<details>
<summary>세부사항</summary>
<div markdown="1">
 
</div>
</details>
<br>

## <a id="4-기술-스택"></a> 4. 기술 스택

### BACKEND

<img src="https://img.shields.io/badge/SpringBoot-10B146?style=for-the-badge&logo=SpringBoot&logoColor=white">

### FRONTEND

<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">

### DATABASE

![mariadb](https://github.com/user-attachments/assets/19a0ad09-804d-4303-80bd-32cafdae0e6f)

### IDE

![intellij](https://github.com/user-attachments/assets/25d426ed-e30e-4619-9968-11375adba8b9)

### COMMUNICATION

![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
</details>

<br>

## <a id="5-시스템-아키텍처"></a> 5. 시스템 아키텍처

<br>

## <a id="6-요구사항-정의서"></a> 6. 요구사항 정의서

<details>
<summary>세부사항</summary>

- [📂 요구사항 정의서 (링크)](https://docs.google.com/spreadsheets/d/1cGxKolGeDWGtzUYxWShYSNCS0KK_gclYAco5hxDQDqI/edit?gid=1415182395#gid=1415182395)

</details>

<br>

## <a id="7-테이블-정의서"></a> 7. 테이블 정의서

<details>
<summary>세부사항</summary>
  
- [📂 테이블 정의서 (링크)](https://docs.google.com/spreadsheets/d/1W4umq2TJ3RlpNsyxd6Db3YlDfhOW4DBUH2VfQkSFBGc/edit?gid=0#gid=0)

</details>

<br>

## <a id="8-ERD"></a> 8. ERD

<details>
<summary>세부사항</summary>
<img src="./images/ERD.png" width="1000" alt="ERD image" /></br>
  
- [📌 ERD 구조도 (링크)](https://www.erdcloud.com/d/puoaE8Gz75mCg6pJt)
  
</details>

<br>

## <a id="9-화면-및-기능-설계서"></a> 9. 화면 및 기능 설계서

<details>
<summary>세부사항</summary>
<div markdown="1">
 
</div>
</details>

<br>

## <a id="10-API-명세서"></a> 10. API 명세서

<details>
<summary>세부사항</summary>
  
- [📌 API 명세서 (링크)](https://www.notion.so/API-308dd863c93280e2808fdca71cc4adde)

</details>

<br>

## <a id="11-테스트-계획서"></a> 11. 테스트 계획서

<details>
<summary>세부사항</summary>
<div markdown="1">
 
</div>
</details>

<br>

## <a id="12-결과-보고서"></a> 12. 결과 보고서

<br>

## <a id="13-회고"></a> 13. 회고

|   이름   |     회고 내용     |
|-----------|-----------------|
|      양준석      |     준석님 작성1     |
|      박재하      |     재하님 작성     |
|      모희주      |     희주님 작성     |
|      윤준상      |     준상님 작성     |
|      이슬이      |     슬이님 작성     |
|      조하은      |     하은님 작성     |

<br>
