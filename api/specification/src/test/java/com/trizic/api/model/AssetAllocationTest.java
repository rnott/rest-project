package com.trizic.api.model;

import java.util.Set;
import javax.validation.ConstraintViolation;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AssetAllocationTest extends ValidatorBasedTest {

    @DataProvider(name = "additionalProperties")
    Object [][] additionalProperties() {
        return new Object [][] {
            { "one", null },
            { "two", "two" },
            { "three", Boolean.TRUE },
            { "four", new Object() }
        };
    }

    /*
     * Unsure why this attribute got generated since
     * additionalProperties = false
     */
    @Test(dataProvider = "additionalProperties")
    public void addtionalProperty( String key, Object value ) {
        AssetAllocation obj = new AssetAllocation();

        obj.setAdditionalProperty( key, value );
        // null is allowed
        if ( value != null ) {
            assert obj.getAdditionalProperties().get( key ) != null : "Excepted additional property not found: " + key;
            assert value.equals( obj.getAdditionalProperties().get( key ) ) : "Unexpected additional property value: " + key;

            assert new AssetAllocation().withAdditionalProperty( key, value )
                .getAdditionalProperties().get( key ) == value : "Unexpected additional property value: " + key;
        }
    }

    /*
     * TODO: percentage should have min/max values specified in the
     * schema (0-100) so that corresponding annotations are generated
     * and validated.
     */
    @DataProvider(name = "percentages")
    Object [][] percentages() {
        return new Object [][] {
            { 0D },
            { 1D },
            { 99D },
            { 100D },
            { 56.9D },
            { 101D },  // should not be valid
            { -1D }    // should not be valid
        };
    }

    @Test(dataProvider = "percentages")
    public void percentage( Double value ) {
        AssetAllocation obj = new AssetAllocation();
        obj.setPercentage( value );
        assert obj.getPercentage().equals( value ) : "Unexpected percentage: " + obj.getPercentage()
            + " expected: " + value;

        assert new AssetAllocation().withPercentage( value )
            .getPercentage().equals( value ) : "Unexpected percentage";
    }

    /*
     * Zero-length string values are considered valid as the schema
     * does not specify a minimum length and the code generator does
     * not assume one even when the property is required.
     */
    @DataProvider(name = "symbols")
    Object [][] symbols() {
        return new Object [][] {
            { "aaa" },
            { "bbb" },
            { "" }
        };
    }

   @Test(dataProvider = "symbols")
    public void symbol( String value ) {
        AssetAllocation obj = new AssetAllocation();
        obj.setSymbol( value );
        assert obj.getSymbol().equals( value ) : "Unexpected symbol: " + obj.getSymbol()
            + " expected: " + value;

        assert new AssetAllocation().withSymbol( value )
            .getSymbol().equals( value ) : "Unexpected symbol";
    }

   @DataProvider(name = "valid")
   Object [][] valid() {
       return new Object [][] {
           { "aaa", 0D },
           { "bbb", 1D },
           { "ccc", 99D },
           { "ddd", 100D },
           { "fff", 56.9D },
       };
   }

   @Test(dataProvider = "valid")
   public void validation( String symbol, Double percentage ) {
       Set<ConstraintViolation<AssetAllocation>> violations = factory.getValidator()
           .validate( new AssetAllocation().withSymbol( symbol ).withPercentage( percentage ) );
       assert violations != null : "Violations is null";
       assert violations.size() == 0 : "Unexpected number of violations: " + violations.size();
   }

   @DataProvider(name = "invalid")
   Object [][] invalid() {
       return new Object [][] {
           { "aaa", null, 1 },
           { null, 1D, 1 },
           { null, null, 2 }
       };
   }

   @Test(dataProvider = "invalid")
   public void invalidation( String symbol, Double percentage, int expect ) {
       Set<ConstraintViolation<AssetAllocation>> violations = factory.getValidator()
           .validate( new AssetAllocation().withSymbol( symbol ).withPercentage( percentage ) );
       assert violations != null : "Violations set is null";
       assert violations.size() == expect : "Unexpected number of violations: " + violations.size()
           + " expected: " + expect;

       for ( ConstraintViolation<AssetAllocation> violation : violations ) {
           System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
       }
   }
}
