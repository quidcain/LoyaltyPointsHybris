/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.facades.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.order.InvalidCartException;

import javax.annotation.Resource;



public class LoyaltyPointAcceleratorCheckoutFacade extends DefaultAcceleratorCheckoutFacade
{
	private LoyaltyPointService loyaltyPointService;

	@Override
	public OrderData placeOrder() throws InvalidCartException
	{
		loyaltyPointService.payPartWithLoyaltyPoints();
		loyaltyPointService.collectLoyaltyPoints();
		return super.placeOrder();
	}

	@Resource
	public void setLoyaltyPointService(final LoyaltyPointService loyaltyPointService)
	{
		this.loyaltyPointService = loyaltyPointService;
	}
}
