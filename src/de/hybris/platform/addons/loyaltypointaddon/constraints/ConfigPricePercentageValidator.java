/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.constraints;

import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;



public class ConfigPricePercentageValidator implements ConstraintValidator<ConfigPricePercentage, Integer>
{
	private LoyaltyPointService loyaltyPointService;

	@Override
	public void initialize(final ConfigPricePercentage constraintAnnotation)
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
		return !loyaltyPointService.isMaximumOrderPercentageExcedeed(value);
	}

	@Resource
	public void setLoyaltyPointService(final LoyaltyPointService loyaltyPointService)
	{
		this.loyaltyPointService = loyaltyPointService;
	}

}
