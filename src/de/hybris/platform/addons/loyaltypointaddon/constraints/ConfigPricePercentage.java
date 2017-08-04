/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ConfigPricePercentageValidator.class)
public @interface ConfigPricePercentage
{
	String message() default "{de.hybris.platform.addons.loyaltypointaddon.constraints.ConfigPricePercentage.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
