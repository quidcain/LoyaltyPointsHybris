package de.hybris.platform.addons.loyaltypointaddon.impl;

import de.hybris.platform.addons.loyaltypointaddon.LoyaltyPointService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.util.TaxValue;

import java.util.Map;
import java.util.Set;

public class LoyaltyCalculationService extends DefaultCalculationService
{
    private LoyaltyPointService loyaltyPointService;

    @Override
    protected void calculateTotals(AbstractOrderModel abstractOrder, boolean recalculate, Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap) throws CalculationException
    {
        super.calculateTotals(abstractOrder, recalculate, taxValueMap);
        loyaltyPointService.subtractLoyaltyPointPartFromTotals(abstractOrder);
    }

    public void setLoyaltyPointService(LoyaltyPointService loyaltyPointService)
    {
        this.loyaltyPointService = loyaltyPointService;
    }
}
