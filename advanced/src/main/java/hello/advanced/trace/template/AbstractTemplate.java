package hello.advanced.trace.template;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

// for app.v4
// 템플릿 메소드 패턴 적용
public abstract class AbstractTemplate<T> {

	private final LogTrace trace;

	public AbstractTemplate(LogTrace trace) {
		this.trace = trace;
	}

	public T execute(String message) {
		TraceStatus status = null;

		try {
			status = trace.begin(message);

			// 비즈니스 로직 호출
			T result = call();

			trace.end(status);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e; // 로그 기능을 위해 예외를 여기서 막으면 안된다. 다시 던져줘야한다.
		}
	}

	protected abstract T call();
}
