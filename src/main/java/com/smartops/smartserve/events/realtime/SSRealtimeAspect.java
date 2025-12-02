package com.smartops.smartserve.events.realtime;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.smartops.smartserve.events.realtime.service.SSRealTimeNotificationService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class SSRealtimeAspect {

	private final SSRealTimeNotificationService notificationService;

	@AfterReturning(pointcut = "execution(* com.smartops.smartserve.service.impl.*.*(..)) && @annotation(com.smartops.smartserve.events.realtime.RealtimeUpdate)", returning = "returnedValue")
	public void afterServiceMethod(JoinPoint jp, Object returnedValue) {
		if (returnedValue == null)
			return;

		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		SSRealtimeUpdate ann = method.getAnnotation(SSRealtimeUpdate.class);

		SSRealtimeMessage msg = new SSRealtimeMessage();
		msg.setType("TABLE_UPDATE");
		msg.setTopic(ann.topic());
		msg.setOperation(ann.operation().name());
		msg.setPayload(returnedValue);
		msg.setTimestamp(LocalDateTime.now());

		notificationService.publish(msg);
	}
}
