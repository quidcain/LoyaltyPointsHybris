/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.addons.loyaltypointaddon.controllers.LoyaltypointaddonControllerConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/checkout/multi/addon/loyaltypoint-payment")
public class LoyaltyPointPaymentCheckoutStepController extends AbstractCheckoutStepController
{

	private final static String LOYALTYPOINT_PAYMENT = "loyaltypoint-payment";

	private CartService cartService;
	private LoyaltyPointService loyaltyPointService;
	private SessionService sessionService;

	@Resource
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Resource
	public void setLoyaltyPointService(final LoyaltyPointService loyaltyPointService)
	{
		this.loyaltyPointService = loyaltyPointService;
	}

	@Resource
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryMethod.breadcrumb"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		return LoyaltypointaddonControllerConstants.LoyaltypointPaymentPage;
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectDeliveryMode(@RequestParam("loyaltypoint_amount") final String selectedCartPercentage)
	{
		if (StringUtils.isNotEmpty(selectedCartPercentage))
		{
			//getCheckoutFacade().setDeliveryMode(selectedDeliveryMethod);
			if (!loyaltyPointService.isMaximumOrderPercentageExcedeed())
			{
				System.out.println(selectedCartPercentage);
				final int intCartPercentage = Integer.parseInt(selectedCartPercentage);
				sessionService.setAttribute("loyaltypoint_amount", intCartPercentage);
			}
		}

		return getCheckoutStep().nextStep();
	}

	@Override
	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@Override
	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(LOYALTYPOINT_PAYMENT);
	}

}
