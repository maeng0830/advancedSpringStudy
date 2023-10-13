package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

// 인터페이스 없는 구체 클래스만으로 프록시 도입
// 서버 객체
@Slf4j
public class ConcreteLogic {

	public String operation() {
		log.info("ConcreteLogic 실행");
		return "data";
	}
}
