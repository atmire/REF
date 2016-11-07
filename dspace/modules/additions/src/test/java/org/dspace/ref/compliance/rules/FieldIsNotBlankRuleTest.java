package org.dspace.ref.compliance.rules;

import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.ref.compliance.result.RuleComplianceResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FieldIsNotBlankRuleTest {

    @Mock
    private Context context;

    @Mock
    private Item item;

    @Before
    public void setUp() {
        DCValue value = new DCValue();
        value.value = "abc123";
        when(item.getMetadata(eq("test"), eq("case"), eq("value"), anyString())).thenReturn(new DCValue[] {value});

        when(item.getMetadata(eq("test"), eq("case"), eq("invalid"), anyString())).thenReturn(new DCValue[] {});

        DCValue invalid2 = new DCValue();
        invalid2.value = "";
        when(item.getMetadata(eq("test"), eq("case"), eq("invalid2"), anyString())).thenReturn(new DCValue[] {invalid2});
    }

    @Test
    public void testValidation() {
        FieldIsNotBlankRule rule = new FieldIsNotBlankRule("test field", "test.case.value");

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(true, result.isCompliant());
    }


    @Test
    public void testValidationValueInvalid() {
        FieldIsNotBlankRule rule = new FieldIsNotBlankRule("test field", "test.case.invalid");

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testValidationValueInvalid2() {
        FieldIsNotBlankRule rule = new FieldIsNotBlankRule("test field", "test.case.invalid2");

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }
}