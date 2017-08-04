/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.impl;

import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.addons.loyaltypointaddon.daos.LoyaltyPointConfigurationDAO;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service("loyaltyPointService")
public class DefaultLoyaltyPointService implements LoyaltyPointService
{

	private static final Logger LOG = Logger.getLogger(DefaultLoyaltyPointService.class);

	private LoyaltyPointConfigurationDAO loyaltyPointConfigurationDAO;
	private ModelService modelService;
	private UserService userService;
	private CartService cartService;
	private SessionService sessionService;


	private LoyaltyPointConfigurationModel getConfigsForCurrency(final CurrencyModel currency)
	{
		final List<LoyaltyPointConfigurationModel> result = loyaltyPointConfigurationDAO.findConfigsByCurrency(currency);
		if (result.isEmpty())
		{
			throw new UnknownIdentifierException("Config with code '" + currency + "' not found!");
		}
		else if (result.size() > 1)
		{
			throw new AmbiguousIdentifierException(
					"Config currency '" + currency + "' is not unique, " + result.size() + " configs found!");
		}
		return result.get(0);
	}

	private CustomerModel getCurrentCustomer()
	{
		try
		{
			return (CustomerModel) userService.getCurrentUser();
		}
		catch (final ClassCastException e)
		{
			LOG.warn("Current user isn't customer");
			return null;
		}
	}

	private Double getTotalPrice()
	{
		return cartService.getSessionCart().getTotalPrice();
	}

	private CurrencyModel getCurrency(final CustomerModel customer)
	{
		return customer.getSessionCurrency();
	}

	@Override
	public boolean isMaximumOrderPercentageExcedeed(final Integer value)
	{
		final CustomerModel customer = getCurrentCustomer();
		if (customer == null)
		{
			return true;
		}
		final LoyaltyPointConfigurationModel config = getConfigsForCurrency(getCurrency(customer));
		final int maxLoyaltyPartOfOrder = (int) (getTotalPrice() * config.getOrderPercentage() / 100);
		if (value > maxLoyaltyPartOfOrder)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void collectLoyaltyPoints()
	{
		final CustomerModel customer = getCurrentCustomer();
		if (customer == null)
		{
			return;
		}
		final LoyaltyPointConfigurationModel config = getConfigsForCurrency(getCurrency(customer));
		final Double totalPrice = getTotalPrice();
		final int oldLoyaltyPointAmount = customer.getLoyaltyPointAmount();
		switch (config.getType())
		{
			case ABSOLUTE:
				customer.setLoyaltyPointAmount(oldLoyaltyPointAmount + config.getCollectAmount());
				break;
			case RELATIVE:
				if (totalPrice != null)
				{
					customer.setLoyaltyPointAmount(
							(int) (oldLoyaltyPointAmount + totalPrice.doubleValue() * config.getCollectPercentage() / 100));
				}
				break;
		}
		modelService.save(customer);
	}

	@Override
	public void payPartWithLoyaltyPoints()
	{
		final CustomerModel customer = getCurrentCustomer();
		if (customer == null)
		{
			return;
		}
		final LoyaltyPointConfigurationModel config = getConfigsForCurrency(getCurrency(customer));
		final Object loyaltypointAmount = sessionService.getAttribute("loyaltypoint_amount");
		if (loyaltypointAmount != null && loyaltypointAmount instanceof Integer)
		{
			final Integer intloyaltypointAmount = (Integer) loyaltypointAmount;
			if (config != null)
			{
				final CartModel cart = cartService.getSessionCart();
				cart.setTotalPrice(getTotalPrice() - intloyaltypointAmount);
				cartService.setSessionCart(cart);
				modelService.save(cart);
				customer.setLoyaltyPointAmount(customer.getLoyaltyPointAmount() - intloyaltypointAmount);
				modelService.save(customer);
			}
		}

	}

	@Resource
	public void setLoyaltyPointConfigurationDAO(final LoyaltyPointConfigurationDAO loyaltyPointConfigurationDAO)
	{
		this.loyaltyPointConfigurationDAO = loyaltyPointConfigurationDAO;
	}

	@Resource
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	@Resource
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
