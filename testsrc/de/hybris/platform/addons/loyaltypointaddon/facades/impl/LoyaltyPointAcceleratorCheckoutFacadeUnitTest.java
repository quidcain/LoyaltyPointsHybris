/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.facades.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.strategies.impl.DefaultCheckoutCustomerStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LoyaltyPointAcceleratorCheckoutFacadeUnitTest
{
	@InjectMocks
	@Spy
	private final LoyaltyPointAcceleratorCheckoutFacade loyaltyPointAcceleratorCheckoutFacade = new LoyaltyPointAcceleratorCheckoutFacade();

	@Mock
	private LoyaltyPointService loyaltyPointService;
	@Mock
	private CartService cartService;
	@Mock
	private CartFacade mockCartFacade;
	@Mock
	private DefaultCheckoutCustomerStrategy checkoutCustomerStrategy;
	@Mock
	private CartModel cartModel;
	@Mock
	private OrderModel orderModel;
	@Test
	public void testPlaceOrder() throws InvalidCartException
	{
		loyaltyPointAcceleratorCheckoutFacade.beforePlaceOrder(cartModel);
		verify(loyaltyPointService).collectLoyaltyPoints();
		verify(loyaltyPointService).payPartWithLoyaltyPoints(orderModel);

		loyaltyPointAcceleratorCheckoutFacade.placeOrder();
		verify(loyaltyPointAcceleratorCheckoutFacade).beforePlaceOrder(cartModel);
	}

}
