package hello.proxy.pureproxy.concreteproxy;

import hello.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteproxy.code.TimeProxy;
import org.junit.jupiter.api.Test;

public class ConcreteProxyTest {

	@Test
	void noProxy() {
		ConcreteLogic concreteLogic = new ConcreteLogic();
		ConcreteClient client = new ConcreteClient(concreteLogic);
		client.execute();
		// ConcreteLogic 실행
	}

	@Test
	void addProxy() {
		// 서버 객체
		ConcreteLogic concreteLogic = new ConcreteLogic();
		// 프록시 객체는 서버 객체를 의존한다.
		TimeProxy timeProxy = new TimeProxy(concreteLogic);
		// 클라이언트는 프록시 객체를 의존한다.
		ConcreteClient client = new ConcreteClient(timeProxy);

		client.execute();
		// TimeDecorator 실행
		// ConcreteLogic 실행
		// TimeDecorator 종료 resultTime=0
	}
}
