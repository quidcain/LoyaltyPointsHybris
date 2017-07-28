/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.aop;

import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointConfigurationService;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoyaltyPointAspect
{
	private static final Logger LOG = Logger.getLogger(LoyaltyPointAspect.class);

	private LoyaltyPointConfigurationService loyaltyPointConfigurationService;
	private UserService userService;
	private CartService cartService;


	@Before("execution(* de.hybris.platform.commercefacades.order.CheckoutFacade.placeOrder())")
	public void collectLoyaltyPoints()
	{
		CustomerModel customer;
		try
		{
			customer = (CustomerModel) userService.getCurrentUser();
			LOG.debug(customer.getDisplayName() + " " + customer.getName());
		}
		catch (final ClassCastException e)
		{
			LOG.warn("Current user isn't customer");
			return;
		}
		final CurrencyModel currency = customer.getSessionCurrency();
		final Double totalPrice = cartService.getSessionCart().getTotalPrice();
		final LoyaltyPointConfigurationModel config = loyaltyPointConfigurationService.getConfigsForCurrency(currency);
		if (config != null)
		{
			loyaltyPointConfigurationService.collectLoyaltyPoints(config, customer, totalPrice);
		}
	}

	@Resource
	public void setLoyaltyPointConfigurationService(final LoyaltyPointConfigurationService loyaltyPointConfigurationService)
	{
		this.loyaltyPointConfigurationService = loyaltyPointConfigurationService;
	}

	@Resource
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Resource
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}
}
