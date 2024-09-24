<div align="center">

# OTTI_Backend

<table>
  <tbody>
    <tr align="center">
      <td colspan="3">
        SKHU IT 경진대회 출품작 <br/> Team Hoodie Backend
      </td>
    </tr>
    <tr align="center">
      <td colspan="3">
        <h3>• OTTi •</h3>
      </td>
    </tr>
    <tr align="center">
      <td colspan="3">
        <img src="https://otti-bucket-2024.s3.ap-northeast-2.amazonaws.com/otti-image/otti.png" width="50%"/>
      </td>
    </tr>
    <tr align="center">
      <td colspan="3">OTT 구독료 관리를 효율적으로 지원하고,<br/>사용자 간의 공동 구독 기능을 제공하는 사용자 중심의 앱</td>
    </tr>
    <tr>
      <td align="center">
        <strong>Project Resources</strong>
      </td>
      <td rowspan="9" align="center">|<br/>•<br/>|<br/>•<br/>|<br/>•<br/>|<br/>•<br/>|<br/>•<br/>|<br/>•<br/>|<br/>•<br/>|</td>
      <td align="center"><strong>목차</strong></td>
    </tr>
    <tr>
      <td align="center" rowspan=2><a href="https://otti.vercel.app">View in Web</a></td>
      <td align="center">
        <a href="#-team-hoodie">Team Hoodie?</a> <br/>
      </td>
    </tr>
    <tr>
      <td align="center">
        <a href="#-작품-개요">작품 개요</a> <br/>
      </td>
    </tr>
    <tr>
      <td align="center">•</td>
      <td align="center">
        <a href="#작품-구성">작품 구성</a> <br/>
      </td>
    </tr>
    <tr>
      <td align="center" rowspan=2><a href="">Download mobile app</a></td>
      <td align="center">
        <a href="#%EF%B8%8F-기술-스택">기술 스택 & 서버 아키텍쳐</a> <br/>
      </td>
    </tr>
    <tr>
      <td align="center">
        <a href="#database-erd">Database ERD</a> <br/>
      </td>
    </tr>
    <tr>
      <td align="center">•</td>
      <td align="center">
        <a href="#-주요-개발-방법">주요 개발 방법</a> <br/>
      </td>
    </tr>
    <tr>
      <td align="center" rowspan=2><a href="https://otti-hoodie.duckdns.org/swagger-ui/index.html#/">Documentation</a></td>
      <td align="center">
        <a href="#-프로젝트-확장성">프로젝트 확장성</a> <br/>
      </td>
    </tr>
    <tr>
      <td align="center">
        <a href="#-member-of-hoodie">Member of Hoodie</a> <br/>
      </td>
    </tr>
  </tbody>
</table>


<hr/>

## 👕 Team Hoodie?

팀원 모두가 후드를 착용하고 있었던 당시의 모습에서 영감을 받아,<br/>창의적이고 자유로운 협업의 의지를 담고 있음.<br/>'Hoodie'라는 이름은 이러한 팀의 정체성을 반영하며,<br/>협업의 분위기를 강조함. | <img src="https://github.com/user-attachments/assets/43edf6c0-0874-4e02-afbf-852765dfb6ba" alt="Team Hoodie" width="150">
---|---|

<br/>
<hr/>

