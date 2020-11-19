# 오브젝트 - 티켓 판매 애플리케이션 구현하기

해당 코드는 문제가 많은 코드이다.

무엇이 문제일까? 

# 무엇이 문제인가

> 모든 모듈은 제대로 실행돼야 하고, 변경이 용이해야 하며, 이해하기 쉬워야 한다. - Robert C. Martin(Clean Software, 2002)

해당 코드는 제대로 실행 되어야 한다는 조건은 만족한다.

그러나, 변경 용이성과 읽는 사람과의 의사소통 (이해하기 쉬워야한다)라는 요구사항을 만족시키지는 않는다.

## 예상을 빗나가는 코드

**관람객(Audience)와 판매원(TicketSeller)가 수동적인 존재이다.**

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

이는 추가적인 로직이나 로직의 변경이 존재할 경우 부담을 주기에 충분하다.

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

# 무엇이 개선됐는가

![high_coupling](https://user-images.githubusercontent.com/22961251/99396656-eaf6f700-28d9-11eb-92e5-5b621d9a1706.png)

먼저 우리가 개선하기 전에 작성했던 코드의 클래스 다이어그램을 보자.

우리가 참고하면 되는 화살표는 극장(Theater)에서 뻗어나가는 화살표 들이다.

즉, 극장은 TicketOffice와 TicketSeller 그리고 Bag과 Ticket, Audience에 의존성을 갖는다.

수 많은 의존성을 갖는 것을 볼 수가 있다.

## 코드 개선 1 - 판매원을 자율적인 객체로 만듦

개선한 코드를 보면서 해당 클래스 다이어그램이 어떻게 변화했는지 살펴보자.

- 캡슐화를 통하여 판매원(TicketSeller)를 자율적인 객체로 만듦

![refactoring1](https://user-images.githubusercontent.com/22961251/99396946-42956280-28da-11eb-8511-573e5aef648b.png)

위와 같은 코드 개선을 통하여 `TicketOffice`에 대한 접근은 오직 `TicketSeller` 안에만 존재하게 된다.

따라서, `TicketSeller`는 `TicketOffice`에서 티켓을 꺼내거나 판매 요금을 적립하는 일을 스스로 할 수밖에 없다.

이런 식으로 캡슐화를 통해서 객체를 자율적인 존재로 만들 수가 있다.

- 극장은 세부적인 것에 의존하는 것이 아니라 추상적인 것에 의존한다.

![refactoring2](https://user-images.githubusercontent.com/22961251/99396959-4628e980-28da-11eb-9748-ddb5d8af5679.png)

극장은 단순하게 입장만 처리하면 된다. 티켓을 발부하고 금액을 처리하는 것은 판매원의 책임이다.

따라서 판매원의 금액을 처리하는 인터페이스인 `SellTo()` 메서드에 의존한다.

극장은 TicketSeller 내부에 TicketOffice를 포함하고 있다는 것을 알 필요가 없다. 이것은 구현의 영역에 속한다. 

따라서, 결합도를 낮추는 방법은 인터페이스와 구현으로 나누고 인터페이스만을 공개하는 것으로 가능하다.

- 개선된 클래스 다이어그램

![low_coupling_diagram1](https://user-images.githubusercontent.com/22961251/99396653-ea5e6080-28d9-11eb-94e3-0027d137b886.png)

## 코드 개선 2 - 관람객을 자율적인 객체로 만듦

1번의 개선을 통해서 극장의 결합도가 낮아진 것을 확인 할 수 있다. 

그러나, 개선된 코드에도 역시 문제점이 존재한다.

해당 코드를 다시 봐보자.

![refactoring1](https://user-images.githubusercontent.com/22961251/99396946-42956280-28da-11eb-8511-573e5aef648b.png)



과연 판매원이 관람객과 관람객의 가방에 대한 정보를 알 필요가 있을까? 

관람객은 극장과의 의존 관계에서는 풀려났으나 새로운 의존 관계인 판매원이 등장한 셈이다.

이를 아래와 같이 개선 가능할 것이다.

- 캡슐화를 통하여 관람객을 자율적인 객체로 만듦

![refactoring3](https://user-images.githubusercontent.com/22961251/99396962-46c18000-28da-11eb-8a31-6e1c23373e5d.png)

판매원은 관람객이 가방을 가지고 있으며, 그 가방 안에 초대장이 있는지 없는지 가방을 뒤져볼 필요가 없다.

그저, 관람객이 스스로 가방을 확인하여 초대장이 있으면 공짜로 티켓으로 바꿔주고, 아니면 관람객에게 입장료를 받으면 된다.

위의 코드는 그 예시라 볼 수 있을 것이다.

- 판매원은 관람객의 추상적인 인터페이스에 의존한다.

![refactoring4](https://user-images.githubusercontent.com/22961251/99396968-475a1680-28da-11eb-89ea-704da4d073bf.png)

이렇게 함으로써 티켓 판매소에는 판매 금액이 자연스럽게 쌓일 것이며, 관람객은 판매원에게 티켓을 구입하고, 극장은 그저 관람객이 티켓이 존재하면 입장을 시켜줄 뿐이다. 

- 개선된 클래스 다이어그램

![high_cohesion_low_coupling](https://user-images.githubusercontent.com/22961251/99396646-e7637000-28d9-11eb-9da3-cc354eebcfce.png)

# 어떻게 한 것인가

캡슐화를 통하여 자기 자신의 문제를 스스로 해결하도록 코드를 변경한 것이다.

기존 코드는 상세한 내부 구현까지 알고 있어야했으며, 극장은 `Audience` 와 `TicketSeller`에 강하게 결합되어 있었다.

수정한 코드는 극장이 `Audience`와 `TicketSeller` 내부에 직접 접근하지 않는다.

내부에서 처리할 일을 외부의 누군가에게 허용하지 않게 함으로서 가능해졌다.

# 캡슐화와 응집도

위에서 얘기했지만, 수동적인 객체를 능동적이고 자율적인 객체로 만들면서 결합도가 낮춰졌다.

즉, 객체 내부의 상태를 캡슐화하고 객체 간에 오직 메시지를 통해서만 상호작용하게 만드는 것을 통해서 결합도를 낮출 수 있는 것이다. 

밀접하게 연관된 작업만을 수행하고 연관성 없는 작업은 다른 객체에게 위임하는 객체를 가리켜 응집도가 높다고 한다. 우리는 위와 같은 작업을 통하여 **결합도를 낮추고 응집도를 높였다.**

외부의 간섭을 최대한 배제하고, 메시지를 통해서만 협력하는 자율적인 객체들의 공동체를 만드는 것이 훌륭한 객체지향 설계임을 잊지말아야 한다.

# 절차지향과 객체지향

![high_coupling](https://user-images.githubusercontent.com/22961251/99396656-eaf6f700-28d9-11eb-92e5-5b621d9a1706.png)

해당 다이어그램은 절차지향 프로그래밍 방식으로 작성된 코드의 의존성 구조를 보여준다.

위 다이어그램을 통해 모든 처리가 하나의 클래스 안에서 위치하고 나머지 클래스는 단지 데이터의 역할만 수행하고 있음을 알 수 있다.

절차지향 프로그래밍이 변경에 취약한 이유 또한 한 곳에 집중된 의존성 때문에 의존성 문제가 다른 곳으로 전파되기 때문에 발생하기 때문이다.

변경하기 쉬운 설계는 **한 번에 하나의 클래스만 변경**하는 것이다.

이를 어떻게 해결할 수 있을까? 바로 **자신의 데이터를 스스로 처리한 뒤에 다른 곳에서 필요한 부분을 넘겨줌으로서** 해결할 수 있다. 

객체지향 프로그래밍은 **이러한 방식을 데이터와 프로세스가 동일한 모듈 내부에 위치하도록 함**으로써 해결하였다. 이것을 통하여 **객체 내부의 변경이 객체 외부에 전파되지 않도록 제어**할 수 있기 때문에 보다 변경이 수월한 코드를 작성할 수 있다. 

# 책임의 이동

이는 SOILD의 원칙 중 하나인 단일책임원칙(Single Responsibility Principle, 이하 SRP)과 관련이 있다고 볼 수 있다.

기존 Theater는 수 많은 책임을 가지고 있었다. 

개선하기 전 코드를 다시 한번 보자.

```java
if(audience.getBag().hasInvitation()) {
		Ticket ticket = ticketSeller.getTicketOffice().getTicket();
		audience.getBag().setTicket(ticket);
	} else {
		Ticket ticket = ticketSeller.getTicketOffice().getTicket();
		audience.getBag().minusAmount(ticket.getFee());
		ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
		audience.getBag().setTicket(ticket);
	}
}
```

1. `audience.getBag()`을 통하여 관람객의 가방을 확인하는 책임
2. `get.Bag().hasInvitation()`을 통하여 가방을 확인하여 초대장 유무를 확인하는 책임
3. `ticketSeller.getTicketOffice()` 를 통하여 티켓 사무소에 접근하는 책임
4. `getTicketOffice().getTicket()`을 통하여 티켓 사무소에서 티켓 발급을 하는 책임
5. `audience.getBag().setTicket(ticket)` 을 통하여 티켓을 관람객의 가방에 넣어주는 책임

이렇게 수 많은 책임을 도맡아서 하고 있었다.

개선된 코드에서는 어떨까?

```java
//Theater.class
public void enter(Audience audience) {
        ticketSeller.SellTo(audience);
}

//TicketSeller.class 
public void SellTo(Audience audience) {
        ticketOffice.plusAmount(audience.buy(ticketOffice.getTicket()));
}

//Audience.class
public Long buy(Ticket ticket) {
	if(bag.hasInvitation()) {
		bag.setTicket(ticket);
		return 0L;
	} else {
		bag.setTicket(ticket);
		bag.minusAmount(ticket.getFee());
		return ticket.getFee();
	}
}
```

1. 극장은 티켓 판매원에게 관람객을 인계하여 티켓을 구입할 책임을 갖는다. 
2. 티켓 판매원은 관람객에게 티켓을 판매하고 해당 금액을 사무소에 적립하는 책임을 갖는다.
3. 관람객은 자신의 가방을 확인하여 티켓을 교환하거나 구매하는 책임을 갖는다.

좀 더 상세하게 표현하자면, 아래와 같을 것이다.

1. 극장은 판매원에게 관람객이 티켓을 구입하도록 요청한다.
2. 티켓 판매원은 관람객에게 티켓을 살 것을 요청한다.
3. 관람객은 가방을 확인 후 교환할 지 구매할 지 스스로 정한 후 해당 금액을 응답한다.
4. 티켓 판매원은 판매 금액을 사무소에 적립한다.
5. 극장은 관람객이 티켓을 샀다고 가정하고 입장을 시켜준다.

요청과 응답이 연쇄적으로 반응하는 것을 확인 할 수 있다.

이렇게 각 객체에 책임이 적절하게 분배 된 후에 메시지를 통하여 자신이 스스로 처리할 일을 처리한 뒤에 다른 객체가 책임져야 할 일을 위임함으로서 객체의 자율성을 높이고 응집도를 높이고 결합도를 낮출 수가 있다.

# 더 개선하자

[commit - bd9790](https://github.com/dailyworker/the-objects-review/commit/bd979099599d43104dd3a6a2477d643cad8d6c1b)
위의 링크는 개선한 코드를 보여준다.

우리가 `Audience`와 `TicketSeller`를 자율적인 객체로 만들어 준 것 처럼 `TicketOffice`와 `Bag`도 자율적인 객체로 만들어 주었다.

하지만, 여기서는 문제점이 발생한다.

바로 `TicketOffice`에서 `Audience`로에 대한 새로운 의존성이 발생한 것이다.

즉, 트레이드오프의 시점이 온 것이다.
`Audience`에 대한 결합도를 낮추는 방법과 `TitkectOffice`의 자율성 모두를 만족시키는 방법을 찾기가 힘들다.

이 사실을 통하여 다음 두 가지 사실을 알 수 있다.

1. 어떤 기능을 설계하는 방법은 한 가지 이상일 수 있다.
2. 동일한 기능을 한 가지 이상의 방법으로 설계할 수 있기 떄문에 결국 설계는 트레이드오프의 산물이다. 

즉 설계는 균형의 예술이며 적절한 트레이드오프의 결과물이다.