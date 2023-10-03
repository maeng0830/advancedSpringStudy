package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

// for app.v3
// 파라미터가 아닌, 필드를 통해 동기화를 한다.
// 파라미터 추가 없는 깔끔한 코드를 작성할 수 있지만, 동시성 문제가 발생한다!!!!
// FieldLogTrace는 싱글톤으로 등록된 스프링 빈이기 때문에, 이 객체의 인스턴스가 어플리케이션에 딱 1개만 존재한다.
// 하나만 존재하는 인스턴스의 traceHolder 필드에 여러 쓰레드가 동시에 접근하기 때문에 문제가 발생한다.
@Slf4j
public class FieldLogTrace implements LogTrace {

	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EX_PREFIX = "<X-";

	private TraceId traceIdHolder; // 필드를 통한 traceId 동기화 -> 동시성 이슈 발생

	@Override
	public TraceStatus begin(String message) {
		syncTraceId();

		TraceId traceId = traceIdHolder;
		Long startTimeMs = System.currentTimeMillis();

		log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

		return new TraceStatus(traceId, startTimeMs, message);
	}

	private void syncTraceId() {
		if (traceIdHolder == null) {
			traceIdHolder = new TraceId();
		} else {
			traceIdHolder = traceIdHolder.createNextId();
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
		if (traceIdHolder.isFirstLevel()) {
			traceIdHolder = null;
		} else {
			traceIdHolder = traceIdHolder.createPreviousId();
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
