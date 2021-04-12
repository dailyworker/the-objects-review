# 오브젝트 - 챕터4 설계 품질과 트레이드오프

# 개요

개발 공부를 하면서 느낀 점은 클론 코딩을 하면서 배우는 점도 많지만 내가 개판으로 짠 (혹은 이상하게 짠?) 소스를 고치면서 보면서 배우는 점도 많다는 점이다.

이번 챕터에서는 나는 정말 많은 부분들을 얻었다. 해당 챕터의 내용은 **설계 품질과 트레이드오프**과 관련된 내용이다.

## 트레이드 오프란?

굳이 디자인 패턴이 쓰일 필요가 없는 기존에 잘 운영되던 서비스가 있다고 가정하자. 

 이 서비스를 개선하기 위해서 나는 디자인 패턴을 도입해야된다고 말했다. 그래서 디자인 패턴으로 바꿀 수 있는 부분들은 다 바꿨다. 이 서비스 자체는 디자인 패턴이 도입이 되어 있지 않아서 결합도가 높은 시스템이었다. 한달동안 밤샘 작업까지 하면서 이 서비스를 통째로 뜯어고치고 디자인 패턴을 도입하였다. 

하지만 운영 시에 기존 코드에 익숙한 개발자들과의 의사소통의 어려움 그리고 유지보수와 디버깅의 어려움들이 많았다. 하지만 개발자들에게 설명을 해주면서 서로서로 공부를 한 덕에 6개월 뒤에는 모두 적응을 했다.

위의 예시는 극단적인 비유를 들기 위한 예시이다. 과연 유지보수를 위해서 6개월이나 소요되는 프로젝트를 만들 필요가 있었을까 **소잡는 칼로 닭을 잡으려고 했다는 행위**라 볼 수 있다. 

