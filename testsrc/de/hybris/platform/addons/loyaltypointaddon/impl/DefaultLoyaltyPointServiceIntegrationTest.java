/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.addons.loyaltypointaddon.enums.LoyaltyPointConfigurationType;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;


@IntegrationTest
public class DefaultLoyaltyPointServiceIntegrationTest extends ServicelayerTransactionalTest
{
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
	public void testIsMaximumOrderPercentageExcedeed()
	{
		final UserModel user = new EmployeeModel();
		modelService.save(user);
		userService.setCurrentUser(user);

		assertTrue(loyaltyPointService.isMaximumOrderPercentageExcedeed(0));

		final CustomerModel customer = new CustomerModel();
		customer.setLoyaltyPointAmount(0);
		customer.setSessionCurrency(currency);
		modelService.save(customer);
		userService.setCurrentUser(customer);

		final CartModel cart = new CartModel();
		cart.setTotalPrice(TOTAL_PRICE);
		modelService.save(cart);
		cartService.setSessionCart(cart);

		config.setOrderPercentage(ORDER_PERCENTAGE);

		assertFalse(loyaltyPointService.isMaximumOrderPercentageExcedeed(-100));
		assertFalse(loyaltyPointService.isMaximumOrderPercentageExcedeed(0));
		assertFalse(loyaltyPointService.isMaximumOrderPercentageExcedeed(5));
		assertTrue(loyaltyPointService.isMaximumOrderPercentageExcedeed(6));
		assertTrue(loyaltyPointService.isMaximumOrderPercentageExcedeed(100));
	}

	@Test
	public void testCollectLoyaltyPoints()
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

		config.setCollectAmount(COLLECT_AMOUNT);
		config.setType(LoyaltyPointConfigurationType.ABSOLUTE);
		loyaltyPointService.collectLoyaltyPoints();
		assertEquals(COLLECT_AMOUNT, customer.getLoyaltyPointAmount());


		config.setType(LoyaltyPointConfigurationType.RELATIVE);
		config.setCollectPercentage(COLLECT_PERCENTAGE);
		loyaltyPointService.collectLoyaltyPoints();
		assertEquals((int) (TOTAL_PRICE * COLLECT_PERCENTAGE / 100) + COLLECT_AMOUNT, customer.getLoyaltyPointAmount());
	}

	@Test
	public void testPayPartWithLoyaltyPoints()
	{
		final AbstractOrderModel order = new OrderModel();
		order.setTotalPrice(TOTAL_PRICE);
		modelService.save(order);

		final UserModel user = new EmployeeModel();
		modelService.save(user);
		userService.setCurrentUser(user);

		loyaltyPointService.payPartWithLoyaltyPoints(order);
		assertEquals(Double.valueOf(TOTAL_PRICE), order.getTotalPrice());

		final CustomerModel customer = new CustomerModel();
		customer.setLoyaltyPointAmount(CUSTOMER_LOYALTY_POINT_AMOUNT);
		customer.setSessionCurrency(currency);
		modelService.save(customer);
		userService.setCurrentUser(customer);

		sessionService.removeAttribute(SESSION_ATTRIBUTE);
		loyaltyPointService.payPartWithLoyaltyPoints(order);
		assertEquals(Double.valueOf(TOTAL_PRICE), order.getTotalPrice());
		assertEquals(CUSTOMER_LOYALTY_POINT_AMOUNT, customer.getLoyaltyPointAmount());

		sessionService.setAttribute(SESSION_ATTRIBUTE, new Double(5f));
		loyaltyPointService.payPartWithLoyaltyPoints(order);
		assertEquals(Double.valueOf(TOTAL_PRICE), order.getTotalPrice());
		assertEquals(CUSTOMER_LOYALTY_POINT_AMOUNT, customer.getLoyaltyPointAmount());

		sessionService.setAttribute(SESSION_ATTRIBUTE, CUSTOMER_LOYALTY_POINT_AMOUNT);
		loyaltyPointService.payPartWithLoyaltyPoints(order);
		assertEquals(Double.valueOf(TOTAL_PRICE - CUSTOMER_LOYALTY_POINT_AMOUNT), order.getTotalPrice());
		assertEquals(0, customer.getLoyaltyPointAmount());
	}

}
