package hello.proxy.config.v6_aop;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v6_aop.aspect.LogTraceAspect;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Indexed;

// for app.v1~v3
// 자동 프록시 생성기(AutoProxyCreator)는 빈으로 등록된 @Aspect 객체를 찾아 Advisor로 변환하여 저장한다.
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

	@Bean
	public LogTraceAspect logTraceAspect(LogTrace logTrace) {
		return new LogTraceAspect(logTrace);
	}
}
