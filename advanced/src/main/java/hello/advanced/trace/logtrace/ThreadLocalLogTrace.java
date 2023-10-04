package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

// for app.v3 ~
// 파라미터가 아닌, 필드를 통해 동기화를 한다.
// FieldLogTrace는 싱글톤으로 등록된 스프링 빈이기 때문에, 이 객체의 인스턴스가 어플리케이션에 딱 1개만 존재한다.
// 하나만 존재하는 인스턴스의 traceHolder 필드에 여러 쓰레드가 동시에 접근하기 때문에 동시성 문제가 발생한다.
// traceHolder에 ThreadLocal을 적용하여, 각 쓰레드마다 개별적인 traceHolder 저장소를 가질 수 있게 하여 동시성 문제를 해결할 수 있다.
@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EX_PREFIX = "<X-";

	private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>(); // ThreadLocal 적용을 통한 동시성 문제 해결

	@Override
	public TraceStatus begin(String message) {
		syncTraceId();

		TraceId traceId = traceIdHolder.get();
		Long startTimeMs = System.currentTimeMillis();

		log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

		return new TraceStatus(traceId, startTimeMs, message);
	}

	private void syncTraceId() {
		TraceId traceId = traceIdHolder.get(); // traceHolder에서 traceId 조회

		if (traceId == null) {
			traceIdHolder.set(new TraceId());
		} else {
			traceIdHolder.set(traceId.createNextId());
		}
	}

	@Override
	public void end(TraceStatus status) {
		complete(status, null);
	}

	@Override
	public void exception(TraceStatus status, Exception e) {
		complete(status, e);
	}

	private void complete(TraceStatus status, Exception e) {
		Long stopTimeMs = System.currentTimeMillis();
		long resultTimeMs = stopTimeMs - status.getStartTimeMs();
		TraceId traceId = status.getTraceId();

		if (e == null) {
			log.info("[{}] {}{} time={}ms", traceId.getId(),
					addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(),
					resultTimeMs);
		} else {
			log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
					addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
					e.toString());
		}

		releaseTraceId();
	}

	private void releaseTraceId() {
		TraceId traceId = traceIdHolder.get(); // traceHolder에서 traceId 조회

		if (traceId.isFirstLevel()) {
			traceIdHolder.remove();
		} else {
			traceIdHolder.set(traceId.createPreviousId());
		}
	}

	private static String addSpace(String prefix, int level) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < level; i++) {
			sb.append((i == level - 1) ? "|" + prefix : "| ");
		}

		return sb.toString();
	}
}
