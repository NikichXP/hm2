package com.hm.interceptor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

	String value() default "";

	public static enum Types {
		moderator("moderator"), admin("admin"), worker("worker"), any("any"), client("client");

		final String id;
		Types(String id) { this.id = id; }
		public String getValue() { return id; }
	}

}