## 🧐 작품 개요
  <blockquote align="left">
	  본 작품은 OTT 구독료 관리의 불편함을 해결하기 위해 개발됨.<br/> 
	  매달 OTT 구독료의 자동이체 시점을 기억하기 어렵고,<br/> 
	  갑작스럽게 통장에서 인출되는 구독료의 출처를 잊어버리는 경우가 빈번하다는 의견에서 출발함.<br/> 
	  <br/> 
	  이를 해결하기 위해 사용자는 OTT 구독 정보를 저장하고 구독료의 출금 일정을 캘린더와 파이 차트, 디데이 기능을 통해 시각적으로 확인할 수 있음.<br/> 
	  또한, '팟' 기능을 도입하여 팀원을 모집하고 OTT 구독료를 공동으로 분담할 수 있도록 함.<br/> 
	  사용자는 각자의 조건을 작성하여 팀원을 찾는 게시글을 올릴 수 있으며,<br/> 
	  이를 통해 넷플릭스와 같은 여러 계정을 함께 사용할 수 있는 기회를 제공함.<br/> 
	  이러한 기능은 사용자 간의 협업을 촉진하고, OTT 구독료 관리의 효율성을 높임.<br/> 
	  <br/> 
	  유사한 앱으로는 피클플러스와 링키드가 있음. 두 앱은 주로 구독 서비스를 공유하는 멤버를 찾는 기능에 중점을 두고 있음.<br/> 
	  반면, 본 작품은 구독료 공동 분담 기능을 서브 기능으로 두고, 메인 기능으로는 사용자가 자신의 OTT 구독 스케줄을 한눈에 보고 기억할 수 있도록 돕는 점에서 차별성을 가짐.
	  <br/> 구독료 출금 일정을 시각적으로 관리하고, 사용자가 일정 관리를 보다 체계적으로 할 수 있도록 설계된 것이 이 앱의 주요 강점임.<br/> 
  </blockquote>
<br/>
<hr/>

## 📑작품 구성
<table>
    <thead>
        <tr>
            <th>기능</th>
            <th>상세 기능</th>
            <th>설명</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><strong>소셜 로그인</strong></td>
            <td>카카오 OAuth를 활용한 간편한 소셜 로그인 기능</td>
            <td>사용자가 손쉽게 계정을 생성하고 로그인할 수 있음</td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td rowspan="2"><strong>구독료 관리</strong></td>
            <td>총 구독료 확인</td>
            <td>이번 달 기준으로 사용자가 지출한 OTT 구독료를 한눈에 볼 수 있도록 표시</td>
        </tr>
        <tr>
            <td>한 번에 보기</td>
            <td>구독 중인 OTT 서비스들의 결제 날짜를 캘린더로 시각화하여 쉽게 파악할 수 있도록 함</td>
        </tr>
        <tr>
            <td rowspan="2"><strong>캘린더 기능</strong></td>
            <td>결제 날짜 표시</td>
            <td>달력에 구독 서비스의 결제 날짜를 표시하고, 특정 날짜를 클릭하면 해당 OTT 서비스의 구독 정보를 상세히 확인할 수 있는 모달창이 뜸</td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td rowspan="2"><strong>구독 OTT 추가 및 <br/> 관리 기능</strong></td>
            <td>사용자가 구독 중인 OTT 서비스 추가</td>
            <td>넷플릭스, 웨이브, 티빙, 쿠팡플레이, 디즈니플러스, 왓챠의 요금제, 결제 금액, 결제일 등을 입력 및 수정할 수 있음</td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td rowspan="2"><strong>구독 정보 관리</strong></td>
            <td>구독 정보 전체 확인</td>
            <td>사용자의 구독 정보 전체를 한눈에 확인할 수 있으며, 요금제, 결제 금액, 결제일, 메모 등을 수정하거나 삭제 가능</td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td rowspan="4"><strong>알림 기능</strong></td>
            <td>OTT 구독료 납부일 알림</td>
            <td>3일 전에 알람을 보내 구독료 납부를 잊지 않도록 도와줌</td>
        </tr>
        <tr>
            <td>읽지 않은 알림 확인</td>
            <td>사용자는 읽지 않은 알림을 한눈에 확인할 수 있으며, 팟 기능 관련 알림도 포함됨</td>
        </tr>
        <tr>
            <td>팟 신청 알림</td>
            <td>팟을 생성한 사용자에게는 팟 신청 알림이 전송되며, 팟 신청이 승인되거나 거절될 경우 해당 내용에 대한 알림이 신청자에게 전달됨</td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td rowspan="3"><strong>프로필 관리</strong></td>
            <td>프로필 사진, 닉네임 수정</td>
            <td>사용자는 프로필 사진, 닉네임을 수정할 수 있으며, 기본 프로필 사진은 오띠 로고로 설정됨</td>
        </tr>
        <tr>
            <td>로그아웃 및 계정 탈퇴 기능</td>
            <td>로그아웃, 계정 탈퇴 기능도 제공</td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td rowspan="4"><strong>팟(Pot) 기능</strong></td>
            <td>팀원 모집 및 구독료 공동 분담</td>
            <td>사용자가 팀원을 모집해 구독료를 공동으로 분담할 수 있는 기능</td>
        </tr>
        <tr>
            <td>팟 만들기</td>
            <td>사용자가 원하는 OTT 서비스 및 요금제를 선택하고, 입금 계좌 및 결제일을 설정할 수 있음</td>
        </tr>
        <tr>
            <td>팟 신청 리스트</td>
            <td>팟에 지원한 사용자 목록을 확인하고, 각 지원자의 프로필 사진, 닉네임, 한 줄 소개를 볼 수 있음</td>
        </tr>
        <tr>
            <td>팟 관리</td>
            <td>팟 멤버 목록 확인, 방장의 멤버 관리(강퇴 가능), 공지 사항 작성 등의 기능을 제공</td>
        </tr>
        <tr>
            <td rowspan="2"><strong>게시글 관리</strong></td>
            <td>작성한 글 목록 확인</td>
            <td>사용자가 작성한 글 목록을 확인하고, 글 제목, 내용 미리보기, 댓글 개수 등의 정보를 확인할 수 있음</td>
        </tr>
        <tr>
            <td>글 작성, 수정, 삭제 및 <br> 사진 추가</td>
            <td>글을 작성하거나 수정, 삭제할 수 있으며, 글에 사진을 추가할 수 있음</td>
        </tr>
    </tbody>
