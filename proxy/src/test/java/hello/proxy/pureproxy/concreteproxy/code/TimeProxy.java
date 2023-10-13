package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

// 인터페이스 없는 구체 클래스만으로 프록시 도입
// 프록시 객체
// 구체 클래스만으로 프록시를 도입할 때는 프록시가 서버를 상속한다! 그리고 호출할 서버의 메소드를 오버라이딩 한다.
@Slf4j
public class TimeProxy extends ConcreteLogic {

	private ConcreteLogic realLogic; // 프록시가 대신 호출할 서버 객체

	public TimeProxy(ConcreteLogic realLogic) {
		this.realLogic = realLogic;
	}

	// 프록시가 대신 호출할 서버의 메소드를 오버라이딩 한다.
	@Override
	public String operation() {
		log.info("TimeDecorator 실행");
		long startTime = System.currentTimeMillis();

		String result = realLogic.operation();

		long endTime = System.currentTimeMillis(); // 서버 객체 호출

		long resultTime = endTime - startTime;

		log.info("TimeDecorator 종료 resultTime={}", resultTime);

		return result;
	}
}
