/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.addons.loyaltypointaddon.enums.LoyaltyPointConfigurationType;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultLoyaltyPointServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private LoyaltyPointService loyaltyPointService;

	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	private static final int COLLECT_AMOUNT = 4;
	private static final int ORDER_PERCENTAGE = 60;
	private static final int COLLECT_PERCENTAGE = 50;
	private CurrencyModel currency;

	/*@Before
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
	public void testGetConfigsForCurrency()
	{

		LoyaltyPointConfigurationModel configByCurrency;
		try
		{
			configByCurrency = loyaltyPointConfigurationService.getConfigsForCurrency(currency);
			fail("UnknownIdentifierException to be thrown");
		}
		catch (final UnknownIdentifierException e)
		{
			//we expect this exception here
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}

		final LoyaltyPointConfigurationModel loyaltyPointConfigurationModel = new LoyaltyPointConfigurationModel();
		loyaltyPointConfigurationModel.setCurrency(currency);
		loyaltyPointConfigurationModel.setType(LoyaltyPointConfigurationType.ABSOLUTE);
		loyaltyPointConfigurationModel.setCollectAmount(COLLECT_AMOUNT);
		loyaltyPointConfigurationModel.setOrderPercentage(ORDER_PERCENTAGE);
		modelService.save(loyaltyPointConfigurationModel);

		configByCurrency = loyaltyPointConfigurationService.getConfigsForCurrency(currency);
		assertNotNull(configByCurrency);
		assertEquals(loyaltyPointConfigurationModel, configByCurrency);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetConfigsForCurrency_NullParam()
	{
		loyaltyPointConfigurationService.getConfigsForCurrency(null);
	}

	@Test
	public void testCollectLoyaltyPoints()
	{

		final LoyaltyPointConfigurationModel config = new LoyaltyPointConfigurationModel();
		config.setCurrency(currency);
		config.setType(LoyaltyPointConfigurationType.ABSOLUTE);
		config.setCollectAmount(COLLECT_AMOUNT);
		config.setOrderPercentage(ORDER_PERCENTAGE);
		modelService.save(config);

		final CustomerModel customer = new CustomerModel();
		customer.setLoyaltyPointAmount(0);
		modelService.save(customer);

		final Double totalPrice = new Double(50);
		loyaltyPointConfigurationService.collectLoyaltyPoints(config, customer, totalPrice);
		assertEquals(COLLECT_AMOUNT, customer.getLoyaltyPointAmount());


		config.setType(LoyaltyPointConfigurationType.RELATIVE);
		config.setCollectPercentage(COLLECT_PERCENTAGE);
		customer.setLoyaltyPointAmount(0);
		modelService.save(customer);

		loyaltyPointConfigurationService.collectLoyaltyPoints(config, customer, totalPrice);
		assertEquals((int) (totalPrice.doubleValue() * COLLECT_PERCENTAGE / 100), customer.getLoyaltyPointAmount());
	}*/
}
