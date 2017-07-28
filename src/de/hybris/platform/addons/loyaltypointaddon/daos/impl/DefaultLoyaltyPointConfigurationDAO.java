/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.daos.impl;

import de.hybris.platform.addons.loyaltypointaddon.daos.LoyaltyPointConfigurationDAO;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component("loyaltyPointConfigurationDAO")
public class DefaultLoyaltyPointConfigurationDAO implements LoyaltyPointConfigurationDAO
{

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<LoyaltyPointConfigurationModel> findConfigsByCurrency(final CurrencyModel currency)
	{
		final String queryString = //
				"SELECT {p:" + LoyaltyPointConfigurationModel.PK + "} "//
						+ "FROM {" + LoyaltyPointConfigurationModel._TYPECODE + " AS p} " + "WHERE " + "{p:"
						+ LoyaltyPointConfigurationModel.CURRENCY + "}=?currency ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("currency", currency);
		return flexibleSearchService.<LoyaltyPointConfigurationModel> search(query).getResult();
	}

}
