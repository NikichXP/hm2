package com.hm.interceptor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

	String value() default "";

	public static enum Types {
		moderator, admin, worker, any, client
	}

}