</table>
<br>

---

<table>
  <tbody>
    <tr>
      <td align="center">
        <h2>🛠️ 기술 스택</h2>
      </td>
      <td align="center">
        <h2>🏛️ 서버 아키텍쳐</h2>
      </td>
    </tr>
    <tr>
      <td>
        <div align="center">
          <table style="width: 100%; table-layout: fixed;">
            <thead>
              <tr>
                <th>Role</th>
                <th>Stack</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td rowspan="5">FE</td>
                <td><img src="https://img.shields.io/badge/react-61DAFB?style=flat-square&logo=react&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/styledcomponents-DB7093?style=flat-square&logo=styledcomponents&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/typescript-3178C6?style=flat-square&logo=typescript&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/axios-5A29E4?style=flat-square&logo=axios&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/vercel-000000?style=flat-square&logo=typescript&logoColor=white"/></td>
              </tr>
              <tr>
                <td rowspan="4">BE</td>
                <td><img src="https://img.shields.io/badge/JAVA-007396?style=flat-square&logo=java&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/swagger-85EA2D?style=flat-square&logo=swagger&logoColor=white"></td>
              </tr>
              <tr>
                <td rowspan="2">Infra</td>
                <td><img src="https://img.shields.io/badge/amazon-232F3E?style=flat-square&logo=amazonwebservices&logoColor=white"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/amazons3-569A31?style=flat-square&logo=amazons3&logoColor=white"/></td>
              </tr>
              <tr>
                <td rowspan="2">App</td>
                <td><img src="https://img.shields.io/badge/React Native-61DAFB?style=flat-square&logo=React&logoColor=black"/></td>
              </tr>
              <tr>
                <td><img src="https://img.shields.io/badge/expo-000020?style=flat-square&logo=expo&logoColor=white"/></td>
              </tr>
            </tbody>
          </table>
        </div>
      </td>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/69946724-28a9-4ca8-91f0-3a5f8e655d9a" width="90%">
      </td>
    </tr>
  </tbody>
</table>
<br>

---

## 🗄Database ERD
<table>
  <tbody>
    <tr>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/bb7cee13-90bd-4b93-8ebb-96c087d26fe5" width="60%">
      </td>
    </tr>
  </tbody>
</table>
<br>

---

## 💻 주요 개발 방법

