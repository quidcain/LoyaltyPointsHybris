package de.hybris.platform.addons.loyaltypointaddon.stepvalidator;

import de.hybris.merchandise.storefront.checkout.steps.validation.impl.DefaultDeliveryMethodCheckoutStepValidator;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;


public class DeliveryMethodAddonCheckoutStepValidator extends DefaultDeliveryMethodCheckoutStepValidator
{
	private UserService userService;

	@Override
	public ValidationResults validateOnExit()
	{
		CustomerModel customer;
		try
		{
			customer = (CustomerModel) userService.getCurrentUser();
			if (customer.getLoyaltyPointAmount() != 0)
			{
				return ValidationResults.REDIRECT_TO_DUMMY_STEP;
			}
		}
		catch (final ClassCastException e)
		{
			LOG.warn("Current user isn't customer");
		}
		if (getCheckoutFacade().hasPickUpItems())
		{
			return ValidationResults.REDIRECT_TO_PICKUP_LOCATION;
		}
		return ValidationResults.SUCCESS;
	}

	@Resource
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}