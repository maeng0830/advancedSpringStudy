package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 클래스에 적용할 애노테이션임을 지정
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassAop {

}
