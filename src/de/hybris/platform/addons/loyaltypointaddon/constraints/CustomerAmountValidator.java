/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.constraints;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;


public class CustomerAmountValidator implements ConstraintValidator<CustomerAmount, Integer>
{
	private static final Logger LOG = Logger.getLogger(CustomerAmountValidator.class);

	private UserService userService;

	@Override
	public void initialize(final CustomerAmount constraintAnnotation)
	{
		// empty
	}

	@Override
	public boolean isValid(final Integer value, final ConstraintValidatorContext context)
	{
		if (value == null)
		{
			return false;
		}
		CustomerModel customer;
		try
		{
			customer = (CustomerModel) userService.getCurrentUser();
		}
		catch (final ClassCastException e)
		{
			LOG.warn("Current user isn't customer");
			return false;
		}
		return customer.getLoyaltyPointAmount() < value ? false : true;
	}

	@Resource
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}


