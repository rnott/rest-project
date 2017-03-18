package com.trizic.api.service.v1;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class ModelTest extends ValidatorBasedTest {

    /*
     * Zero-length string values are considered valid as the schema
     * does not specify a minimum length and the code generator does
     * not assume one even when the property is required.
     */
    @DataProvider(name = "valid")
    Object [][] valid() {
        return new Object [][] {
            { 
              "testModel",
              "Test Model",
              Model.ModelType.QUALIFIED,
              Model.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            },
        };
    }

    @Test(dataProvider = "valid")
    public void validation( String name, String description,
            Model.ModelType modelType, Model.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations )
    {
        Model model = new Model()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations );

        Set<ConstraintViolation<Model>> violations = factory.getValidator().validate( model );
        assert violations != null : "Violations is null";
        assert violations.size() == 0 : "Unexpected number of violations: " + violations.size();
    }

    @DataProvider(name = "validHoldingAndAssetAllocation")
    Object [][] validHoldingAndAssetAllocation() {
        return new Object [][] {
            { 
              "testModel",
              "Test Model",
              Model.ModelType.QUALIFIED,
              Model.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            },
        };
    }

    @Test(dataProvider = "validHoldingAndAssetAllocation")
    public void holdingAndAssetAllocation( String name, String description,
            Model.ModelType modelType, Model.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations )
    {
        Model model = new Model()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations );

        assert Util.validateHoldingAndAssetAllocation( model ) : "Cash holding and asset allocation is invalid";
    }

    @DataProvider(name = "invalid")
    Object [][] invalid() {
        return new Object [][] {
            { 
              null,
              "Test Model",
              Model.ModelType.QUALIFIED,
              Model.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              null,
              Model.ModelType.QUALIFIED,
              Model.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              null,
              Model.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              Model.ModelType.QUALIFIED,
              null,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              Model.ModelType.QUALIFIED,
              Model.RebalanceFrequency.ANNUAL,
              null,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              Model.ModelType.QUALIFIED,
              Model.RebalanceFrequency.ANNUAL,
              10,
              null,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              Model.ModelType.QUALIFIED,
              Model.RebalanceFrequency.ANNUAL,
              10,
              5,
              null
            }
        };
    }

    @Test(dataProvider = "invalid")
    public void invalidation( String name, String description,
            Model.ModelType modelType, Model.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations )
    {
        int count = 0;
        Model model = new Model()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations );

        Set<ConstraintViolation<Model>> violations = factory.getValidator().validate( model );
        assert violations != null : "Violations is null";
        count += violations.size();
        assert count > 0 : "Expected validation errors but found none";
    }

    @DataProvider(name = "invalidHoldingAndAssetAllocation")
    Object [][] invalidHoldingAndAssetAllocation() {
        return new Object [][] {
            { 
                "testModel",
                "Test Model",
                Model.ModelType.QUALIFIED,
                Model.RebalanceFrequency.ANNUAL,
                10,
                5,
                Collections.EMPTY_LIST
            }, { 
                "testModel",
                "Test Model",
                Model.ModelType.QUALIFIED,
                Model.RebalanceFrequency.ANNUAL,
                10,
                5,
                Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 91D ) )
            }
        };
    }

    @Test(dataProvider = "invalidHoldingAndAssetAllocation")
    public void holdingAndAssetAllocation_Invalid( String name, String description,
            Model.ModelType modelType, Model.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations )
    {
        Model model = new Model()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations );

        assert Util.validateHoldingAndAssetAllocation( model ) == false :
            "Expected validation errors but found none";
    }

}
