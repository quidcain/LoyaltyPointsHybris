/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon;

import de.hybris.platform.core.model.order.OrderModel;

public interface LoyaltyPointService
{
	boolean isMaximumOrderPercentageExcedeed(Integer value);

	void collectLoyaltyPoints();

	void payPartWithLoyaltyPoints(OrderModel order);
}
