package com.trizic.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class ModelResponseTest extends ValidatorBasedTest {

    private List<AssetAllocation> generateAssetAllocation( int count, int reserve ) {
        List<AssetAllocation> list = new ArrayList<AssetAllocation>( count );
        BigDecimal percentage = new BigDecimal( 100 - reserve ).divide( new BigDecimal( count ) );
        for ( int i = 0; i < count; i++ ) {
            list.add(
                new AssetAllocation()
                    .withSymbol( String.valueOf( i ) )
                    .withPercentage( percentage.doubleValue() )
                );
        }
        return list;
    }

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
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              10,
              5,
              generateAssetAllocation( 2, 10 ),
              "advisor1",
              UUID.randomUUID().toString(),
              "00:00:99T00:00:00Z"
            }, { 
              "testModel",
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              20,
              5,
              generateAssetAllocation( 4, 20 ),
              "advisor1",
              "",
              "00:00:99T00:00:00Z"
            }, { 
              "testModel",
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              50,
              5,
              generateAssetAllocation( 10, 50 ),
              "",
              UUID.randomUUID().toString(),
              "00:00:99T00:00:00Z"
            },
        };
    }

    @Test(dataProvider = "valid")
    public void validation( String name, String description,
            ModelResponse.ModelType modelType, ModelResponse.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations,
            String advisorId, String guid, String createdOn )
    {
        ModelResponse model = new ModelResponse()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations )
            .withAdvisorId( advisorId )
            .withGuid( guid )
            .withCreatedOn( createdOn );

        Set<ConstraintViolation<ModelResponse>> violations = factory.getValidator().validate( model );
        assert violations != null : "Violations is null";
        assert violations.size() == 0 : "Unexpected number of violations: " + violations.size();
    }


    @Test(dataProvider = "valid")
    public void holdingAndAssetAllocation( String name, String description,
            ModelResponse.ModelType modelType, ModelResponse.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations,
            String advisorId, String guid, String createdOn )
    {
        ModelResponse model = new ModelResponse()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations )
            .withAdvisorId( advisorId )
            .withGuid( guid )
            .withCreatedOn( createdOn );

        Util.validateHoldingAndAssetAllocation( model );
    }

    @DataProvider(name = "invalid")
    Object [][] invalid() {
        return new Object [][] {
            { 
              null,
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              null,
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              null,
              ModelResponse.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              null,
              10,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              null,
              5,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              10,
              null,
              Collections.singletonList( new AssetAllocation().withSymbol( "foo" ).withPercentage( 90D ) )
            }, { 
              "testModel",
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              10,
              5,
              null
            }, { 
              "testModel",
              "Test Model",
              ModelResponse.ModelType.QUALIFIED,
              ModelResponse.RebalanceFrequency.ANNUAL,
              10,
              5,
              Collections.EMPTY_LIST
            }
        };
    }

    @Test(dataProvider = "invalid")
    public void invalidation( String name, String description,
            ModelResponse.ModelType modelType, ModelResponse.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations )
    {
        int count = 0;
        ModelResponse model = new ModelResponse()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations );

        Set<ConstraintViolation<ModelResponse>> violations = factory.getValidator().validate( model );
        assert violations != null : "Violations is null";
        count += violations.size();
        assert count > 0 : "Expected validation errors but found none";
    }

    @DataProvider(name = "holdingAndAssetAllocation")
    Object [][] holdingAndAssetAllocation() {
        return new Object [][] {
            { 
                "testModel",
                "Test Model",
                ModelResponse.ModelType.QUALIFIED,
                ModelResponse.RebalanceFrequency.ANNUAL,
                15,
                5,
                generateAssetAllocation( 2, 10 )
            }, { 
                "testModel",
                "Test Model",
                ModelResponse.ModelType.QUALIFIED,
                ModelResponse.RebalanceFrequency.ANNUAL,
                10,
                5,
                generateAssetAllocation( 4, 20 )
            }
        };
    }

    @Test(dataProvider = "holdingAndAssetAllocation", expectedExceptions = RuntimeException.class)
    public void validateHoldingAndAssetAllocation_invalid( String name, String description,
            ModelResponse.ModelType modelType, ModelResponse.RebalanceFrequency rebalanceFrequency,
            Integer cashHoldingPercentage, Integer driftPercentage,
            List<AssetAllocation> assetAllocations )
    {
        ModelResponse model = new ModelResponse()
            .withName( name )
            .withDescription( description )
            .withModelType( modelType )
            .withRebalanceFrequency( rebalanceFrequency )
            .withCashHoldingPercentage( cashHoldingPercentage )
            .withDriftPercentage( driftPercentage )
            .withAssetAllocations( assetAllocations );

        Util.validateHoldingAndAssetAllocation( model );
    }
}
