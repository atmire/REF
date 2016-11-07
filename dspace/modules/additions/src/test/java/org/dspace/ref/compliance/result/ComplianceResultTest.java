package org.dspace.ref.compliance.result;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplianceResultTest {

    @Test
    public void testIsCompliantTrue() throws Exception {
        ComplianceResult result = new ComplianceResult();

        RuleComplianceResult rule1 = new RuleComplianceResult();
        rule1.setCompliant(true);

        CategoryComplianceResult categoryResult1 = new CategoryComplianceResult();
        categoryResult1.addRuleResult(rule1);
        categoryResult1.setApplicable(true);

        RuleComplianceResult rule2 = new RuleComplianceResult();
        rule2.setCompliant(true);

        CategoryComplianceResult categoryResult2 = new CategoryComplianceResult();
        categoryResult2.addRuleResult(rule2);
        categoryResult2.setApplicable(true);

        result.addCategoryResult(categoryResult2);
        result.addCategoryResult(categoryResult2);

        assertEquals(true, result.isCompliant());
    }

    @Test
    public void testIsCompliantFalse() throws Exception {
        ComplianceResult result = new ComplianceResult();

        RuleComplianceResult rule1 = new RuleComplianceResult();
        rule1.setCompliant(true);

        CategoryComplianceResult categoryResult1 = new CategoryComplianceResult();
        categoryResult1.addRuleResult(rule1);
        categoryResult1.setApplicable(true);

        RuleComplianceResult rule2 = new RuleComplianceResult();
        rule2.setCompliant(false);

        CategoryComplianceResult categoryResult2 = new CategoryComplianceResult();
        categoryResult2.addRuleResult(rule2);
        categoryResult2.setApplicable(true);

        result.addCategoryResult(categoryResult2);
        result.addCategoryResult(categoryResult2);

        assertEquals(false, result.isCompliant());
    }

    @Test
    public void testIsCompliantGlobalException() throws Exception {
        ComplianceResult result = new ComplianceResult();

        RuleComplianceResult rule1 = new RuleComplianceResult();
        rule1.setCompliant(true);

        CategoryComplianceResult categoryResult1 = new CategoryComplianceResult();
        categoryResult1.addRuleResult(rule1);
        categoryResult1.setApplicable(true);

        RuleComplianceResult rule2 = new RuleComplianceResult();
        rule2.setCompliant(false);

        CategoryComplianceResult categoryResult2 = new CategoryComplianceResult();
        categoryResult2.addRuleResult(rule2);
        categoryResult2.setApplicable(true);

        result.addCategoryResult(categoryResult2);
        result.addCategoryResult(categoryResult2);

        RuleComplianceResult exception1 = new RuleComplianceResult();
        exception1.setApplicable(true);
        exception1.setCompliant(true);

        result.addExceptionResult(exception1);

        assertEquals(true, result.isCompliant());
    }

    @Test
    public void testIsCompliantCategoryException() throws Exception {
        ComplianceResult result = new ComplianceResult();

        RuleComplianceResult rule1 = new RuleComplianceResult();
        rule1.setCompliant(false);

        CategoryComplianceResult categoryResult1 = new CategoryComplianceResult();
        categoryResult1.addRuleResult(rule1);
        categoryResult1.setApplicable(true);

        RuleComplianceResult exception1 = new RuleComplianceResult();
        exception1.setCompliant(true);
        exception1.setApplicable(true);
        categoryResult1.addExceptionResult(exception1);

        RuleComplianceResult rule2 = new RuleComplianceResult();
        rule2.setCompliant(false);

        CategoryComplianceResult categoryResult2 = new CategoryComplianceResult();
        categoryResult2.addRuleResult(rule2);
        categoryResult2.setApplicable(false);

        result.addCategoryResult(categoryResult2);
        result.addCategoryResult(categoryResult2);

        assertEquals(true, result.isCompliant());
    }
}