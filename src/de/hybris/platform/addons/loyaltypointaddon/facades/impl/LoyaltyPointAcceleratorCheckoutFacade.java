/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.facades.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.core.model.order.CartModel;

import javax.annotation.Resource;



public class LoyaltyPointAcceleratorCheckoutFacade extends DefaultAcceleratorCheckoutFacade
{
	private LoyaltyPointService loyaltyPointService;

	@Override
	public void beforePlaceOrder(@SuppressWarnings("unused") final CartModel cartModel)
	{
		loyaltyPointService.collectLoyaltyPoints();
		loyaltyPointService.reduceCustomerLoyaltyPointAmount(cartModel);
	}


	@Resource
	public void setLoyaltyPointService(final LoyaltyPointService loyaltyPointService)
	{
		this.loyaltyPointService = loyaltyPointService;
	}
}
