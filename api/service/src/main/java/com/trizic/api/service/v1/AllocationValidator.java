package com.trizic.api.service.v1;

import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/*
 * allocation.percentage.total.invalid
 */
public class AllocationValidator implements ConstraintValidator<ValidAllocation, Model> {

    /* 
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize( ValidAllocation constraintAnnotation ) {}

    /* 
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid( Model value, ConstraintValidatorContext context ) {
        if ( value == null ) {
            // can't do our thing so nothing to report
            return true;
        }

        // ensure cash allocation + E(asset allocations percentage) == 100
        // assume this validation occurs after all the various NULL checking
        Integer val = value.getCashHoldingPercentage();
        BigDecimal sum = val == null ? BigDecimal.ZERO : new BigDecimal( val );
        for ( AssetAllocation asset : value.getAssetAllocations() ) {
            Double d = asset.getPercentage();
            sum = sum.add( d == null ? BigDecimal.ZERO : new BigDecimal( d ) );
        }
        return sum.compareTo( new BigDecimal( 100 ) ) == 0;
    }
}
