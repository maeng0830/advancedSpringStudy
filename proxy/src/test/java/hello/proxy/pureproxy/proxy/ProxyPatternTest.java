package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

	// 총 3번 데이터를 조회하므로 3초가 걸린다.
	@Test
	void noProxyTest() {
		RealSubject realSubject = new RealSubject();

		ProxyPatternClient client = new ProxyPatternClient(realSubject);

		client.execute();
		// 실제 객체 호출
		client.execute();
		// 실제 객체 호출
		client.execute();
		// 실제 객체 호출
	}

	// 프록시 객체를 통해 캐시 기능을 적용했다.
	// 서버 객체와 클라이언트의 코드를 전혀 변경하지 않고, 프록시 객체를 통해 접근 제어 기능을 구현했다.
	@Test
	void cacheProxyTest() {
		RealSubject realSubject = new RealSubject();

		// 프록시 객체는 서버 객체를 의존한다.
		CacheProxy cacheProxy = new CacheProxy(realSubject);

		// 클라이언트는 프록시 객체를 의존한다.
		// 프록시 객체와 서버 객체는 동일한 인터페이스의 구현체이다.
		ProxyPatternClient client = new ProxyPatternClient(cacheProxy);

		// 캐시값이 없는 상태의 첫 번째 데이터 조회에서는 실제 객체 호출을 통해 데이터를 조회하고, 캐시에 저장한다.
		// 이후 데이터 조회에서는 실제 객체를 호출하지 않고, 프록시 객체의 캐시값을 조회한다.
		client.execute();
		// 프록시 호출
		// 실제 객체 호출
		client.execute();
		// 프록시 호출
		client.execute();
		// 프록시 호출
	}
}
