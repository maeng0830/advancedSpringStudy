package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// for app.v1~v3
// implementation 'org.springframework.boot:spring-boot-starter-aop'라는 라이브러리를 등록했다.
// 때문에 AnnotationAwareAspectJAutoProxyCreator라는 빈후처리기가 이미 스프링 빈으로 등록되어 있다.
// 이 빈후처리기는 스프링 빈으로 등록된 Advisor들을 자동으로 찾아서 프록시가 필요한 곳에 자동으로 프록시를 적용해준다.
// 어드바이저의 포인트컷은 총 2가지에 사용된다.
//	1. 프록시 적용 여부 판단: 클래스 + 메소드 조건을 통해 프록시를 생성할 필요가 있는지를 판단한다.
//	2. 어드바이스 적용 여부 판단: 프록시가 호출되었을 때 프록시 로직을 적용해야하는 지 여부를 판단한다.
@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

	//	@Bean
	public Advisor advisor1(LogTrace logTrace) {
		// pointcut
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedNames("request*", "order*", "save*");

		// advice
		LogTraceAdvice advice = new LogTraceAdvice(logTrace);

		return new DefaultPointcutAdvisor(pointcut, advice);
	}

	// hello.proxy.app 하위만 프록시, 어드바이스 적용 대상이다.
//	@Bean
	public Advisor advisor2(LogTrace logTrace) {
		// pointcut
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(* hello.proxy.app..*(..))");

		// advice
		LogTraceAdvice advice = new LogTraceAdvice(logTrace);

		return new DefaultPointcutAdvisor(pointcut, advice);
	}

	// hello.proxy.app 하위만 프록시, 어드바이스 적용 대상이다. 단 noLog 메소드는 제외이다.
	@Bean
	public Advisor advisor3(LogTrace logTrace) {
		// pointcut
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(
				"execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");

		// advice
		LogTraceAdvice advice = new LogTraceAdvice(logTrace);

		return new DefaultPointcutAdvisor(pointcut, advice);
	}
}
