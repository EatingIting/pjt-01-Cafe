# Java Cafe – Spring Boot 기반 카페 주문 서비스

일반 사용자 주문 → 관리자 실시간 주문 수신(SSE) → 주문 처리까지 전체 흐름을 구현한 프로젝트 입니다.

---

## 1. 프로젝트 소개

Spring Boot, MyBatis, JavaScript(SSE), Thymeleaf 기반으로 구현한 실시간 카페 주문 서비스입니다.  
사용자는 앱에서 주문을 생성하고, 각 매장의 관리자는 관리자 화면에서 실시간으로 주문 카드가 뜨는 방식으로 주문을 처리합니다.

처음에는 **Polling 방식**으로 구현했지만 트래픽/지연 문제가 있어 **SSE(Server-Sent Events)** 로 전환했습니다.

---

## 2. 전체 시스템 구조

[사용자 페이지] → 주문 생성  
↓  
[Spring Boot 서버] → 주문 DB 저장(MySQL)  
↓  
[SSE Controller] → 매장 관리자에게 실시간 주문 Push  
↓  
[관리자 페이지]
- 주문 카드 실시간 생성
- 상태 변경 시 SSE 브로드캐스트

(이미지 삽입 위치)  
`![주문관리UI](images/주문관리UI.png)`

---

## 3. 주요 기술 스택

**Backend**
- Spring Boot 3
- Spring MVC / Security
- MyBatis
- SSE(Server-Sent Events)
- MySQL

**Frontend**
- JavaScript
- Thymeleaf
- Fetch API
- CSS Grid / Flexbox

---

## 4. Polling → SSE 로 바꾼 이유

### Polling 방식의 문제점

1. **주기적으로 서버에 요청을 보내므로 불필요한 트래픽이 증가합니다.**
2. **응답이 오기까지의 시간 때문에 실시간성이 떨어집니다.**
3. **주문량이 증가할수록 서버 부하가 커집니다.**

이 문제들을 해결하기 위해 SSE(Server-Sent Events)를 도입했습니다.

---

## 5. SSE(Server-Sent Events) 적용 후 장단점

### 장점

- **관리자 페이지에서 주문이 즉시 나타나는 진짜 실시간 경험 제공**
- **Polling 대비 트래픽·서버 부하 감소**
- **브라우저 기본 지원으로 구현 난이도가 낮음**
- **단방향 스트림에 특화되어 있어 주문 Push 시나리오에 적합**

### 단점

- **단방향이므로 클라이언트 → 서버 메시지 전송은 Fetch/AJAX를 따로 사용해야 합니다.**
- **연결이 끊겼을 때 자동 복구 처리를 직접 구현해야 합니다.**
- **프록시·로드밸런서 환경에서 Timeout 설정을 맞춰야 안정적으로 동작합니다.**

---

## 6. 핵심 구현 코드 요약

### 6-1. SSE Controller

```java
@GetMapping(value = "/sse/admin/{storeName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public SseEmitter subscribeAdmin(@PathVariable String storeName) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    emitterStore.addAdminEmitter(storeName, emitter);

    emitter.send(SseEmitter.event().name("connect").data("connected"));
    return emitter;
}
```

---

### 6-2. 주문 생성 후 매장에 Push

```java
public void notifyStore(String storeName, OrderVO order) {
    emitterStore.sendToStore(storeName, "new-order", order);
}
```

---

### 6-3. 관리자 페이지에서 SSE 수신

```javascript
const eventSource = new EventSource(`/sse/admin/${storeName}`);

eventSource.addEventListener("new-order", (event) => {
    const order = JSON.parse(event.data);
    renderNewOrderCard(order);
});
```

---

## 7. 관리자 페이지 동작 흐름

1. 페이지 로딩 → 기존 주문 목록 1회 로드
2. SSE 연결
3. 새로운 주문 발생 시 `new-order` 이벤트 수신
4. 주문 카드 UI 즉시 생성
5. 완료/취소 버튼 클릭 시 Fetch API로 상태 변경 요청
6. 서버에서 상태 변경 브로드캐스트 → 다른 관리자 화면 즉시 갱신

---

## 8. 프로젝트 회고

SSE를 적용하면서
- 실시간 동기화 처리
- 네트워크 단절 시 재연결 처리
- 여러 매장 단위로 emitter를 분리 관리
- 중복 카드 방지  
  등의 실제 서비스에서 필요한 문제들을 직접 해결해볼 수 있었습니다.

Polling 방식보다 훨씬 자연스러운 실시간 경험을 제공할 수 있었으며,  
프론트와 백엔드 모두 구조가 명확해졌습니다.

---

## 9. 이미지

```markdown
![로그인 화면](images/login.png)
![주문 화면](images/order1.png)
![주문 화면2](images/order2.png)
![주문 화면3](images/order3.png)
![SSE 실시간 알림](images/sse-realtime.gif)
```

