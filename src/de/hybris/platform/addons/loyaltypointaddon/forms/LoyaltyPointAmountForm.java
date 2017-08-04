/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.forms;

import de.hybris.platform.addons.loyaltypointaddon.constraints.ConfigPricePercentage;
import de.hybris.platform.addons.loyaltypointaddon.constraints.CustomerAmount;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class LoyaltyPointAmountForm
{
	@NotNull(message = "You should fill this field")
	@Min(value = 0, message = "Amount must not be negative")
	@CustomerAmount(message = "You haven't got that much loyalty points")
	@ConfigPricePercentage(message = "You can not spend so many loyalty points on this order")
	private Integer loyaltyPointAmount;

	public Integer getLoyaltyPointAmount()
	{
		return loyaltyPointAmount;
	}

	public void setLoyaltyPointAmount(final Integer loyaltyPointAmount)
	{
		this.loyaltyPointAmount = loyaltyPointAmount;
	}
}
