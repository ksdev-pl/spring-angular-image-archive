package pl.ksdev.imagearchive.shared;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Transaction which rolls back also on checked exceptions.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor=Throwable.class)
public @interface TransactionalWithChecked {}
