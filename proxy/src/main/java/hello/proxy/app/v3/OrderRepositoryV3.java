package hello.proxy.app.v3;

import org.springframework.stereotype.Repository;

@Repository // 컴포넌트 스캔으로 빈 자동 등록
public class OrderRepositoryV3 {

	public void save(String itemId) {
		// 저장 로직
		if (itemId.equals("ex")) {
			throw new IllegalStateException("예외 발생!");
		}
		sleep(1000);
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
