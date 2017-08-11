/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.addons.loyaltypointaddon.daos.LoyaltyPointConfigurationDAO;
import de.hybris.platform.addons.loyaltypointaddon.enums.LoyaltyPointConfigurationType;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultLoyaltyPointConfigurationDAOIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private LoyaltyPointConfigurationDAO loyaltyPointConfigurationDAOs;

	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	private static final int COLLECT_AMOUNT = 4;
	private static final int ORDER_PERCENTAGE = 60;
	private CurrencyModel currency;

	@Before
	public void setup()
	{
		final String queryString = //
				"SELECT {p:" + CurrencyModel.PK + "} "//
						+ "FROM {" + CurrencyModel._TYPECODE + " AS p} " + "WHERE " + "{p:" + CurrencyModel.ISOCODE + "}=?isocode ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("isocode", "EUR");
		currency = flexibleSearchService.<CurrencyModel> search(query).getResult().get(0);
	}

	@Test
	public void testFindConfigsByCurrency()
	{
		List<LoyaltyPointConfigurationModel> configsByCurrency = loyaltyPointConfigurationDAOs.findConfigsByCurrency(currency);
		assertTrue(configsByCurrency.isEmpty());

		final LoyaltyPointConfigurationModel loyaltyPointConfigurationModel = new LoyaltyPointConfigurationModel();
		loyaltyPointConfigurationModel.setCurrency(currency);
		loyaltyPointConfigurationModel.setType(LoyaltyPointConfigurationType.ABSOLUTE);
		loyaltyPointConfigurationModel.setCollectAmount(COLLECT_AMOUNT);
		loyaltyPointConfigurationModel.setOrderPercentage(ORDER_PERCENTAGE);
		modelService.save(loyaltyPointConfigurationModel);

		configsByCurrency = loyaltyPointConfigurationDAOs.findConfigsByCurrency(currency);
		assertTrue(configsByCurrency.size() != 0);
		assertEquals(1, configsByCurrency.size());

		assertNull(loyaltyPointConfigurationDAOs.findConfigsByCurrency(new CurrencyModel()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testloyaltyPointConfigurationDAO_NullParam()
	{
		loyaltyPointConfigurationDAOs.findConfigsByCurrency(null);
	}
}
