/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon;

public interface LoyaltyPointService
{
	boolean isMaximumOrderPercentageExcedeed(Integer value);

	void collectLoyaltyPoints();

	void payPartWithLoyaltyPoints();
}
