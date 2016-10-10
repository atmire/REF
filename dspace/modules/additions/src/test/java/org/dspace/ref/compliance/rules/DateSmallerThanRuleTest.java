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
public class DateSmallerThanRuleTest {

    @Mock
    private Context context;

    @Mock
    private Item item;

    private Value value;

    @Before
    public void setUp() {
        DCValue from = new DCValue();
        from.value = "2016-05-01T11:21:13Z";
        when(item.getMetadata(eq("test"), eq("case"), eq("date"), anyString())).thenReturn(new DCValue[]{from});

        value = new Value();
    }

    @Test
    public void testDoValidationValid() throws Exception {
        value.setValue("2016-06-03");
        DateSmallerThanRule rule = new DateSmallerThanRule("test date", "test.case.date", Arrays.asList(value));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(true, result.isCompliant());
    }

    @Test
    public void testDoValidationInvalid() throws Exception {
        value.setValue("2016-04-03");
        DateSmallerThanRule rule = new DateSmallerThanRule("test date", "test.case.date", Arrays.asList(value));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testDoValidationNoValue() throws Exception {
        value.setValue("2016-06-03");
        DateSmallerThanRule rule = new DateSmallerThanRule("test date", "test.case.nodate", Arrays.asList(value));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testDoValidationNoThreshold() throws Exception {
        value.setValue(null);
        DateSmallerThanRule rule = new DateSmallerThanRule("test date", "test.case.date", Arrays.asList(value));

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testDoValidationNoThresholdList() throws Exception {
        value.setValue(null);
        DateSmallerThanRule rule = new DateSmallerThanRule("test date", "test.case.date", null);

        RuleComplianceResult result = rule.validate(context, item);

        assertEquals(false, result.isCompliant());
    }
}