/**
 *
 */
package de.hybris.platform.addons.loyaltypointaddon.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;


public class LoyaltyPointConfigurationModelValidationTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private ValidationService validationService;


	@Test
	public void testMinMaxConstaint()
	{
		final LoyaltyPointConfigurationModel lpConfigModel = modelService.create(LoyaltyPointConfigurationModel.class);
		Set<HybrisConstraintViolation> violations;

		lpConfigModel.setCollectPercentage(60);
		lpConfigModel.setOrderPercentage(60);
		violations = validationService.validate(lpConfigModel);
		assertTrue(violations.size() == 0);

		lpConfigModel.setCollectPercentage(101);
		lpConfigModel.setOrderPercentage(101);
		violations = validationService.validate(lpConfigModel);
		assertTrue(violations != null && violations.size() > 0);
		assertEquals(2, violations.size());
		for (final HybrisConstraintViolation hybrisConstraintViolation : violations)
		{
			System.out.println(hybrisConstraintViolation.getProperty() + ":" + hybrisConstraintViolation.getLocalizedMessage());
		}

		lpConfigModel.setCollectPercentage(-1);
		lpConfigModel.setOrderPercentage(-1);
		lpConfigModel.setCollectAmount(-1);
		violations = validationService.validate(lpConfigModel);
		assertTrue(violations != null && violations.size() > 0);
		assertEquals(3, violations.size());
		for (final HybrisConstraintViolation hybrisConstraintViolation : violations)
		{
			System.out.println(hybrisConstraintViolation.getProperty() + ":" + hybrisConstraintViolation.getLocalizedMessage());
		}
	}

}