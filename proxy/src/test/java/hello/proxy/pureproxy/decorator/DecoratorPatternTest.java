package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.Component;
import hello.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import hello.proxy.pureproxy.decorator.code.MessageDecorator;
import hello.proxy.pureproxy.decorator.code.RealComponent;
import hello.proxy.pureproxy.decorator.code.TimeDecorator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

	@Test
	void noDecorator() {
		RealComponent realComponent = new RealComponent();

		DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

		client.execute();
		// realComponent 실행
		// result=data
	}

	// 프록시 객체를 통해 부가 기능을 적용했다.
	// 서버 객체와 클라이언트의 코드를 전혀 변경하지 않고, 프록시 객체를 통해 부가 기능을 구현했다.
	@Test
	void decorator1() {
		// 서버 객체
		RealComponent realComponent = new RealComponent();
		// 프록시 객체는 서버 객체를 의존한다.
		MessageDecorator messageDecorator = new MessageDecorator(realComponent);
		// 클라이언트는 프록시 객체를 의존한다.
		DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);

		client.execute();
		// MessageDecorator 실행
		// realComponent 실행
		// MessageDecorator 적용 전=data, 적용 후=******data******
		// result=******data******
	}

	// 프록시 객체를 체인처럼 사용할 수 있다.
	// client -> timeDecorator -> messageDecorator -> realComponent
	@Test
	void decorator2() {
		// 서버 객체
		RealComponent realComponent = new RealComponent();
		// 프록시 객체는 서버 객체를 의존한다.
		MessageDecorator messageDecorator = new MessageDecorator(realComponent);
		// 프록시 객체(timeDecorator)는 또 다른 프록시 객체(messageDecorator)를 의존한다.
		TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
		// 클라이언트는 프록시 객체를 의존한다.
		DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);

		client.execute();
		// TimeDecorator 실행
		// MessageDecorator 실행
		// realComponent 실행
		// MessageDecorator 적용 전=data, 적용 후=******data******
		// TimeDecorator 종료 resultTIme=4ms
		// result=******data******
	}
}
