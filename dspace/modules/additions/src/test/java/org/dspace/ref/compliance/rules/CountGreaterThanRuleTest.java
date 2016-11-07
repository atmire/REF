package org.dspace.ref.compliance.rules;

import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.ref.compliance.definition.model.Value;
import org.dspace.ref.compliance.result.RuleComplianceResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CountGreaterThanRuleTest {

    @Mock
    private Context context;

    @Mock
    private Item item;

    private Value value;

    @Before
    public void setUp() {
        when(item.getMetadata(eq("test"), eq("case"), eq("valid"), anyString())).thenReturn(new DCValue[] {new DCValue(), new DCValue(), new DCValue()});
        when(item.getMetadata(eq("test"), eq("case"), eq("invalid"), anyString())).thenReturn(new DCValue[] {});
        value = new Value();
    }

    @Test
    public void testDoValidationValid() throws Exception {
        value.setValue("2");
        CountGreaterThanRule rule = new CountGreaterThanRule("test field", "test.case.valid", Arrays.asList(value));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(true, result.isCompliant());
    }

    @Test
    public void testDoValidationInvalid() throws Exception {
        value.setValue("2");
        CountGreaterThanRule rule = new CountGreaterThanRule("test field", "test.case.invalid", Arrays.asList(value));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testDoValidationNoThreshold() throws Exception {
        CountGreaterThanRule rule = new CountGreaterThanRule("test field", "test.case.invalid", null);

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testDoValidationNoNumericThreshold() throws Exception {
        value.setValue("foobar");
        CountGreaterThanRule rule = new CountGreaterThanRule("test field", "test.case.invalid", Arrays.asList(value));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }
}