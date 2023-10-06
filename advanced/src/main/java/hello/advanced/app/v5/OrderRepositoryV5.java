package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV5 {

	private final TraceTemplate template;

	public OrderRepositoryV5(LogTrace trace) {
		this.template = new TraceTemplate(trace);
	}

	public void save(String itemId) {
		template.execute("OrderRepository.save()", () -> {
			// 저장 로직
			if (itemId.equals("ex")) {
				throw new IllegalArgumentException("예외 발생");
			}
			sleep(1000);
			return null; // 반환타입이 Void인 경우, null을 반환해준다.
		});
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
