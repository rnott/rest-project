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


public class ModelPageTest extends ValidatorBasedTest {

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

    private List<ModelResponse> generateModels( int count, int assets, int reserve ) {
        List<ModelResponse> list = new ArrayList<ModelResponse>( count );
        for ( int i = 0; i < count; i++ ) {
            list.add(
                new ModelResponse()
                    .withName( "test" )
                    .withDescription( "Test" )
                    .withModelType( ModelResponse.ModelType.QUALIFIED )
                    .withRebalanceFrequency( ModelResponse.RebalanceFrequency.ANNUAL )
                    .withCashHoldingPercentage( reserve )
                    .withDriftPercentage( 10 )
                    .withAssetAllocations( generateAssetAllocation( assets, reserve ) )
                    .withAdvisorId( "1" )
                    .withGuid( UUID.randomUUID().toString() )
                    .withCreatedOn( "00:00:99T00:00:00Z" )
            );
        }
        return list;
    }

    @DataProvider(name = "valid")
    Object [][] valid() {
        return new Object [][] {
            { 1, 1, 10, 1, generateModels( 1, 2, 10 ) },
            { 1, 1, 10, 10, generateModels( 10, 4, 20 ) },
            { 0, 0, 10, 0, Collections.EMPTY_LIST }
        };
    }

    @Test(dataProvider = "valid")
    public void validation( Integer numberOfPages, Integer pageNumber, Integer pageSize,
            Integer totalNumberOfElements, List<Object> entries )
    {
        ModelPage page = new ModelPage()
            .withNumberOfPages( numberOfPages )
            .withPageNumber( pageNumber )
            .withPageSize( pageSize )
            .withTotalNumberOfElements( totalNumberOfElements )
            .withPage( entries );

        Set<ConstraintViolation<ModelPage>> violations = factory.getValidator().validate( page );
        assert violations != null : "Violations is null";
        assert violations.size() == 0 : "Unexpected number of violations: " + violations.size()
            + " " + violations;
    }

    //@Test(dataProvider = "valid")
    public void validatePageMetadata( Integer numberOfPages, Integer pageNumber, Integer pageSize,
            Integer totalNumberOfElements, List<Object> entries )
    {
        ModelPage page = new ModelPage()
            .withNumberOfPages( numberOfPages )
            .withPageNumber( pageNumber )
            .withPageSize( pageSize )
            .withTotalNumberOfElements( totalNumberOfElements )
            .withPage( entries );

        Util.validatePageMetadata( page );
    }

    @DataProvider(name = "invalid")
    Object [][] invalid() {
        return new Object [][] {
            { null, 0, 10, 0, Collections.EMPTY_LIST },
            { 0, null, 10, 0, Collections.EMPTY_LIST },
            { 0, 0, null, 0, Collections.EMPTY_LIST },
            { 0, 0, 10, null, Collections.EMPTY_LIST },
            { 0, 0, 10, 0, null }
        };
    }

    @Test(dataProvider = "invalid")
    public void invalidation( Integer numberOfPages, Integer pageNumber, Integer pageSize,
            Integer totalNumberOfElements, List<Object> entries )
    {
        ModelPage page = new ModelPage()
            .withNumberOfPages( numberOfPages )
            .withPageNumber( pageNumber )
            .withPageSize( pageSize )
            .withTotalNumberOfElements( totalNumberOfElements )
            .withPage( entries );

        Set<ConstraintViolation<ModelPage>> violations = factory.getValidator().validate( page );
        assert violations != null : "Violations is null";
        assert violations.size() > 0 : "Expected violations, got none";
    }

    @DataProvider(name = "invalidPageMetadata")
    Object [][] invalidPageMetadata() {
        return new Object [][] {
            { 1, -1, 10, 0, Collections.EMPTY_LIST },
            { 0, 1, 10, 0, Collections.EMPTY_LIST },
            { 2, 1, 5, 10, generateModels( 10, 2, 10 ) },
            { 3, 3, 5, 10, generateModels( 5, 2, 10 ) },
        };
    }

    @Test(dataProvider = "invalidPageMetadata", expectedExceptions = RuntimeException.class)
    public void validatePageMetadata_invalid( Integer numberOfPages, Integer pageNumber, Integer pageSize,
            Integer totalNumberOfElements, List<Object> entries )
    {
        ModelPage page = new ModelPage()
            .withNumberOfPages( numberOfPages )
            .withPageNumber( pageNumber )
            .withPageSize( pageSize )
            .withTotalNumberOfElements( totalNumberOfElements )
            .withPage( entries );

        Util.validatePageMetadata( page );
    }
}

