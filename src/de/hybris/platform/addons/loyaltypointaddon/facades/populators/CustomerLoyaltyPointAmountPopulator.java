/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.facades.populators;


import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class CustomerLoyaltyPointAmountPopulator implements Populator<CustomerModel, CustomerData>
{

	@Override
	public void populate(final CustomerModel source, final CustomerData target) throws ConversionException
	{
		target.setLoyaltyPointAmount(source.getLoyaltyPointAmount());
	}

}
