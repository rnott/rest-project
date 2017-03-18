package com.trizic.api.service.v1;

import java.math.BigDecimal;


public class Util {

    /**
     * Ensure cash allocation + E(asset allocations percentage) == 100.
     * Assumption here is that all field level validations have already
     * passed, particularly NULL checking so we don't have to take that
     * into consideration.
     * 
     * Since cash holding percentage is double, we choose to use of BigDecimal
     * since doubles are notorious for rounding errors.
     * 
     * @param model the model to validate
     * @return <code>true</code> if the model is valid, <code>false</code> if
     * the validation rule is not satisfied.
     */
    public static boolean validateHoldingAndAssetAllocation( Model model ) {
        // ensure cash allocation + E(asset allocations percentage) == 100
        // assume this validation occurs after all the various NULL checking
        BigDecimal sum = new BigDecimal( model.getCashHoldingPercentage() );
        for ( AssetAllocation asset : model.getAssetAllocations() ) {
            sum = sum.add( new BigDecimal( asset.getPercentage() ) );
        }
        return sum.compareTo( new BigDecimal( 100 ) ) == 0;
    }

    /**
     * Ensure cash allocation + E(asset allocations percentage) == 100.
     * Assumption here is that all field level validations have already
     * passed, particularly NULL checking so we don't have to take that
     * into consideration.
     * 
     * Since cash holding percentage is double, we choose to use of BigDecimal
     * since doubles are notorious for rounding errors.
      * 
     * This validation is performed on data generated internally so exceptions
     * are thrown as they should never happen (they're bugs).
    * 
     * @param model the model to validate
     * @return <code>true</code> if the model is valid, <code>false</code> if
     * the validation rule is not satisfied.
     */
    public static void validateHoldingAndAssetAllocation( ModelResponse model ) {
        // ensure cash allocation + E(asset allocations percentage) == 100
        // assume this validation occurs after all the various NULL checking
        BigDecimal sum = new BigDecimal( model.getCashHoldingPercentage() );
        for ( AssetAllocation asset : model.getAssetAllocations() ) {
            sum = sum.add( new BigDecimal( asset.getPercentage() ) );
        }
        if ( sum.compareTo( new BigDecimal( 100 ) ) != 0 ) {
            throw new RuntimeException( "Sum of cash holding and asset allocations not 100%: " + sum.toString() );
        }
    }

    /**
     * Ensure page metadata is consistent.
     * Assumption here is that all field level validations have already
     * passed, particularly NULL checking so we don't have to take that
     * into consideration.
     * 
     * This validation is performed on data generated internally so exceptions
     * are thrown as they should never happen (they're bugs).
     * 
     * @param page the page to validate
     * @return <code>true</code> if the metadata is valid, <code>false</code> if
     * any validation rules are not satisfied.
     */
    public static void validatePageMetadata( ModelPage page ) {
        int pageNumber = page.getPageNumber();
        int numberOfPages = page.getNumberOfPages();
        int pageSize = page.getPageSize();
        int totalElements = page.getTotalNumberOfElements();
        // page number [0, # of pages]
        if ( Math.min( 0, pageNumber ) < 0
             || Math.max( pageNumber, numberOfPages ) > numberOfPages ) {
            throw new RuntimeException( "Page number out of range: " + pageNumber );
        }
        // returned models <= page size
        if ( page.getPage().size() > pageSize ) {
            throw new RuntimeException( "Number of returned models exceeds the page size: " + page.getPage().size() );
        }
        // page # * per page <= total elements
        if ( (pageSize * (pageNumber - 1) + 1) > totalElements ) {
            throw new RuntimeException( "Page number and/or total elements incorrect, suggests more total elements than are possible" );
        }
    }
}
