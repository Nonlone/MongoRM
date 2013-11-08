package com.lunjar.mjorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解DBCollection名字
 * @author Ezir
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DBCollectionName {
	/**
	 * 注解DBCollection名字，""空为类名
	 * @return
	 */
	public String name() default "";
}
