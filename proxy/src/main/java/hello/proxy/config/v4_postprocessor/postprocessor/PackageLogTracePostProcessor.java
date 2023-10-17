package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

// 빈후처리기
// 원본 객체를 프록시 객체로 변환하는 기능을 제공한다.
@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {

	private final String basePackage; // 프록시 적용 여부를 판단할 기준(패키지)
	private final Advisor advisor; // 프록시 팩토리에 적용할 어드바이저

	public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
		this.basePackage = basePackage;
		this.advisor = advisor;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		log.info("param beanName={} bean={}", beanName, bean.getClass());

		// 프록시 적용 대상이 아니면 원본을 그대로 진행
		String packageName = bean.getClass().getPackageName();

		if (!packageName.startsWith(basePackage)) {
			return bean;
		}

		// 프록시 대상이면 프록시를 만들어서 반환
		ProxyFactory proxyFactory = new ProxyFactory(bean);
		proxyFactory.addAdvisor(advisor);

		Object proxy = proxyFactory.getProxy();
		log.info("create proxy: target={} proxy={}", bean.getClass(), proxy.getClass());

		return proxy;
	}
}