<table>
  <tbody>
    <tr>
      <td colspan="4" align="center"><h4>FE</h4></td>
    </tr>
    <tr>
      <td rowspan="2">&nbsp&nbsp김주하&nbsp&nbsp</td>
      <td align="center">
        <strong><mark>커뮤니티 댓글 작성</mark></strong><br/><br/>
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
        <img src="https://github.com/user-attachments/assets/321f06a0-9364-43ef-9c03-e22594f7c728" width="230">
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
      </td>
      <td rowspan="2">&nbsp&nbsp박예은&nbsp&nbsp</td>
      <td align="center">
        <strong><mark>ott 추가, 삭제, 구독 정보 리스트</mark></strong><br/><br/>
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
        <img src="https://github.com/user-attachments/assets/1fbdfde6-180e-4459-8fd6-96723b08c7a1" width="230">
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
      </td>
    </tr>
    <tr>
      <td align="center">
        <details>
          <summary>설명</summary>
          <div markdown="1">
            <code>handleInputChange</code>: 사용자가 입력한 댓글 내용을 실시간으로 상태에 반영 content 상태를 관리<br/>
            <code>handleAddComment</code>: 댓글이 입력 시 상위 컴포넌트로 전달 입력 필드를 초기화하는 책임을 가짐
          </div>
        </details>
        <details>
          <summary>상세 코드</summary>
          <div markdown="1">
            <img src="https://github.com/user-attachments/assets/e69ff950-60c6-4481-b397-f726b9cdd973" width="100%">
          </div>
        </details>
      </td>
      <td align="center">
        <details>
          <summary>설명</summary>
          <div markdown="1">
            <code>handleChange</code> 함수는 form의 값을 변경할 때 호출되며, 
            <code>ott</code> 정보와 일반 폼 데이터를 구분하여 상태를 업데이트함.<br/><br/>
            <code>if (name.startsWith('ott.'))</code>: <code>name</code>이 <code>'ott.'</code>로 시작하면 OTT 관련 정보를 업데이트함.
            <code>const [_, key] = name.split('.');</code>: <code>name</code>을 ‘.’으로 나누어 두 번째 부분(<code>key</code>)을 가져옴. 이 부분은 <code>ott</code> 객체의 속성 이름이 됨.<br/>
            <code>setFormData((prevFormData) => ({...}))</code>: <code>formData</code> 상태를 업데이트함. 이전 상태를 복사하고 <code>ott</code> 객체 안의 해당 속성만 수정하는 기능을 함. <code>[key]: value</code>를 이용해 특정 OTT 속성을 업데이트함.<br/><br/>
            <code>else</code>의 경우 <code>name</code>이 <code>'ott.'</code>로 시작하지 않으면 일반적인 폼 필드로 간주함. 
            입력 필드의 <code>name</code>이 <code>'payment'</code> 또는 <code>'paymentDate'</code>일 경우 빈 문자열을 <code>0</code>으로 설정함. 
            그렇지 않으면 입력 값을 그대로 사용하여 <code>formData</code> 상태를 업데이트함.
          </div>
        </details>
        <details>
          <summary>상세 코드</summary>
          <div markdown="1"> 
            <img src="https://github.com/user-attachments/assets/5875da9e-d0ff-4597-89d1-b3cc851fbd05" width="80%">
          </div>
        </details>
      </td>
    </tr>
    <tr>
      <td colspan="4"  align="center"><h4>BE</h4></td>
    </tr>
    <tr>
      <td rowspan="2" align="center">&nbsp&nbsp박세은&nbsp&nbsp</td>
      <td align="center">
        <strong><mark>팟 가입 요청 처리</mark></strong><br/><br/>
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
        <img src="https://github.com/user-attachments/assets/a5bc1d1f-fd0c-4144-ac86-a6caa3518821" width="230">
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
      </td>
      <td rowspan="2" align="center">&nbsp&nbsp박소정&nbsp&nbsp</td>
      <td align="center">
        <strong><mark>미사용 이미지 삭제 스케줄링</mark></strong><br/><br/>
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
        <img src="https://github.com/user-attachments/assets/fd78a8cb-ca07-49c9-b02f-53d7ea912655" width="230">
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
      </td>
    </tr>
    <tr>
      <td align="center">
        <details>
          <summary>설명</summary>
          <div markdown="1">
            다른 사용자가 만든 팟에 가입하기 위해서는 <code>PotJoinRequestDTO</code>를 사용하여 가입 요청을 전송해야 함<br/>
            가입 요청은 해당 팟의 관리자에게 전송되며, 관리자는 이를 승인하거나 거부 가능<br/>
            관리자는 팟에 대한 가입 요청을 확인하고 승인 또는 거부할 수 있는 권한을 가짐<br/>
            승인 시 해당 사용자는 Pot 멤버로 추가됨
          </div>
        </details>
        <details>
          <summary>상세 코드</summary>
          <div markdown="1">
            <img src="https://github.com/user-attachments/assets/0b0b6e49-c42d-4216-a259-46a5add23009" width="100%">
          </div>
        </details>
      </td>
      <td align="center">
        <details>
          <summary>설명</summary>
          <div markdown="1">
            사용자가 이미지 호스팅 한 글을 작성 중단한 경우, 사용되지 않는 이미지가 Amazon S3 버킷에 계속 남게 됨<br/>
            이런 이미지는 자원을 낭비하므로 특정 시각에 주기적으로 불필요한 이미지가 삭제되게 구현함<br/>
            Post와 연결되지 않은 이미지 중에서 생성된 지 24시간이 지난 이미지는 버킷과 이미지 레포지토리에서 삭제됨
          </div>
        </details>
        <details>
          <summary>상세 코드</summary>
          <div markdown="1">
            <img src="https://github.com/user-attachments/assets/0dac01fb-66ec-478c-b1bb-0eba445a9859" width="80%">
          </div>
        </details>
      </td>
    </tr>
  </tbody>
