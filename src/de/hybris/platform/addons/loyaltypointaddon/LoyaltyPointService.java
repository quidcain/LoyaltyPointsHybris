/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon;

public interface LoyaltyPointService
{
	boolean isMaximumOrderPercentageExcedeed();

	void collectLoyaltyPoints();

	void payPartWithLoyaltyPoints();
}
