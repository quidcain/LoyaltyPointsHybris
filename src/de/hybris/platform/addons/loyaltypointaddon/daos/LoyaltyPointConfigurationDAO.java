/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.daos;

import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.util.List;


/**
 * @author stoat
 *
 */
public interface LoyaltyPointConfigurationDAO
{
	List<LoyaltyPointConfigurationModel> findConfigsByCurrency(CurrencyModel currency);
}
