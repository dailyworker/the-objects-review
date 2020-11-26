# 오브젝트 챕터 2 - 영화 예매 애플리케이션 구현하기

## 요구사항

1. 영화(Movie)는 이름(Title)과 가격(Amount)과 할인 정책(DiscountPolicy)을 갖는다.
2. 할인 정책(DiscountPolicy)은 금액 할인 정책(Amount Discount Policy)과 비율 할인 정책(Percent Discount Policy)을 갖고, 할인 조건(DiscountCondition)에 따라 할인 된다. 
3. 할인 조건은 기간 조건(Period Condition)과 순번 조건(Sequence Condition)이 있다.
4. 하나의 영화(Movie)는 다수의 상영(Sceerning)을 가질 수 있다.
5. 하나의 상영(Screening)은 다수의 예매(Reservation)을 가질 수 있다. 

이러한 요구사항을 토대로 영화 예매 애플리케이션을 구현해보자 한다.

## 도메인 다이어그램

![domain](https://user-images.githubusercontent.com/22961251/100318295-b9ef8400-2fb5-11eb-98be-f58ca2c605e1.png)

위의 요구사항으로 도출된 도메인 다이어그램

**도메인이란? 문제를 해결하기 위해 사용자가 프로그램을 사용하는 분야**

객체지향 패러다임이 강력한 이유는 요구사항을 분석하는 초기 단계부터 프로그램을 구현하는 마지막 단계까지 객체라는 동일한 추상화 기법을 사용할 수 있기 때문이다.

요구사항과 프로그램을 객체라는 동일한 관점에서 바라볼 수 있기 때문에 **도메인을 구성하는 개념들이 프로그램의 객체와 클래스로 매끄럽게 연결이 가능**하다.

## 도메인 예시
|   | 영화 제목                             | 할인 정책                        | 할인 조건                                      |
|---|---------------------------------------|----------------------------------|------------------------------------------------|
|   | 아바타 (가격 : 10,000원)              | 금액 할인 정책  (할인액 : 800원) | 순번조건  조조상영                             |
|   |                                       |                                  | 순번 조건  10회 상영                           |
|   |                                       |                                  | 기간 조건 월요일 10:00 ~ 12:00 사이  상영 시작 |
|   |                                       |                                  | 기간 조건 목요일 18:00 ~ 21:00 사이  상영 시작 |
|   | 터미네이터3  (가격: 10,000원)         | 비율 할인 정책 (할인율 : 10%)    | 기간 조건 화요일 14:00 ~ 17:00 사이 상영 시작  |
|   |                                       |                                  | 기간 조건 목요일 10:00 ~ 14:00 사이 상영시작   |
|   |                                       |                                  | 순번 조건 2회 상영                             |
|   | 스타워즈:깨어난 포스(가격 : 10,000원) | 없음                             | 없음                                           |

위와 같은 영화들이 있을 때, 아바타를 예매한다고 가정하면, 할인 조건을 만족할 경우 800원을 할인 해줘야 한다.

사용자가 아래와 같이 예매하면 다음과 같은 결과 값이 나올 수 있을 것이다.

```
예매 요청 : 인원 2명, 2019년 12월 26일 7회 18:00 ~ 20:00

예매 결과 
제    목 : 아바타
상영 정보 : 2019년 12월 26일 7회 18:00 ~ 20:00
인    원 : 2명
정    가 : 20,000원
결제 금액 : 18,400원 
```

## 구현

- 상영(Screening)

```java
public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    public Screening(Movie movie, int sequence, LocalDateTime whenScreened) {
        this.movie = movie;
        this.sequence = sequence;
        this.whenScreened = whenScreened;
    }

    public LocalDateTime getStartTime() {
        return whenScreened;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Money getMovieFee() {
        return movie.getFee();
    }

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }
}
```

상영(Screening) 클래스는 다음을 포함하고 있다.

1. 변수
    - 상영 영화(movie)
    - 상영 순번(sequence)
    - 상영 시작 시간(whenScreened)
2. 메소드
    - 상영 시작 시간 반환(getStartTime())
    - 순번 일치 여부 검사(isSequence())
    - 기본 요금 반환(getMovieFee())

주목해야 할 점은 변수는 `private` 가시성을 가지고, 메소드는 `public` 가시성을 가진다.

훌륭한 클래스 설계는 챕터 1 리뷰에서도 말했지만, 적절한 수준을 캡슐화를 지녀야한다고 했다.

캡슐화를 통해서 **객체의 자율성이 보장된다**고 하였다. 그 뿐만이 아니라 캡슐화는 프로그래머에게 구현의 자유를 제공해준다.

챕터 1에서도 캡슐화에 대해서 정리한 부분이 있었다.

이번에는 좀 더 딥하게 들어가보자 한다.

## 자율적인 객체

1. 객체는 **상태(state)**와 **행동(behavior)**을 가지는 복합적인 존재이다.
2. 객체는 스스로 판단하고 행동하는 **자율적인 존재**이다.

사실 이 두 가지는 서로 깊이 연관되어 있다.

많은 사람들은 객체를 얘기할 때 **"상태와 행동을 함께 포함하는 식별 가능한 단위"** 로 정의한다.

캡슐화란 **우리가 아는 객체처럼 데이터와 기능을 객체 내부로 함께 묶는 것**이다.

이와 더불어 많은 객체지향 프로그래밍 언어들은 상태와 행동을 캡슐화하는 것에 더 나아가 외부에서 접근을 통제할 수 있는 **접근 제어(access control)** 매커니즘도 제공한다. 이는 우리가 알고 있는 `public` `protected` `private` 과 같은 **접근 수정자(access modifier)**들이라고 볼 수 있을 것이다.

객체 내부로 접근을 통제하는 이유는 챕터 1 리뷰를 하면서 적었지만, 객체를 **자율적인 존재로 만들기 위함**이다.

객체가 자율적인 존재가 되기 위해서는 외부의 간섭을 최소화해야 한다.

이렇게 외부의 간섭을 최소화해야 되는 부분 (외부에게 굳이 공개할 필요 없는 것들)을 **구현(implemenatation)**이라 부르고 메시지를 위해서 외부에게 공개해야 되는 부분을 **퍼블릭 인터페이스(public interface)**라 부른다.

객체를 자율적 존재로 만들기 위해서 **인터페이스와 구현의 분리(separation of interface and implementation)** 원칙은 핵심 원칙 중에 하나이다.

적용하기 가장 쉬운 부분은 객체의 상태는 숨기고 행동만 외부로 공개해야 한다라는 것이다. 

즉, 위의 코드는 이러한 인터페이스와 구현의 분리 원칙을 토대로 객체를 자율적인 존재로 만들고 있다.

## 프로그래머의 자유

우리가 회사에서 협업을 해본다 가정하자.

A라는 사람은 API 설계와 B라는 API에서 사용되는 도메인들에 대한 구현을 맡고 있다 가정해보자.

A는 B가 만지고 있는 도메인들의 세부적인 구현에 까지 관심을 가질 필요가 있을까?

B가 이렇게 사용하라고 준 메소드 명세만 알고 있으면 되지 않을까?

B는 혹여나 A가 잘못된 메소드 접근을 통하여 서비스 장애가 나지 않도록 A에게 보여줄 부분만 명세를 하고 나머지는 숨기는 것이 맞을 것이다.

이를 **구현 은닉(implementation hiding)**이라 부른다.

만약 A가 접근하지 말아야 할 부분을 `private` 으로 막아둔다면 A가 설령 접근을 한다해도 컴파일 단에서 에러가 뿜어질 것이다.

이를 통해 A는 내부 구현을 무시한 채 인터페이스만 알고 있어도 클래스를 사용할 수 있으며 개발하는데 알아야 하는 것들을 줄일 수가 있다. B는 인터페이스를 바꾸지 않는 한 외부의 영향을 걱정하지 않고도 내부 구현을 마음대로 변경할 수 있다.

캡슐화는 이렇게 프로그래머에게 자유를 준다고 볼 수 있다.

# 협력하는 객체들의 공동체

```java
public class Screening {
	public Reservation reserve(Customer customer, int audienceCount) {
		return new Reservation(customer, this, calculateFee(audienceCount), audicenceCount);
	}
	private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
  }
}
```

`Screening` 의 `reserve()` 메소드를 보면 `private` 지정자를 가진 `calculateFee()` 를 호출하여 요금을 계산 한 뒤 `Reservation` 생성자에 전달하는 것을 볼 수가 있다.

금액과 관련된 다양한 계산은 `Money` 클래스에서 진행된다. 

```java
public class Money {
    public static final Money ZERO = Money.amountToWon(0);

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money amountToWon(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money amountToWon(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money plus(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money minus(Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqaul(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }
}
```

챕터 1 리뷰를 했을 때 티켓 판매원(TicketSeller)가 티켓을 판매하는 것을 다시 생각해보자.

```java
public void plusAmount(Long amount) {
        this.amount += amount;
}
```

이와 같이 단순하게 `Long` 타입을 선언하여 잔고가 추가되고 빼지는 것을 구현하였다. 그러나 이 자체가 금액과 관련된 의미가 있다라는 것을 전달하기 어려웠다. 하지만, 위와 같이 `Money`  타입을 선언하면, 저장하는 값들이 금액과 관련이 있음을 전달 할 수 있다.

또한, `Money` 타입에서 사용되는 로직들이 한 군데 집약을 되는 것을 가능케한다.

따라서, 의미를 좀 더 명시적이고 분명하게 표현할 수 있으면 객체를 사용하여 해당 개념을 구현할 수가 있다.

이러한 것의 장점은 위에도 설명했지만, 간단하게 정리하면 다음과 같다.

1. 해당 타입에 인간이 이해할 수 있는 개념에 대한 의미를 부여할 수 있음.
2. 해당 타입이 사용하는 로직들을 한 군데로 집약할 수 있음.

이러한 개념을 명시적으로 표현하는 것도 전체적인 설계의 명확성과 유연성을 높일 수 있다.

개념을 명시적으로 표현하여 예약(Reservation) 클래스를 설계하면 다음과 같을 것이다.

```java
public class Reservation {
    private Customer customer;
    private Screening screening;
    private Money fee;
    private int audienceCount;

    public Reservation(Customer customer, Screening screening, Money fee, int audienceCount) {
        this.customer = customer;
        this.screening = screening;
        this.fee = fee;
        this.audienceCount = audienceCount;
    }
}
```

`Reservation` 클래스는 처음 보는 사람도 어떠한 속성들을 갖고 있는지 이해할 수 있으며, 이 객체는 `Customer`, `Screening`, `Money`  와 같은 객체들과 협력을 한다는 것을 확인할 수 있다.

어떻게 협력을 하는지 다음 테스트 코드를 살펴보자.

```java
public class ReservationTest {

    @Test
    public void create_reservation() {
        Customer customer = new Customer("dailyworker");

        Movie avatar = new Movie("아바타",
                Duration.ofMinutes(162),
                Money.amountToWon(10000),
                new NoneDiscountPolicy());

        Screening screening = new Screening(avatar, 
                1, 
                LocalDateTime.of(LocalDate.of(2020, 11, 23), 
                LocalTime.of(10, 23)));        

        Money money = new Money(BigDecimal.valueOf(20000.0));

        Reservation reserve = screening.reserve(customer, 2);

        assertEquals(reserve.getCustomer().getName(), "dailyworker");
        assertEquals(reserve.getScreening(), screening);
        assertEquals(reserve.getAudienceCount(), 2);
        assertEquals(reserve.getFee(), money);
    }
}
```

`Reservation` 객체는 `Screening` 의 `reserve()` 메소드를 통하여 생성이 된다.

이 때, 예약이 된 금액은 `Screening` 의 `calculateFee()` 메소드를 통해 예약 인원 * 예매 가격을 통하여 이뤄진다. 

만약, 할인 조건을 만족하는 경우에는 예약 인원 * 할인된 예매가격으로 매겨 질 것이며, 이러한 일련의 과정들이 `Reservation` 혼자 독단적으로 처리하는 것이 아니라 다른 객체들과의 협력으로 이뤄짐을 알 수 있다.

객체가 다른 객체와 상호작용할 수 있는 유일한 방법은 **메세지를 전송하는 것** 뿐이다.

다른 객체에게 요청이 도착할 때 해당 객체가 메시지를 수신했다고 이야기하며, **수신된 메시지를 자신만의 방법으로 처리하는 것이 메소드**이다.

## 할인 요금 계산을 위한 협력 구현

```java
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private DiscountPolicy discountPolicy;

    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = discountPolicy;
    }

    public Money getFee() {
        return fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscount(screening));
    }
}
```

이 코드에서 주목 해야할 점은   `cllculateMovieFee()` 함수에서 어떠한 할인 정책으로 계산 되는지 모른다는 것이다. 단순하게 `discountPolicy` 에 위임해서 처리하는 것을 볼 수가 있다. 

여기서 볼 수 있는 객체지향 특성은 **추상화**를 통해서 만들어진 **다형성**과 **상속** 이다.

```java
public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition ... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscount(Screening screening) {
        for(DiscountCondition each : conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }

        return Money.ZERO;
    }

    abstract protected Money getDiscountAmount(Screening screening);
}
```

두 가지 할인 정책(AmountDiscountPolicy, PercentDiscountPolicy)는 대부분의 코드가 유사하고, 계산하는 방법만 차이가 있는데 중복을 제거하기 위해 공통 코드를 보관하는 것이 필요하였고, 이것을 **추상 클래스(abstract class)**로 구현하였다.

여기서 할인 조건(DiscountCondition)이 존재하면, 추상 메소드인 `getDiscountAmount()` 메소드를 호출하여 할인 가격을 넘겨준다.

`DiscountPolicy` 는 할인 여부와 요금 계산에 필요한 흐름을 정의하지만, 실제 요금은 추상 메소드에게 위임하는 것을 볼 수가 있다. 실제 할인된 요금은 이 `DiscountPolicy` 클래스를 상속한 자식이 해당 메소드를 오버라이딩하여 구해지는 것이다. 

이를 보고 비슷한 디자인 패턴이 생각 날 수도 있다. 

부모에서 흐름을 정의하고 중간에 필요한 처리를 자식에게 위임하는 디자인패턴 바로, **템플릿 메소드(TEMPLATE METHOD) 패턴**이다.

자 이제, 할인 조건에 대해서 구현해보자.

```java
// 할인 조건 인터페이스 
public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}

// 순번 조건 클래스 
public class SequenceCondition implements DiscountCondition{
    private int sequence;

    public SequenceCondition(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.isSequence(sequence);
    }
}

// 기간 조건 클래스 
public class PeriodCondition implements DiscountCondition {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public PeriodCondition(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.getStartTime().getDayOfWeek().equals(dayOfWeek) &&
                startTime.compareTo(screening.getStartTime().toLocalTime()) <= 0 &&
                endTime.compareTo(screening.getStartTime().toLocalTime()) >= 0;
    }
}
```

위의 할인 조건 클래스들은 상영(Screening) 클래스가 할인 조건을 만족하는지 체크하는 `isSatisFiedBy()` 를 오버라이딩한다. 이를 통하여 상영 객체가 할인 조건을 만족하는지 안하는지를 판별할 수 있다.

할인 정책을 구현해보자.

```java
// 금액 할인 정책
public class AmountDiscountPolicy extends DiscountPolicy {
    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DiscountCondition... conditions) {
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}

//비율 할인 정책
public class PercentDiscountPolicy extends DefaultDiscountPolicy {
    private double percent;

    public PercentDiscountPolicy(double percent, DiscountCondition... conditions) {
        super(conditions);
        this.percent = percent;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return screening.getMovieFee().times(percent);
    }
}
```

여기서 추상클래스의 메소드를 오버라이딩해서 각각의 할인 정책에 맞게 계산하는 것을 볼 수 있다.

테스트 코드는 다음과 같다.

```java
@Test
public void is_adopt_amount_discount_policy() {
    Movie avatar = new Movie("아바타",
                        Duration.ofMinutes(162),
                        Money.amountToWon(10000),
                        new AmountDiscountPolicy(Money.amountToWon(800),
                                new SequenceCondition(1),
                                new SequenceCondition(10),
                                new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
                                new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59))
                        ));

    Screening screening = new Screening(avatar, 1, LocalDateTime.of(LocalDate.of(2020, 11, 23), LocalTime.of(10, 23)));
    Money actual = avatar.calculateMovieFee(screening);
    Money expected = new Money(BigDecimal.valueOf(9200));
    System.out.println("adopted amount discount policy movie fee is : " + actual.toString());
    assertTrue(actual.isGreaterThanOrEqaul(expected));
}

@Test
public void is_adopt_percent_discount_policy() {
	Movie terminator = new Movie("터미네이터 3",
	                                Duration.ofMinutes(109),
	                                Money.amountToWon(10000),
	                                new PercentDiscountPolicy(0.1,
	                                    new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 59)),
	                                    new SequenceCondition(2),
	                                    new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(13, 59))
	                                ));
	
	Screening screening = new Screening(terminator, 2, LocalDateTime.of(LocalDate.of(2020, 11, 26), LocalTime.of(14, 23)));
	Money actual = terminator.calculateMovieFee(screening);
	Money expected = new Money(BigDecimal.valueOf(9000));
	System.out.println("adopted percent discount policy movie fee is : " + actual.toString());
	assertTrue(actual.isGreaterThanOrEqaul(expected));
}
```

# 상속과 다형성 그리고 추상화

## 컴파일 시간 의존성과 실행 시간 의존성

![movie-discount-policy](https://user-images.githubusercontent.com/22961251/100318316-be1ba180-2fb5-11eb-8c0f-e1ff55c8ec64.png)

위에서 구현했던 부분의 클래스 다이어그램은 위와 같다고 볼 수 있다.

`Movie` 는 `DiscountPolicy` 에 의존하는데 영화 할인 금액을 하기 위해서는 `AmountDiscountPolicy` 나 `PercentDiscountPolicy` 의 인스턴스가 필요하지 않을까? 이게 어떻게 가능한 것일까?

개발자가 코드가 작성하는 시점에는 영화 객체는 금액 할인 정책 객체나 비율 할인 정책 객체를 모른다.

그렇다면 어떻게 실행하는 시점에 알 수가 있을까? Movie의 인스턴스 생성 시점을 다시 보자.

```java
// Movie의 생성자 
public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
  this.title = title;
  this.runningTime = runningTime;
  this.fee = fee;
  this.discountPolicy = discountPolicy;
}

// Movie 생성
Movie terminator = new Movie("터미네이터 3",
	Duration.ofMinutes(109),
  Money.amountToWon(10000),
	new PercentDiscountPolicy(0.1, ...));
```

즉, 영화를 생성하는 시점에서 의존성이 결정된다. 이는 런타임 의존성이라고 볼 수 있다. (실행 시점에 의존성이 결정되기 때문에)

우리가 알아야 할 것은 **코드의 의존성과 실행 시점의 의존성**이 다를 수 있다는 점이다. 그렇다면 두 의존성이 다르면 다를 수록 좋을까? 이것도 트레이드오프 관계에 놓여져있다고 볼 수 있다.

코드의 의존성과 실행 시점의 의존성이 같으면 같을 수록 디버깅이나 이해하기 편할 것이다. 반면에,  달라질 수록 코드뿐만 아니라 객체를 생성하고 연결하는 부분을 찾은 다음에 확인해야될 것이다.

그렇다면, `Movie` 생성자에 타입은 `DiscountPolicy` 라 명시되어 있는데 어째서, `AmountDiscountPolicy` 나 `PercentDiscountPolicy` 의 인스턴스를 전달할 수 있을까?

```java
//Movie가 discountPolicy에 위임하여 할인 금액을 계산하는 부분
public Money calculateMovieFee(Screening screening) {
  return fee.minus(discountPolicy.calculateDiscount(screening));
}
```

영화 객체는 자신과 협력하는 객체가 어떤 클래스의 인스턴스인지가 중요한 것이 아니라 `calculateDiscount()` 이 메시지를 수신할 수 있다는 것이 중요한 것이다. 따라서, 이 메시지를 수신할 수 있는 `AmountDiscountPolicy` 나 `PercentDiscountPolicy` 와 협력이 가능한 것이다.

즉, 자식 클래스는 상속을 통해 부모 클래스의 인터페이스(여기서는 public method를 의미)를 물려 받기 때문에 부모 클래스 대신 사용할 수 있으며, 우리 예시와 자식 클래스가 부모 클래스를 대신하는 것을 **업캐스팅(upcasting)**이라 한다.

다시 정리해보자, 코드 상에서는 `DiscountPolicy` 클래스에게 메시지를 전송하지만, 실제 실행하는 시점에서 어떤 클래스와 협력할지 결정된다. 즉, 동일한 메시지를 전송하지만 실제로 어떤 메서드가 실행될 지는 메시지를 수신하는 객체의 클래스에 따라 달라진다. 이를 **다형성**이라한다.

즉, 다형성은 동일한 메시지를 수신했을 때 객체 타입에 따라 다르게 응답하는 능력이라 볼 수 있다.

런타임 시점에서 메서드를 바인딩한다하여 이를 지연 바인딩(Lazy Binding) 혹은 동적 바인딩(Dynamic Binding)이라 한다. 컴파일 시점에서 메서드를 바인딩하는 것은 초기 바인딩(Early Binding) 혹은 정적 바인딩(Static Binding) 이라 한다.

## 추상화의 힘

위에서는 다형성과 상속에 대해서 다뤄봤다.

다형성과 상속은 결국은 추상화으로 이뤄지는데 추상화를 통해 얻을 수 있는 이득은 다음과 같다.

1. 추상화 계층만 따로 보면 세부적인 내용을 무시한 채 상위 정책을 쉽고 간단하게 표현 가능
2. 좀 더 유연한 설계가 가능

1번의 경우에는 상위 개념만으로도 도메인의 중요한 개념을 설명할 수 있다고 생각하면 될 것 같다.

이를 통하여, 상위 정책을 추상화를 사용하여 표현하면 기존 구조를 수정하지 않고, 새로운 기능을 쉽게 추가할 수 있다. 

어떻게 기존 구조를 수정하지 않고, 새로운 기능을 쉽게 추가할 수 있을까? 

예시를 통해 확인해보자.

```java
public Money calculateMovieFee(Screening screening) {
  if(discountPolicy == null){
      return fee;
  }

  return fee.minus(discountPolicy.calculateDiscount(screening));
}
```

여기서 보면 알 수 있듯이 `discountPolicy == null` 인 경우에는 해당 영화의 금액을 리턴해준다.

하지만, 이 코드는 지금까지 해왔던 협력의 일관성을 무너트리게 된다. 

할인 금액 0원이라는 책임이 `Movie` 에 있기 때문이다. 

이 문제를 해결하기 위해서 다음과 같이 수정한다.

전체 코드 - [commit 43901a](https://github.com/dailyworker/the-objects-review/commit/43901a640c0db15169c23676feaa42dd8fb62739)

1. 기존 DiscountPolicy 인터페이스화  

```java
public interface DiscountPolicy {
    Money calculateDiscount(Screening screening);
}
```

2. 기존 추상클래스를 DefaultDiscountPolicy로 옮겨옴

```java
public abstract class DefaultDiscountPolicy implements DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DefaultDiscountPolicy(DiscountCondition ... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscount(Screening screening) {
        for(DiscountCondition each : conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }

        return Money.ZERO;
    }

    abstract protected Money getDiscountAmount(Screening screening);
}
```

3. 할인 금액이 없는 경우의 할인 정책을 추가함

```java
public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public Money calculateDiscount(Screening screening) {
        return Money.ZERO;
    }
}
```

4. 기존 Movie 메소드를 수정함.

```java
public Money calculateMovieFee(Screening screening) {
  return fee.minus(discountPolicy.calculateDiscount(screening));
}
```

이처럼 추상화를 중심으로 코드의 구조를 설계하면 유연하고 확장 가능한 설계를 만들 수 있다.

이것이 가능한 이유는 너무 세부적인 것에 의존을 하지 않기 때문이다. 

`Movie` 는 특정 할인 정책에 얽매여있지 않으며, `DiscountPolicy` 도 어떠한 특정 할인 조건에 얽매여있지 않다.

위의 코드에서 고민할 수 있는 부분은 아주 단순히 `NoneDiscountPolicy` 때문에 인터페이스를 사용한 부분이다.  이것은 트레이드오프의 대상이 될 수 있다. 이 책의 저자는 말한다. 사소한 결정이라도 항상 고민하고 트레이드오프하라고 말이다. 

비록 지금은 1개의 메소드만 갖고 있는 인터페이스지만, 나중에 확장을 생각해서 보다 나은 구조라고 나는 생각한다.

- 인터페이스로 빼기전 클래스 다이어그램

![only-use-abstract-class](https://user-images.githubusercontent.com/22961251/100318312-be1ba180-2fb5-11eb-800f-6be93ef701d4.png)

- 인터페이스로 뺀 후 클래스 다이어그램

![adopt-abstact-class-with-interface](https://user-images.githubusercontent.com/22961251/100318308-bcea7480-2fb5-11eb-9df3-9873cb719f3f.png)


## 합성과 상속

흔히들 자바에서 코드 재사용을 위해 상속보다는 **합성(Composition)**을 사용하라 한다. 

합성은 **다른 객체의 인스턴스를 자신의 인스턴스 변수로 포함해서 재사용하는 것**을 말한다.

우리는 이미 사용하고 있는데 바로 영화와 할인 정책의 관계이다.

```java
public class Movie {
private String title;
private Duration runningTime;
private Money fee;
private DiscountPolicy discountPolicy;

public Movie(String title, Duration runningTime, Money fee, DiscountPoilcy discountPoilcy) {
    this.title = title;
    this.runningTime = runningTime;
    this.fee = fee;
    this.discountPoilcy = discountPoilcy;
	}
...
}
```

하지만 이를 상속으로도 풀어낼 수가 있다. 

예를 들면 `AmountDiscountMovie` 나 `PercentDiscountMovie` 등으로 상속해서 말이다.

하지만, 이럴 경우에 단점이 있다.

1. 캡슐화 위반
2. 유연한 설계 방해

1번부터 보자, 만약 `AmountDiscountMovie` 라는 클래스를 만든다 가정하면, 부모 클래스의 내부 구조를 잘 알고 있어야 한다. `calculateMovieFee()` 안에 추상 메소드인 `calcuateDIscount()` 가 호출된다는 사실과 같은 것들을 말이다.

결과적으로 부모 클래스의 구현이 자식 클래스에게 노출되기 때문에 캡슐화가 약화된다.

2번의 경우에는 만약 `AmountDiscountMovie` 의 인스턴스를 `PercentDiscountMovie` 로 변경하고자 할 때에 나타난다. 대부분의 언어에서 이미 생성된 객체의 클래스를 변경하는 기능은 지원하지 않는다.

따라서, `PercentDiscountMovie` 인스턴스 생성한 후 `AmountDiscountMovie` 의 상태를 복사하는 방법밖에 없다. 이것은 부모와 자식 클래스가 강결합이 되어있기 떄문에 발생한다.

반면, 인스턴스 변수로 연결한 기존 방법을 사용하면 아주 쉽게 가능하다.

```java
@Test
    public void is_change_discount_policy_in_movie() {
        Movie avatar = new Movie("아바타",
                Duration.ofMinutes(162),
                Money.amountToWon(10000),
                new AmountDiscountPolicy(Money.amountToWon(800),
                        new SequenceCondition(1),
                        new SequenceCondition(10),
                        new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
                        new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59))
                )
        );

        avatar.changeDiscountPolicy(
                new PercentDiscountPolicy(0.1,
                        new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 59)),
                        new SequenceCondition(2),
                        new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(13, 59))
                )
        );

        Screening screening = new Screening(avatar, 2, LocalDateTime.of(LocalDate.of(2020, 11, 26), LocalTime.of(14, 23)));
        Money actual = avatar.calculateMovieFee(screening);
        Money expected = new Money(BigDecimal.valueOf(9000));
        System.out.println("adopted percent discount policy movie fee is : " + actual.toString());
        assertTrue(actual.isGreaterThanOrEqaul(expected));
    }
```

위 처럼 `Movie` 내부에 아래와 같은 단순한 메서드 추가로 해결이 가능해진다.

```java
public void changeDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
```

합성은 인터페이스에 정의된 메시지를 통해서만 코드를 재사용하는 방법이라 볼 수 있다.

이 때문에 상속이 갖고 있는 문제를 해결할 수 있다.

1. 인터페이스에 정의된 메시지를 통해서만 재사용이 가능 → 구현을 효과적으로 캡슐화
2. 의존하는 인스턴스를 교체하는 것이 비교적 쉬움 → 보다 유연한 설계 가능

따라서, 코드 재사용성을 위해서 상속보다 합성을 사용하는 것이 좋다.

그렇다면 상속은 무조껀 지양해야 되는 기능일까? 그것은 아니다.

우리 코드도 추상 클래스를 활용해서 짰지않았는가?

즉, **다형성을 위해 인터페이스를 재사용하는 경우에는 상속을 사용**해도 되는 것이다.

정리를 하자면, 코드의 재사용성을 위해서는 합성을 선호하고, 다형성을 위해 인터페이스를 재사용하는 경우에는 상속과 합성을 적절하게 조합해서 사용하는 것이다.