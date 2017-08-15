/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.addons.loyaltypointaddon.daos.LoyaltyPointConfigurationDAO;
import de.hybris.platform.addons.loyaltypointaddon.enums.LoyaltyPointConfigurationType;
import de.hybris.platform.addons.loyaltypointaddon.model.LoyaltyPointConfigurationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultLoyaltyPointServiceUnitTest
{
	@InjectMocks
	private final LoyaltyPointService loyaltyPointService = new DefaultLoyaltyPointService();

	@Mock
	private LoyaltyPointConfigurationDAO loyaltyPointConfigurationDAO;

	@Mock
	private ModelService modelService;

	@Mock
	private UserService userService;

	@Mock
	private CartService cartService;

	@Mock
	private SessionService sessionService;

	private static final int COLLECT_AMOUNT = 4;
	private static final int COLLECT_PERCENTAGE = 50;
	private static final int ORDER_PERCENTAGE = 50;
	private static final double TOTAL_PRICE = 10;
	private static final int CUSTOMER_LOYALTY_POINT_AMOUNT = 5;
	private static final int SELECTED_LOYALTY_POINT_AMOUNT = 2;
	private LoyaltyPointConfigurationModel config;
	private CurrencyModel currency;

	@Before
	public void setup()
	{
		currency = new CurrencyModel("EUR", "€");

		config = new LoyaltyPointConfigurationModel();
		config.setCurrency(currency);
		config.setType(LoyaltyPointConfigurationType.ABSOLUTE);
		config.setCollectAmount(COLLECT_AMOUNT);
		config.setCollectPercentage(COLLECT_PERCENTAGE);
		config.setOrderPercentage(ORDER_PERCENTAGE);

		when(loyaltyPointConfigurationDAO.findConfigsByCurrency(currency)).thenReturn(Collections.singletonList(config));
	}


	@Test
	public void testIsMaximumOrderPercentageExcedeed()
	{
		final UserModel user = new EmployeeModel();
		modelService.save(user);
		userService.setCurrentUser(user);
		when(userService.getCurrentUser()).thenReturn(user);

		assertTrue(loyaltyPointService.isMaximumOrderPercentageExcedeed(0));

		final CustomerModel customer = new CustomerModel();
		customer.setLoyaltyPointAmount(0);
		customer.setSessionCurrency(currency);
		when(userService.getCurrentUser()).thenReturn(customer);

		final CartModel cart = new CartModel();
		cart.setTotalPrice(TOTAL_PRICE);
		when(cartService.getSessionCart()).thenReturn(cart);

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
		when(userService.getCurrentUser()).thenReturn(customer);

		final CartModel cart = new CartModel();
		cart.setTotalPrice(TOTAL_PRICE);
		when(cartService.getSessionCart()).thenReturn(cart);

		loyaltyPointService.collectLoyaltyPoints();
		assertEquals(COLLECT_AMOUNT, customer.getLoyaltyPointAmount());

		config.setType(LoyaltyPointConfigurationType.RELATIVE);
		loyaltyPointService.collectLoyaltyPoints();

		assertEquals((int) (TOTAL_PRICE * COLLECT_PERCENTAGE / 100) + COLLECT_AMOUNT, customer.getLoyaltyPointAmount());
	}

	@Test
	public void testReduceCustomerLoyaltyPointAmount()
	{
		final CartModel cart = new CartModel();
		cart.setLoyaltyPointAmount(SELECTED_LOYALTY_POINT_AMOUNT);
		modelService.save(cart);

		final CustomerModel customer = new CustomerModel();
		customer.setLoyaltyPointAmount(CUSTOMER_LOYALTY_POINT_AMOUNT);
		customer.setSessionCurrency(currency);
		modelService.save(customer);
		userService.setCurrentUser(customer);

		loyaltyPointService.reduceCustomerLoyaltyPointAmount(cart);
		assertEquals(CUSTOMER_LOYALTY_POINT_AMOUNT - SELECTED_LOYALTY_POINT_AMOUNT, customer.getLoyaltyPointAmount());
	}

	@Test
	public void testSubtractLoyaltyPointPartFromTotals()
	{
		final OrderModel order = new OrderModel();
		order.setTotalPrice(TOTAL_PRICE);
		order.setLoyaltyPointAmount(SELECTED_LOYALTY_POINT_AMOUNT);
		modelService.save(order);

		final UserModel user = new EmployeeModel();
		modelService.save(user);
		userService.setCurrentUser(user);

		loyaltyPointService.subtractLoyaltyPointPartFromTotals(order);
		assertEquals(Double.valueOf(TOTAL_PRICE), order.getTotalPrice());

		final CustomerModel customer = new CustomerModel();
		customer.setLoyaltyPointAmount(CUSTOMER_LOYALTY_POINT_AMOUNT);
		customer.setSessionCurrency(currency);
		modelService.save(customer);
		userService.setCurrentUser(customer);

		loyaltyPointService.subtractLoyaltyPointPartFromTotals(order);
		assertEquals(Double.valueOf(TOTAL_PRICE), order.getTotalPrice());
		assertEquals(CUSTOMER_LOYALTY_POINT_AMOUNT, customer.getLoyaltyPointAmount());
	}

}
