/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/my-account/loyaltypoints")
public class AccountLoyaltyPointAmountPageController extends AbstractSearchPageController
{
	private static final String LOYALTYPOINTS_CMS_PAGE = "loyaltyPointAmount";

	@GetMapping
	public String loyaltypoints(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("customerData", getCustomerFacade().getCurrentCustomer());
		storeCmsPageInModel(model, getContentPageForLabelOrId(LOYALTYPOINTS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(LOYALTYPOINTS_CMS_PAGE));
		return getViewForPage(model);
	}
}
