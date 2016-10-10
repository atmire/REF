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
public class FieldHasValueRuleTest {

    @Mock
    private Context context;

    @Mock
    private Item item;

    private Value value1;
    private Value value2;

    @Before
    public void setUp() {
        DCValue value = new DCValue();
        value.value = "abc123";
        when(item.getMetadata(eq("test"), eq("case"), eq("value"), anyString())).thenReturn(new DCValue[] {value});

        when(item.getMetadata(eq("test"), eq("case"), eq("invalid"), anyString())).thenReturn(new DCValue[] {});

        DCValue invalid2 = new DCValue();
        invalid2.value = "";
        when(item.getMetadata(eq("test"), eq("case"), eq("invalid2"), anyString())).thenReturn(new DCValue[] {invalid2});

        value1 = new Value();
        value2 = new Value();
    }

    @Test
    public void testValidation() {
        value1.setValue("def567");
        value2.setValue("abc123");
        FieldHasValueRule rule = new FieldHasValueRule("test field", "test.case.value", Arrays.asList(value1, value2));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(true, result.isCompliant());
    }

    @Test
    public void testValidationValueNotInList() {
        value1.setValue("def567");
        value2.setValue("ghi890");
        FieldHasValueRule rule = new FieldHasValueRule("test field", "test.case.value", Arrays.asList(value1, value2));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testValidationValueInvalid() {
        value1.setValue("def567");
        value2.setValue("abc123");
        FieldHasValueRule rule = new FieldHasValueRule("test field", "test.case.invalid", Arrays.asList(value1, value2));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testValidationValueInvalid2() {
        value1.setValue("def567");
        value2.setValue("abc123");
        FieldHasValueRule rule = new FieldHasValueRule("test field", "test.case.invalid2", Arrays.asList(value1, value2));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }
}