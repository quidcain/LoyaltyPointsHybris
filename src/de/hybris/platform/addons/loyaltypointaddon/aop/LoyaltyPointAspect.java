/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.aop;

import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoyaltyPointAspect
{
	private LoyaltyPointService loyaltyPointService;

	@Before("execution(* de.hybris.platform.commercefacades.order.CheckoutFacade.placeOrder())")
	public void placeOrder()
	{
		payPartWithLoyaltyPoints();
		collectLoyaltyPoints();
	}

	private void collectLoyaltyPoints()
	{
		loyaltyPointService.collectLoyaltyPoints();
	}

	private void payPartWithLoyaltyPoints()
	{
		loyaltyPointService.payPartWithLoyaltyPoints();
	}

	@Resource
	public void setLoyaltyPointService(final LoyaltyPointService loyaltyPointService)
	{
		this.loyaltyPointService = loyaltyPointService;
	}

}
