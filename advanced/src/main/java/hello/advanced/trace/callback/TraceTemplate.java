package hello.advanced.trace.callback;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

// for app.v5
// 전략 패턴(파라미터 전달 방식) = 템플릿 콜백 패턴
public class TraceTemplate {

	private final LogTrace trace;

	public TraceTemplate(LogTrace trace) {
		this.trace = trace;
	}

	public <T> T execute(String message, TraceCallback<T> callback) {
		TraceStatus status = null;

		try {
			status = trace.begin(message);

			// 비즈니스 로직 호출
			T result = callback.call();

			trace.end(status);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e; // 로그 기능을 위해 예외를 여기서 막으면 안된다. 다시 던져줘야한다.
		}
	}
}
