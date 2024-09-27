package com.sap.cloud.environment.servicebinding.autoconfiguration;

import javax.annotation.Nonnull;

import com.sap.cloud.environment.servicebinding.environment.SapServiceBindingEnvironmentPostProcessor;
import com.sap.cloud.environment.servicebinding.environment.SapServiceBindingsPropertiesAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import com.sap.cloud.environment.servicebinding.SapServiceBindingsProperties.ServiceBindingProperties;
import com.sap.cloud.environment.servicebinding.TestResource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SapServiceBindingEnvironmentPostProcessorTest
{
    final SapServiceBindingEnvironmentPostProcessor cut = new SapServiceBindingEnvironmentPostProcessor();

    @BeforeEach
    void setup()
    {
        SapServiceBindingsPropertiesAccessor.setServiceBindingsProperties(new HashMap<>());
    }

    @Test
    void bindServiceBindingsProperties()
    {
        final MockEnvironment mockEnvironment = TestResource.getAllBindingsProperties();

        cut.postProcessEnvironment(mockEnvironment, null);

        assertNotNull(SapServiceBindingsPropertiesAccessor.getServiceBindingsProperties());
        assertContainsXsuaaBindingProperties(
            SapServiceBindingsPropertiesAccessor.getServiceBindingsProperties().get("xsuaa"));
        assertContainsServiceManagerBindingProperties(
            SapServiceBindingsPropertiesAccessor.getServiceBindingsProperties().get("service-manager"));
    }

    @Test
    void bindEmptyServiceBindingsProperties()
    {
        final MockEnvironment mockEnvironment = TestResource.getEmptyProperties();

        cut.postProcessEnvironment(mockEnvironment, null);

        assertNotNull(SapServiceBindingsPropertiesAccessor.getServiceBindingsProperties());
        assertTrue(SapServiceBindingsPropertiesAccessor.getServiceBindingsProperties().isEmpty());
    }

    private void assertContainsXsuaaBindingProperties( @Nonnull final ServiceBindingProperties xsuaaBindingProperties )
    {
        assertEquals("xsuaa-test", xsuaaBindingProperties.getName());
        assertEquals("broker", xsuaaBindingProperties.getPlan());
        assertNotNull(xsuaaBindingProperties.getCredentials());
    }

    private void assertContainsServiceManagerBindingProperties(
        @Nonnull final ServiceBindingProperties serviceManagerBindingProperties )
    {
        assertEquals("service-manager-test", serviceManagerBindingProperties.getName());
        assertEquals("standard", serviceManagerBindingProperties.getPlan());
        assertNotNull(serviceManagerBindingProperties.getCredentials());
    }
}
