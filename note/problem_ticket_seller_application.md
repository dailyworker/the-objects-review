# 오브젝트 - 티켓 판매 애플리케이션 구현하기

해당 코드는 문제가 많은 코드이다.

무엇이 문제일까? 

# 무엇이 문제인가

> 모든 모듈은 제대로 실행돼야 하고, 변경이 용이해야 하며, 이해하기 쉬워야 한다. - Robert C. Martin(Clean Software, 2002)

해당 코드는 제대로 실행 되어야 한다는 조건은 만족한다.

그러나, 변경 용이성과 읽는 사람과의 의사소통 (이해하기 쉬워야한다)라는 요구사항을 만족시키지는 않는다.

## 예상을 빗나가는 코드

*****관람객(Audience)와 판매원(TicketSeller)가 수동적인 존재이다.**

### 현실과 어긋나는 구현 코드

```java
//Theater.class 
if(audience.getBag().hasInvitation()) {
	Ticket ticket = ticketSeller.getTicketOffice().getTicket();
	audience.getBag().setTicket(ticket);
}
```

해당 코드는 제 3자인 극장(Teater)이 관람객의 가방을 뒤져서 입장을 처리하는 모습이다.

현실 세계에서 자신의 가방을 뒤지는 것을 방관할 수 있는가? 

### 적절하지 않은 책임 부여

```java
//Theater.class 
Ticket ticket = ticketSeller.getTicketOffice().getTicket();
audience.getBag().minusAmount(ticket.getFee());
ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
audience.getBag().setTicket(ticket);
```

또한, 더 큰 문제는 티켓이 없는 관람객(Audience)에게 받은 돈을 매표소에 적립하는 것을 극장(Teater)에서 처리한다는 것이다. 이것은 판매원(TicketSeller)의 책임이라고 볼 수 있지 않을까?

### 세부적인 것들에 대한 의존

```java
//Theater.class 
Ticket ticket = ticketSeller.getTicketOffice().getTicket();
audience.getBag().minusAmount(ticket.getFee());
ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
audience.getBag().setTicket(ticket);
```

같은 코드지만 더 볼 것이 남아있다. 해당 코드는 극장(Teater)에서 입장(enter)를 처리하는 메소드의 일부이다.

너무 구체적인 것들에 의존하고 있지 않은가? 단순하게 입장하는 코드인데 `getBag()` 과 `minusAmount()` 와 같은 세부적인 로직을 기억할 필요가 있을까?

이는 **추가적인 로직 생성이나 로직의 변경이 존재할 경우 부담을 주기에 충분**하다.

## 변경에 취약한 코드

이 코드의 가장 심각한 문제는 단순한 요구사항이 변경되기만 해도 와르르 무너지는 코드라는 점이다.

예를 생각해보자. 관람객이 현금이 아니라 신용카드를 이용할 수 있게 요구사항을 변경해본다고 말이다.

그렇다면, 수 많은 코드를 고쳐야 하는 문제가 생길 것이다.

또한, 관람객(Audience)가 가방(Bag)을 가지고 다니지 않는다는 추가적인 요구사항이 발생할 경우에도 위와 같은 동일한 문제가 발생한다. 

이러한 문제가 발생한 원인은 극장(Teater)가 관람객이 가방을 들고 있고, 판매원(TicketSeller)가 매표소(TicketOffice)에서만 **티켓을 판매한다는 지나치게 세부적인 사실에 의존해서 동작**하기 때문이다.

이는 어떻게 보면 의존역전원칙(Dependency Inversion Principle, 이하 DIP)을 위배한다 볼 수 있다.

물론, 정확히 DIP 위배라고는 볼 수 없지만, 큰 틀로 봤을 때 DIP는 구체적인 것에 의존하는 것이 아니라 추상적인 것에 의존해야되는 것이므로 얼추 맞는다고 나는 생각한다.

즉, 객체 사이의 의존성과 관련된 문제가 발생한 것이다. 위의 코드는 객체들 간의 결합도(Coupling)이 높으므로, 사소한 변경에도 결합도가 높다보니 함께 변경될 소지가 존재하는 것이다.

우리가 학부 시절 객체지향 설계나 객체지향 프로그래밍에서 봤던 **Low Coupling, high Cohesion**을  위반한 코드로 볼 수 있다.

**따라서 우리는 필요한 최소한의 의존성만 유지하고 불필요한 의존성을 제거하는 것을 초점을 맞춰서 개선해야한다.**