삼천포로 잠깐 빠지자면 디자인 패턴 바이블 중 하나로 꼽히는 'Head First Design Patterns'에서 이와 같은 행위를 패턴 중독이라 한다. 책이 있는 분들은 634 ~ 636페이지를 참고해보자. 없으신 분들은 [https://ppss.kr/archives/87970](https://ppss.kr/archives/87970) 링크를 참고해보자.

즉, 트레이드 오프는 선택 사항 양자가 서로 상충하는 관계에 있는 것을 의미한다.

예시를 들었던 내용은 다음 이해 관계가 상충한다고 보면 된다. 

- 기존 서비스
    - 운영에는 문제가 없지만 난잡한 설계
    - 디자인이 단순하고 기존 개발자들이 적응한 아키텍처라서 유지보수가 쉽다.

- 디자인패턴으로 갈아엎은 서비스
    - 깔끔하지만 복잡한 설계
    - 초기 기존 개발자들과의 의사소통의 어려움, 유지보수의 어려움

디자인패턴 뿐만 아니라 이러한 이해 관계는 개발하는 동안 여기저기 녹아져있다고 생각이 든다.

애자일 방식으로 업무를 진행하는게 좋은 프로젝트가 있을 수 있고, 폭포수 방식으로 업무를 진행하는 것이 좋은 프로젝트가 좋은 프로젝트가 존재할 것이다. 

항상 개발자는 이러한 이해 관계안에서 자신의 상황과 프로젝트 마감 기한 등을 고려하여 선택의 연속을 한다고 생각한다. 

오브젝트의 챕터 4는 그러한 내용을 지금까지 우리가 객체지향의 지향점을 코드에 녹여낼려고 했던 연습과 반대로 데이터 중심적인 사고에 대한 예제로 했을 때 어떠한 문제점을 갖고 있을지에 대한 내용과 설계를 하면서 부딪힐 수 있는 트레이드오프에 대한 설명이 담겨있는 챕터이다.

## 오브젝트 챕터 4 - 설계 품질과 트레이드오프

> 객체지향 설계란 올바른 객체에게 바른 책임을 할당하면서 낮은 결합도와 높은 응집도를 가진 구조를 창조하는 활동이다.

설계는 변경을 위해 존재하고 변경에는 어떤 식으로든 비용이 발생한다. 여기서 비용은 시간과 인건비 등과 같은 것들을 포함한다. 그렇다면 훌륭한 설계란 무엇일까? 

- 훌륭한 설계란 합리적인 비용 안에서 변경을 수용할 수 있는 구조를 만드는 것이다.

이런 합리적인 비용안에서 쉽게 변경할 수 있는 설계는 우리가 이미 소프트웨어공학과 같은 수업에 배웠듯 `High Cohesion Low Coupling` 을 통해 만들어낼 수 있다. 

이런 원칙을 유지하려면 챕터 1, 2장에서도 책에서 강조하는 내용이지만 **객체의 상태가 아니라 객체의 행동에 초점을 맞추는 것이 중요**하다.

이러한 장점을 이해하기 위해 오히려 이 책에서는 반대의 설계방법인 `데이터 중심의 설계` 를 통한 예제를 들고 있다.

# 캡슐화, 응집도 그리고 결합도

## 캡슐화

우리가 상태(`state`)와 행위(`behavior`)를 하나의 객체 안에 모으는 이유는 객체의 내부 구현을 외부로부터 감추기 위해서이다.

여기서 **구현**은 나중에 변경될 가능성이 높은 어떠한 것을 의미한다.

객체지향이 강력한 이유는 이러한 **캡슐화**를 사용하여 한 곳에 집약을 시켜서 변경을 통한 사이드 이펙트가 전체 시스템에 영향을 끼치지않고, 조절할 수 있는 장치를 제공한다.

여기서, 변경이 될 가능성이 높은 부분을 **구현** 이라 부르고, 상대적으로 안정된 부분을 **인터페이스**라 부른다.

캡슐화는 변경의 정도에 따라 구현과 인터페이스를 분리한 후 외부에서는 상대적으로 안정적인 인터페이스에만 의존하도록 관계를 조절하는 방법이라 볼 수 있다.

객체지향 4대 특성은 흔히 `캡(슐화) 상(속) 추(상화) 다(형성)` 라고 말하는데 캡슐화 또한 큰 범위의 **추상화**라고 볼 수 있다.

그 이유는 변경 가능성이 높은 부분을 개체 내부로 숨김으로써 대상을 단순화하기 때문이다.

따라서, 캡슐화는 복잡한 구현은 내부로 숨기는 추상화 기법이며 변경될 수 있는 어떠한 것이라도 캡슐화를 하는 것이 객체지향 설계의 핵심이다.

그렇다면 우리가 SW공학시간에 배웠던 응집도와 결합도는 정확히 어떤 의미를 나타낼까? 

## 응집도(Cohesion)와 결합도(Coupling)

- 응집도(Cohesion)

모듈에 포함된 내부 요소들이 연관돼 있는 정도를 나타낸다.

**모듈 내의 요소들이 하나의 목적을 위해 긴밀하게 협력하면 그 모듈은 높은 응집도를 가진다 볼 수 있다.**

- 결합도(Coupling)

의존성의 정도를 나타내며 다른 모듈에 대해서 얼마나 알고 있는지를 나타내는 척도이다.

**어떤 모듈이 다른 모듈에 대해 너무 자세한 부분까지 알고 있다면 두 모듈은 높은 결합도를 가진다 볼 수 있다.**

위 내용은 아마 SW공학시간에도 다뤘던 응집도와 결합도에 대한 정의라고 볼 수 있을 것이다.

나 뿐만 아니라 학부생 시절 다른 친구들도 이 부분을 개념적으로만 이해하고 몸에 와닿는다는 느낌은 못받았던 것으로 기억한다.

이 책에서는 응집도와 결합도를 아래와 같이 변경의 관점에서 설명한다.

> 응집도는 변경이 발생할 때 모듈 내부에서 발생하는 변경의 정도

위의 얘기는 하나의 변경을 수용하기 위해 모듈 전체가 함께 변경된다면 응집도가 높고, 일부만 변경된다면 낮다는 뜻이다. 또한, 하나의 변경에 대해 하나의 모듈만 변경되는 것이 아니라 다수의 모듈이 함께 변경되어야한다면 응집도가 낮은 것이라 얘기하고 있다.

아래의 그림을 살펴보자.

![cohesion](https://user-images.githubusercontent.com/22961251/114389258-504acd80-9b84-11eb-8db3-646383db2a54.png)

왼쪽은 높은 응집도의 경우고 오른쪽은 낮은 응집도의 경우를 도표로 표기한 것이다.

높은 응집도의 경우 하나의 요구사항을 변경하기 위해서 하나만 수정하면 된다.

그러나, **낮은 응집도를 갖는 경우 하나의 요구사항을 변경하기 위해서 다른 모듈까지 동시 수정**해야한다.

즉, 높은 응집도의 모듈을 설계한 경우 수정되는 부분을 파악하기 위해 다른 모듈을 찾을 필요 없이 하나의 모듈만 수정할 수 있게 되는 것이다. 

그렇다면 결합도는 어떤 것일까? 

> 결합도는 한 모듈이 변경되기 위해서 다른 모듈의 변경을 요구하는 정도

위의 얘기는 하나의 모듈을 수정할 때 얼마나 많은 모듈을 함께 수정해야 하는지 나타낸다. 따라서, 결합도가 높으면 높을수록 함께 변경하는 모듈의 수가 늘어나기 때문에 변경하기가 어려워진다. 

아래의 그림을 참고하자.

![coupling](https://user-images.githubusercontent.com/22961251/114389262-517bfa80-9b84-11eb-89ad-6b01f5a591cc.png)

왼쪽의 경우가 낮은 결합도고, 오른쪽이 높은 결합도를 가지는 모듈을 나타낸 것이다. 

위와 같이 A라는 모듈을 변경하기 위해서 A 모듈에 의존을 하는 다른 모듈까지 수정해야되는 것이 높은 결합도를 가질 때 나타나는 문제라 볼 수 있다. 

또한, 결합도는 영향을 모듈의 수 외에도 **변경의 원인**을 이용해 개념을 설명할 수 있다.

**내부 구현을 변경했을 때 이것이 다른 모듈에 영향을 미치는 경우에는 두 모듈 사이의 결합도가 높다고 표현한다.**

**퍼블릭 인터페이스를 수정했을 때만 다른 모듈에 영향을 미치는 경우는 결합도가 낮다고 표현한다.**

따라서, 캡슐화에서 다뤘던 내용과 같이 내부 구현에 의존하는 것이 아니라 다른 모듈이 인터페이스에 의존하게끔 코드를 작성해서 결합도를 낮출 수가 있다. 

응집도와 결합도 내용을 정리하자면 `High Cohesion Low Coupling` 을 얻기 위해서는 캡슐화가 중요하다고 볼 수 있다. 캡슐화를 잘 지킬 경우 모듈 안의 응집도는 높아지고, 모듈 사이의 결합도는 낮아진다. 

그러나, 캡슐화를 위반하면 모듈 안의 응집도는ㄴ 낮아지고 모듈 사이의 결합도는 높아진다.

해당 내용의 인사이트를 갖고 데이터 중심의 영화 예매 시스템의 문제점을 살펴보자.

# 데이터 중심의 영화 예매 시스템의 문제점

## 캡슐화 위반

```java
public class Movie {
	... (중략) ...
	public Money getFee() {
	    return fee;
	  }
	
	public void setFee(Money fee) {
	  this.fee = fee;
	}
		... (중략) ...
}
```

위의 코드는 객체 내부에 접근할 수 없기 때문에 캡슐화의 원칙을 지키는 것과 같이 보이지만, `getFee()`와 `setFee()` 메서드를 통해서 Movie 내부에 Money 타입의 fee라는 인스턴스 변수가 존재한다는 사실을 노골적으로 드러내고 있다. 

이 원칙을 어기게 된 근본적인 원인은 **객체가 수행할 책임이 아니라 내부에 저장할 데이터에 초점을 맞췄기 때문이**

**다.** 즉, 설계를 할 때 협력에 관해 고민하지 않으면 캡슐화를 위반하는 과도한 접근자와 수정자를 가지게 되는 경향이 존재하며, 내부 구현이 퍼블릭 인터페이스에 그대로 노출되는 것과 같은 현상이 나타난다. 

## 높은 결합도

```java
public class ReservationAgency { 
	public Reservation reserve(Screening screening, Customer customer, int auidenceCount) {
		... (중략) ...
		Money fee;
		if(discountable) {
			fee = movie.getFee().minus(discountedAmount).times(audienceCount);
		} else {
			fee = movie.getFee();
		}
	}
}
```

위의 코드는 한 명의 예매 요금을 계산하기 위해 Movie의 `getFee()` 메서드를 호출한 후 계산된 결과를 다시 Money 타입의 `fee` 에 저장하는 코드이다. 

이때 만약 `fee` 의 타입이 변경된다고 가정해보면 `getFee()` 의 리턴 타입까지 영향을 받게된다. 

그리고 이를 호출하는 `ReservationAgency` 구현도 변경된 타입에 맞게 수정되어야한다. 

또 다른 문제점으로 여러 객체들이 하나의 제어 로직에 집중되어 하나의 제어 객체가 다수의 객체가 강하게 결합된다는 것이다. 

`ReservationAgency.reserve()` 제어 로직을 살펴보면 `DiscountCondition` 이 변경되면 `reserve()` 메서드도 수정되어야하고, `Screening` 객체가 변경되면 이 또한 수정되어야한다.

## 낮은 응집도

`ReservationAgency` 를 예로 들어 변경과 응집도 사이의 관계를 살펴보고자하는데 아래의 변경사항이 생기는 경우 `ReservationAgency` 는 코드를 수정해야할 것이다.

1. 할인 정책이 추가될 경우
2. 할인 정책별로 할인 요금을 계산하는 방법이 변경될 경우
3. 할인 조건이 추가되는 경우
4. 할인 조건별로 할인 여부를 판단하는 방법이 변경될 경우
5. 예매 요금을 계산하는 방법이 변경될 경우

낮은 응집도를 가질 경우 위의 변경사항을 통해서 발생하는 사이드 이펙트는 다음과 같다.

- **변경과 아무 상관이 없는 코드들이 영향을 받게 된다.**

    `ReservationAgency` 안에 할인 정책을 선택하는 코드와 조건을 판단하는 코드가 함께 존재하기 때문에 새로운 할인 정책을 추가하는 작업이 여기에도 영향을 끼칠 수 있다. 

- **하나의 요구사항을 반영하기 위해 동시에 여러 모듈을 수정해야한다.**

       새로운 할인 정책을 추가한다고 생각해 보자. 

       `MovieType` 열거형 값을 추가하고 `ReservationAgency.reserve()` 메서드에 `switch-case` 부분에 

       해당 부분을 추가해야할 것이다. 하나의 요구사항을 변경하기 위해 `MovieType, ReservationAgency,` 

       `Movie` 세 개의 클래스를 함께 수정해야한다.

자 이제 이러한 문제점을 수정해 보자.

# 자율적인 객체를 향해

## 스스로 자신의 데이터를 책임지는 객체

예전에도 얘기했지만, 우리가 상태와 행동을 객체라는 하나의 단위로 묶는 이유는 스스로 자신의 상태를 처리하기 위함이라고 하였다.

객체는 단순한 데이터 제공자가 아니다. 객체 내부에 저장되는 데이터보다 객체가 협력에 참여하면서 수행할 책임을 정의하는 오퍼레이션이 더 중요하다.

따라서, 설계를 진행할 때 "이 객체가 어떤 데이터를 포함해야 하는가?"에 다음과 같은 두 개의 질문으로 분리해야한다.

1. 이 객체가 어떤 데이터를 포함해야 하는가? 
2. 이 객체가 데이터에 대해 수행해야 하는 오퍼레이션은 무엇인가? 

이 두 질문을 조합하면 **객체 내부 상태를 저장하는 방식과 저장된 상태에 대해 호출할 수 있는 오퍼레이션의 집합을 얻을 수 있다.** 

이 인사이트를 통해서 `ReservationAgency` 로 새어나간 데이터에 대한 책임을 스스로가 수행할 수 있게 해보자.

- DiscountCondition

먼저 첫번째로 할인 조건을 표현하는 `DiscountCondition` 에서 시작하자.

1. 이 객체가 어떤 데이터를 포함해야하는가?

    이 부분은 이미 우리가 초기에 정했었다.

    ```java
    private DiscountConditionType type;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    ```

2.  이 객체가 데이터에 수행해야하는 오퍼레이션은 무엇인가?

이제부터가 핵심인데 우리는 복잡했던 `ReservationAgecy.reserve()` 에 새어나간 책임을 원래대로 돌려놓을 생각이다.

`DiscountCondition` 은 `순번 조건(DiscountConditionType.SEQUENCE)` 과 `기간 조건(DiscountConditionType.PERIOD)` 로 나눠진다. 

따라서 두 가지 할인 조건을 판단할 수 있게 `isCountable()` 메소드를 다음과 같이 구현하였다.

```java
public boolean isCountable(DayOfWeek dayOfWeek, LocalTime time) {
    if(type != DiscountConditionType.PERIOD) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return this.dayOfWeek.equals(dayOfWeek) &&
        this.startTime.compareTo(time) <= 0 &&
        this.endTime.compareTo(time) >= 0;
  }

  public boolean isCountable(int sequence) {
    if(type != DiscountConditionType.SEQUENCE) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return this.sequence == sequence;
  }
```

위와 같은 코드로 변경함으로써 할인 여부를 토대로 할인 조건에 맞게 판단할 수 있게 처리할 수 있다. 

- Movie
1. 이 객체가 어떤 데이터를 포함해야하는가?

    `Movie` 또한 이미 구현을 해놨었다. 

```java
private String title;
private Duration runningTime;
private Money fee;
private List<DiscountCondition> discountConditions;

private MovieType movieType;
private Money discountAmount;
private double discountPercent;
```

 2.  이 객체가 데이터에 수행해야하는 오퍼레이션은 무엇인가?

`Movie` 가 포함하는 데이터를 보면 영화 요금을 계산하는 오퍼레이션과 할인 여부를 판단하는 오퍼레이션이 필요함을 데이터를 보고 알 수 있다.

먼저, 계산하기 위한 오퍼레이션을 구현하고자 한다. 

중요한 점은 `Movie` 는 3가지 할인 정책을 갖는다는 것이다. (금액, 비율, 미적용) 따라서, `DiscountCondition` 과 같이 할인 정책의 타입을 반환하는 메서드와 정책별로 요금을 계산하는 부분이 필요할 것이다. 

```java
public MovieType getMovieType() {
    return movieType;
  }

  public Money calculateAmountDiscountedFee() {
    if(movieType != MovieType.AMOUNT_DISCOUNT) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return fee.minus(discountAmount);
  }

  public Money calculatePercentDiscountedFee() {
    if(movieType != MovieType.PERCENT_DISCOUNT) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return fee.minus(fee.times(discountPercent));
  }

  public Money calculateNoneDiscountFee() {
    if(movieType != MovieType.NONE_DISCOUNT) {
      throw new IllegalArgumentException("올바르지 않은 값입니다.");
    }
    return fee;
  }

  public boolean isDiscountable(LocalDateTime whenScreened, int sequence) {
    for(DiscountCondition condition : discountConditions) {
      if (discountableByCondition(whenScreened, sequence, condition)) {
        return true;
      }
    }
    return false;
  }

  private boolean discountableByCondition(LocalDateTime whenScreened, int sequence, DiscountCondition condition) {
    if(condition.getType() == DiscountConditionType.PERIOD) {
      return condition.isCountable(whenScreened.getDayOfWeek(), whenScreened.toLocalTime());
    } else {
      return condition.isCountable(sequence);
    }
  }
```

 위와 같이 구현할 수 있을 것이다.

- Screening

`Screening` 은 한번에 표기해보도록 하겠다.

```java
private Movie movie;
private int sequence;
private LocalDateTime whenScreened;

public Screening(Movie movie, int sequence, LocalDateTime whenScreened) {
    this.movie = movie;
    this.sequence = sequence;
    this.whenScreened = whenScreened;
}

public Money calculateFee(int audienceCount) {
switch (movie.getMovieType()) {
    case AMOUNT_DISCOUNT:
      if(movie.isDiscountable(whenScreened, sequence)) {
        return movie.calculateAmountDiscountedFee().times(audienceCount);
      }
      break;
    case PERCENT_DISCOUNT:
      if(movie.isDiscountable(whenScreened, sequence)) {
        return movie.calculatePercentDiscountedFee().times(audienceCount);
      }
      break;
    case NONE_DISCOUNT:
      return movie.calculateNoneDiscountFee().times(audienceCount);
  }
  return movie.calculateNoneDiscountFee().times(audienceCount);
}
```

위를 잠깐 설명하자면, `Screening` 은 영화와 몇번째 상영 순번인지와 언제 상영하는지에 대한 정보를 갖고 있다. 

이를 통해 `Movie` 가 금액 할인 정책이나 비율 할인 정책을 지원할 경우 이를 확인하여 요금을 계산하게 한다. 

위와 같은 과정을 거치니 `ReservationAgency.reserve()` 는 아래와 같이 변화한다.

```java
public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
    Money fee = screening.calculateFee(audienceCount);
    return new Reservation(customer, screening, fee, audienceCount);
}
```

매우 깔끔해졌고 객체들이 각자의 책임을 수행할 수 있도록 변경되었다.

하지만, 아직도 부족한 부분이 존재한다.

# 여전히 부족하다

첫 번째 설계보다 향상된 것은 사실이고 만족할 수 있는 수준까지 올라왔다고 생각할 수 있다.

하지만, 대부분의 문제는 두번째 설계에서도 여전히 발생한다.

## 캡슐화 위반

분명 수정된 객체들은 자신의 데이터를 스스로 처리한다. 

`DiscountCondition` 은 자기 자신의 데이터를 이용해 할인 가능 여부를 스스로 판단한다.

```java
private DiscountConditionType type;
private int sequence;
private DayOfWeek dayOfWeek;
private LocalTime startTime;
private LocalTime endTime;

public DiscountConditionType getType() { ... }
public boolean isCountable(DayOfWeek dayOfWeek, LocalTime time) { ... }
public boolean isCountable(int sequence) { ... }
```

위의 소스에서 우리가 중점으로 봐야할 메서드는 `isCountable()` 이다.

기간 조건을 판단하는 `isCountable(DayOfWeek dayOfWeek, LocalTime time)` 메서드의 시그니처를 자세히 살펴보면 `DiscountCondition` 에 속성으로 포함돼 있는 `DayOfWeek` 타입의 요일 정보와 `LocalTime` 타입의 시간 정보를 파라미터로 받는 것을 알 수 있다.

즉, 이 메서드 시그니처를 통해서 `DayOfWeek` 타입의 요일 정보와 `LocalTime` 타입의 시간 정보가 **인스턴스 변수로 포함돼 있다는 사실을 인터페이스를 통해 외부에서 노출**하고 있다. 

두 번째 `isCountable(int sequence)` 메서드 역시 객체가 int 타입의 순번 정보를 포함하고 있음을 **외부에 노출**한다.

비록 `setType` 메서드는 없지만 `getType` 메서드를 통해서 내부에 `DiscountConditionType` 포함하고 있다는 정보 역시 노출시키고 있다. 

이것이 왜 문제가 되는가? 

만약 속성을 변경해본다 가정해보자. 아마도 두 `isCountable` 메서드의 파라미터를 수정하고 해당 메서드를 사용하는 모든 클라이언트도 함께 수정해야한다. 내부 구현의 변경이 외부로 퍼져나가는 **파급 효과(ripple effect)**는 캡슐화가 부족하다는 증거이다.

`Movie` 역시 캡슐화가 부족하기는 마찬가지이다.

```java
public class Movie {
  private String title;
  private Duration runningTime;
  private Money fee;
  private List<DiscountCondition> discountConditions;

  private MovieType movieType;
  private Money discountAmount;
  private double discountPercent;

  public MovieType getMovieType() { ... }
  public Money calculateAmountDiscountedFee() { ... }
  public Money calculatePercentDiscountedFee() { ... }
  public Money calculateNoneDiscountFee() { ... }
}
```

위와 다르게 파라미터가 없으므로 객체 내부 상태를 그대로 드러내지 않는다고 생각할 수 있고, 캡슐화의 원칙을 지킨다 생각할 수 있다. 

그러나, `Movie` 역시 내부 구현을 인터페이스에 노출시키고 있다. 여기서 노출 시키는 것은 `calculateAmountDiscountedFee()` 와 `calculatePercentDiscountedFee()` , `calculateNoneDiscountFee()` 라는 세 개의 메서드는 할인 정책이 어떠한 것들이 있다는 사실을 만천하에 공개하고 있다. 

만약 **새로운 할인 정책이 추가되거나 제거된다면 의존하는 모든 클라이언트가 영향**을 받을 것이다.

따라서 세 가지 할인 정책을 포함하고 있다는 내부 구현을 성공적으로 캡슐화 하지 않는다.

## 높은 결합도

캡슐화 위반으로 인해 `DiscountCondition` 의 내부 구현이 외부로 노출된 상황이다.

이 때문에 `Movie`와 `DiscountCondition` 사이의 결합도는 높을 수 밖에 없다.

아래 코드를 참고해보자.

```java
public boolean isDiscountable(LocalDateTime whenScreened, int sequence) {
    for(DiscountCondition condition : discountConditions) {
      if (discountableByCondition(whenScreened, sequence, condition)) {
        return true;
      }
    }
    return false;
  }

  private boolean discountableByCondition(LocalDateTime whenScreened, int sequence, DiscountCondition condition) {
    if(condition.getType() == DiscountConditionType.PERIOD) {
      return condition.isCountable(whenScreened.getDayOfWeek(), whenScreened.toLocalTime());
    } else {
      return condition.isCountable(sequence);
    }
  }
```

`isCountable()` 메서드는 `DiscountConditions` 을 순회돌면서 알맞은 컨디션이 존재하는지 확인한다.

이러한 로직 때문에 `Movie` 와 `DiscountCondition` 사이에는 결합도가 높을 수 밖에 없다.

만약 여기 메서드를 변경하면 얼마나 많은 영향을 끼치게 될까?

1. `DiscountCondition` 의 기간 할인 조건 명칭이 `PERIOD` 에서 다른 값으로 변경된다면 `Movie` 를 수정해야한다.
2. `DiscountCondition` 의 종류가 추가되거나 삭제된다면 `if ~ else` 문을 수정해야한다.
3. 각 `DiscountCondition` 의 만족 여부를 판단 하는데 필요한 정보가 변경된다면 `discountableByCondition()` 메서드의 시그니처가 함께 변경되고, 이 메서드에 의존하는 `Screening` 에 대한 변경이 필수불가결하다.

이러한 문제의 원천은 `DiscountCondition` 의 **구현에 의존**하기 때문이다. 즉, DIP 위배이다.

결국 구체적인 것에 의존하면서 DIP가 위배되면서 결합도가 높아지면서 생긴 결과이다.

DIP 위배라는 것도 사실 인터페이스로 해결하기 이전에 캡슐화를 통해서 의존하는 객체가 구체적으로 무얼 하는지 모르게끔하여 처리할 수 있다. 

즉, 이러한 결합도가 높아지게 된 원인도 `DiscountCondition` 을 올바르게 캡슐화 원칙을 지키지 못했기 때문이라 볼 수 있다.

## 낮은 응집도

이번에는 `Screening` 을 살펴보자. 

앞에서 설명한 바와 같이 `DiscountCondition`이 할인 여부를 판단하는 데 필요한 정보가 변경되면 `isDiscountable()` 메서드로 전달해야 하는 파라미터의 종류도 변경해야 하고, 이로 인해 메서드 시그니처도 변경될 수 있다고 하였다.

아래의 코드를 보자.

```java
public Money calculateFee(int audienceCount) {
    switch (movie.getMovieType()) {
      case AMOUNT_DISCOUNT:
        if(movie.isDiscountable(whenScreened, sequence)) {
          return movie.calculateAmountDiscountedFee().times(audienceCount);
        }
        break;
      case PERCENT_DISCOUNT:
        if(movie.isDiscountable(whenScreened, sequence)) {
          return movie.calculatePercentDiscountedFee().times(audienceCount);
        }
        break;
      case NONE_DISCOUNT:
        return movie.calculateNoneDiscountFee().times(audienceCount);
    }
    return movie.calculateNoneDiscountFee().times(audienceCount);
  }
```

만약, 할인 조건의 종류를 변경한다면 `DiscountCondition`, `Movie` 그리고 `Screening` 모두를 수정해야한다. 위에서 말한 바와 같이 하나의 변경을 수용하기 위해 코드의 여러 곳을 동시에 변경해야한 다는 것은 응집도가 낮다는 증거이다. 

이 또한, 캡슐화가 제대로 되어있지 않았기때문이라는 사실을 우리는 이미 알고 있다.

# 데이터 중심 설계의 문제점

이번 장에서는 데이터 중심 설계를 중점으로 왜 객체지향 설계를 해야되는지 알게되었다고 본다.

데이터 중심 설계는 아래의 문제를 가질 수 있다고 정리할 수 있을 것이다. 

1. **데이터 중심 설계는 본질적으로 너무 이른 시기에 데이터에 관해 결정하도록 강요한다.**
2. **데이터 중심의 설계에서는 협력이라는 문맥을 고려하지 않고 객체를 고립시킨 채 오퍼레이션을 결정한다.**

## 데이터 중심 설계는 객체의 행동 보다 상태에 초점을 맞춘다.

데이터는 구현의 일부이다. 즉, 데이터 중심 설계는 처음부터 데이터에 관해 결정하도록 강요하기 때문에 너무 이른 시기에 내부 구현에 초점을 맞추게 된다.

과도하게 필요한 데이터를 넣다보니 접근자와 수정자를 추가하게되고 이는 캡슐화를 무너뜨리는 행위이다.

데이터를 먼저 결정하고 데이터를 처리하는 데 필요한 오퍼레이션을 나중에 결정하는 방식은 데이터에 대한 지식이 객체의 인터페이스에 고스란히 드러나게 된다. 결과적으로 구현을 캡슐화하는 데 실패하고 코드는 변경에 취약해진다.

## 데이터 중심 설계는 객체를 고립시킨 채 오퍼레이션을 정의하도록 만든다

객체지향 설계란 협력하는 객체들의 공동체를 구축하는 것을 의미한다 하였다.

따라서, 협력이라는 문맥 안에서 필요한 책임을 결정하고 이를 수행할 적절한 객체를 결정하는 것이 가장 중요하다.

객체가 내부에 어떤 상태를 가지고 그 상태를 어떻게 관리하는가는 부가적인 문제이다.

데이터 중심 설계는 객체의 외부가 아니라 내부로 향한다. 

위와 같은 문제 떄문에 체의 인터페이스에 구현이 노출되어 협력이 구현 세부사항에 종속되게 되고 무너지게 되는 것이다.
