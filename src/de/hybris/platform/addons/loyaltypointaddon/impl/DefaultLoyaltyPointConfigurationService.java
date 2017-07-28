/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.impl;

import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointConfigurationService;
import de.hybris.platform.addons.loyaltypointaddon.daos.LoyaltyPointConfigurationDAO;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service("loyaltyPointConfigurationService")
public class DefaultLoyaltyPointConfigurationService implements LoyaltyPointConfigurationService
{

	private LoyaltyPointConfigurationDAO loyaltyPointConfigurationDAO;
	private ModelService modelService;

	@Override
	public LoyaltyPointConfigurationModel getConfigsForCurrency(final CurrencyModel currency)
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

	@Override
	public void collectLoyaltyPoints(final LoyaltyPointConfigurationModel config, final CustomerModel customer,
			final Double totalPrice)
	{
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
}
