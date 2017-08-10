/**
 *
 */
package de.hybris.platform.addons.facades.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.addons.loyaltypointaddon.enums.LoyaltyPointConfigurationType;
import de.hybris.platform.addons.loyaltypointaddon.facades.impl.LoyaltyPointAcceleratorCheckoutFacade;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class LoyaltyPointAcceleratorCheckoutFacadeIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private LoyaltyPointAcceleratorCheckoutFacade loyaltyPointAcceleratorCheckoutFacade;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;

	@Resource
	private LoyaltyPointService loyaltyPointService;

	@Resource
	private UserService userService;

	@Resource
	private CartService cartService;

	@Resource
	private SessionService sessionService;

	private static final int COLLECT_AMOUNT = 4;
	private static final int COLLECT_PERCENTAGE = 50;
	private static final int ORDER_PERCENTAGE = 50;
	private static final double TOTAL_PRICE = 10;
	private static final int CUSTOMER_LOYALTY_POINT_AMOUNT = 5;
	private static final int SELECTED_LOYALTY_POINT_AMOUNT = 5;
	private static final String SESSION_ATTRIBUTE = "loyaltypoint_amount";
	private LoyaltyPointConfigurationModel config;
	private CurrencyModel currency;

	@Before
	public void setup()
	{

		String queryString = "SELECT {p:" + CurrencyModel.PK + "} " + "FROM {" + CurrencyModel._TYPECODE + " AS p} " + "WHERE "
				+ "{p:" + CurrencyModel.ISOCODE + "}=?isocode ";

		FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("isocode", "EUR");
		currency = flexibleSearchService.<CurrencyModel> search(query).getResult().get(0);

		queryString = "SELECT {p:" + LoyaltyPointConfigurationModel.PK + "} " + "FROM {" + LoyaltyPointConfigurationModel._TYPECODE
				+ " AS p} " + "WHERE " + "{p:" + LoyaltyPointConfigurationModel.CURRENCY + "}=?currency ";

		query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("currency", currency);
		config = flexibleSearchService.<LoyaltyPointConfigurationModel> search(query).getResult().get(0);

	}

	@Test
	public void testBeforePlaceOrder() throws InvalidCartException
	{
		final CustomerModel customer = new CustomerModel();
		customer.setLoyaltyPointAmount(0);
		customer.setSessionCurrency(currency);
		modelService.save(customer);
		userService.setCurrentUser(customer);

		final CartModel cart = new CartModel();
		cart.setTotalPrice(TOTAL_PRICE);
		modelService.save(cart);
		cartService.setSessionCart(cart);

		config.setCollectPercentage(COLLECT_PERCENTAGE);
		config.setType(LoyaltyPointConfigurationType.RELATIVE);

		loyaltyPointAcceleratorCheckoutFacade.placeOrder();
		final int customerLoyaltyPointAmount = (int) (TOTAL_PRICE * COLLECT_PERCENTAGE / 100);
		assertEquals(customerLoyaltyPointAmount, customer.getLoyaltyPointAmount());

		config.setCollectAmount(COLLECT_AMOUNT);
		config.setType(LoyaltyPointConfigurationType.ABSOLUTE);
		sessionService.setAttribute(SESSION_ATTRIBUTE, new Double(5f));

		loyaltyPointAcceleratorCheckoutFacade.placeOrder();
		assertEquals(customerLoyaltyPointAmount - 5 + COLLECT_AMOUNT, customer.getLoyaltyPointAmount());

	}
}
