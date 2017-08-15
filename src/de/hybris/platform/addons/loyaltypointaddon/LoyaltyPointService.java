/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;

public interface LoyaltyPointService
{
	boolean isMaximumOrderPercentageExcedeed(Integer value);

	void collectLoyaltyPoints();

	void reduceCustomerLoyaltyPointAmount(CartModel cart);

	void subtractLoyaltyPointPartFromTotals(AbstractOrderModel abstractOrder);
}
