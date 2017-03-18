package com.trizic.api.service.v1;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.testng.annotations.BeforeClass;


public abstract class ValidatorBasedTest {

    protected ValidatorFactory factory = null;

    /**
     * Bootstrap the validator framework.
     */
    @BeforeClass
    void bootstrapJsr303() {
        factory = Validation.buildDefaultValidatorFactory();
    }

}
