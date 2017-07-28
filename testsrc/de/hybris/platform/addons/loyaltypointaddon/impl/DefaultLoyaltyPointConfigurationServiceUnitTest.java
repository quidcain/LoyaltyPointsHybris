/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointConfigurationService;
import de.hybris.platform.addons.loyaltypointaddon.daos.LoyaltyPointConfigurationDAO;
import de.hybris.platform.addons.loyaltypointaddon.enums.LoyaltyPointConfigurationType;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultLoyaltyPointConfigurationServiceUnitTest
{
	@InjectMocks
	private final LoyaltyPointConfigurationService loyaltyPointConfigurationService = new DefaultLoyaltyPointConfigurationService();

	@Mock
	private LoyaltyPointConfigurationDAO loyaltyPointConfigurationDAO;

	private LoyaltyPointConfigurationModel loyaltyPointConfigurationModel;
	private CurrencyModel currency;

	private static final int COLLECT_AMOUNT = 4;
	private static final int ORDER_PERCENTAGE = 60;


	@Before
	public void setup()
	{
		currency = new CurrencyModel("EUR", "€");

		loyaltyPointConfigurationModel = new LoyaltyPointConfigurationModel();
		loyaltyPointConfigurationModel.setCurrency(currency);
		loyaltyPointConfigurationModel.setType(LoyaltyPointConfigurationType.ABSOLUTE);
		loyaltyPointConfigurationModel.setCollectAmount(COLLECT_AMOUNT);
		loyaltyPointConfigurationModel.setOrderPercentage(ORDER_PERCENTAGE);
	}

	@Test
	public void testloyaltyPointConfigurationService()
	{
		when(loyaltyPointConfigurationDAO.findConfigsByCurrency(currency))
				.thenReturn(Collections.singletonList(loyaltyPointConfigurationModel));
		final LoyaltyPointConfigurationModel configForCurrency = loyaltyPointConfigurationService.getConfigsForCurrency(currency);
		assertEquals(loyaltyPointConfigurationModel, configForCurrency);
	}
}