</table>

<br/>

---

## ⏩ 프로젝트 확장성

<table>
  <thead>
    <tr>
      <th>🛠️ 기술 측면</th>
      <th>💁‍♀️ 서비스 측면</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>1. 댓글 수정 기능</td>
      <td>1. 배지 기능</td>
    </tr>
    <tr>
      <td>2. 팟 해지(퇴장) 기능</td>
      <td>2. OTT 종류 추가</td>
    </tr>
    <tr>
      <td>3. 팟 정보 수정 기능</td>
      <td>3. 본인 인증(PASS 앱 등)</td>
    </tr>
    <tr>
      <td>4. 팟 본인 인증 강화(인증 시 아이디, 비번, 계좌 확인)</td>
      <td>4. 팟 채팅 기능</td>
    </tr>
    <tr>
      <td>5. 프로필 기본 이미지 변경 기능</td>
      <td></td>
    </tr>
    <tr>
      <td>6. 로그아웃, 탈퇴 기능</td>
      <td></td>
    </tr>
    <tr>
      <td>7. 알림 기능</td>
      <td></td>
    </tr>
    <tr>
      <td>8. 로딩 페이지 디자인 수정 및 필요한 페이지 삽입</td>
      <td></td>
    </tr>
    <tr>
      <td>9. 팟 삭제 기능</td>
      <td></td>
    </tr>
  </tbody>
</table>
<br>
<hr/>
</div>

## 😏 Member of Hoodie

<table>
<thead>
  <tr>
    <th colspan="2">Frontend</th>
    <th colspan="2">Backend</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td align="center">김주하</td>
    <td align="center">박예은</td>
    <td align="center">박세은</td>
    <td align="center">박소정</td>
  </tr>
  <tr>
    <td>
      <a href="https://github.com/laketree2">
        <img src="https://avatars.githubusercontent.com/u/101048129?v=4" style="width:230px"/>
      </a>
    </td>
    <td>
      <a href="https://github.com/yengniws">
        <img src="https://avatars.githubusercontent.com/u/145003970?v=4" style="width:230px"/>
      </a>
    </td>
    <td>
      <a href="https://github.com/seun123">
        <img src="https://avatars.githubusercontent.com/u/101261562?v=4" style="width:230px"/>
      </a>
    </td>
    <td>
      <a href="https://github.com/sojeong0202">
        <img src="https://avatars.githubusercontent.com/u/112674378?v=4" style="width:230px"/>
      </a>
    </td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/laketree2">@laketree2</a></td>
    <td align="center"><a href="https://github.com/yengniws">@yengniws</a></td>
    <td align="center"><a href="https://github.com/seun123">@seun123</a></td>
    <td align="center"><a href="https://github.com/sojeong0202">@sojeong0202</a></td>
  </tr>
</tbody>
</table>

