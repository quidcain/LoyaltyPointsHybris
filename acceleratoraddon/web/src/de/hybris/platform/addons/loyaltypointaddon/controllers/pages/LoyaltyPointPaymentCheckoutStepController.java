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
import de.hybris.platform.addons.loyaltypointaddon.forms.LoyaltyPointAmountForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;


@Controller
@RequestMapping(value = "/checkout/multi/addon/loyaltypoint-payment")
public class LoyaltyPointPaymentCheckoutStepController extends AbstractCheckoutStepController
{

	private final static String LOYALTYPOINT_PAYMENT = "loyaltypoint-payment";

	private LoyaltyPointService loyaltyPointService;
	private SessionService sessionService;
	private CartService cartService;
	private ModelService modelService;

	@Override
	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		setupAddPaymentPage(model);
		final LoyaltyPointAmountForm form = new LoyaltyPointAmountForm();
		form.setLoyaltyPointAmount(0);
		model.addAttribute(form);
		model.addAttribute("customerData", getCustomerFacade().getCurrentCustomer());
		return LoyaltypointaddonControllerConstants.LoyaltypointPaymentPage;
	}

	@RequestMapping(method = RequestMethod.POST)
	@RequireHardLogIn
	public String doSelectDeliveryMode(@ModelAttribute @Valid final LoyaltyPointAmountForm loyaltyPointAmountForm,
			final BindingResult bindingResult, final Model model) throws CMSItemNotFoundException
	{
		setupAddPaymentPage(model);
		model.addAttribute("customerData", getCustomerFacade().getCurrentCustomer());
		if (bindingResult.hasErrors())
		{
			return LoyaltypointaddonControllerConstants.LoyaltypointPaymentPage;
		}
		CartModel cart = cartService.getSessionCart();
		cart.setLoyaltyPointAmount(loyaltyPointAmountForm.getLoyaltyPointAmount());
		cartService.setSessionCart(cart);
		modelService.save(cart);
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

	protected void setupAddPaymentPage(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("metaRobots", "noindex,nofollow");
		prepareDataForPage(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentMethod.breadcrumb"));
		final ContentPageModel contentPage = getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL);
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
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

	@Resource
	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}

	@Resource
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}
}
