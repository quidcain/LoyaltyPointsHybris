/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon;

import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.CustomerModel;



public interface LoyaltyPointConfigurationService
{
	LoyaltyPointConfigurationModel getConfigsForCurrency(CurrencyModel currency);

	void collectLoyaltyPoints(LoyaltyPointConfigurationModel config, CustomerModel customer, Double totalPrice);
}
