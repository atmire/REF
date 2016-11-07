package org.dspace.ref.compliance.rules;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.ref.compliance.definition.model.Value;
import org.joda.time.DateTime;
import org.joda.time.Months;

import java.util.List;

/**
 * Rule to check if a date range defined by two metadata fields is smaller than a specified threshold
 */
public class DateRangeSmallerThanRule extends AbstractComplianceRule {

    private String fromField;
    private String toField;
    private String rangeDescription;

    private Value thresholdValue;
    private Integer thresholdNumber;

    public DateRangeSmallerThanRule(final String fromField, final String toField, final String rangeDescription,
                                    final List<Value> thresholdValues) {
        this.fromField = StringUtils.trimToNull(fromField);
        this.toField = StringUtils.trimToNull(toField);
        this.rangeDescription = StringUtils.trimToNull(rangeDescription);

        thresholdValue = CollectionUtils.isEmpty(thresholdValues) ? null : thresholdValues.get(0);

        try {
            this.thresholdNumber = thresholdValue == null ? null : Integer.valueOf(StringUtils.trimToEmpty(thresholdValue.getValue()));
        } catch(NumberFormatException ex) {
            addViolationDescription("the provided threshold value %s is not a number", thresholdValue);
            this.thresholdNumber = null;
        }
    }

    protected String getRuleDescriptionCompliant() {
        return String.format("the %s (from %s to %s) is smaller than %s month(s)", rangeDescription, fromField, toField,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }

    protected String getRuleDescriptionViolation() {
        return String.format("the %s (from %s to %s) must be smaller than %s month(s)", rangeDescription, fromField, toField,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }

    protected boolean doValidationAndBuildDescription(final Context context, final Item item) {
        boolean valid = false;

        if (fromField == null || toField == null) {
            addViolationDescription("the from and to date fields of a date range validation rule cannot be blank.");
        } else {
            if(thresholdNumber != null) {
                DateTime from = getFirstDateValue(context, item, fromField);
                DateTime to = getFirstDateValue(context, item, toField);

                if(from == null) {
                    addViolationDescription("there is no value for the field " + fromField);
                }
                if(to == null) {
                    addViolationDescription("there is no value for the field " + toField);
                }

                if (to != null && from != null) {
                    if(to.isAfter(from)){
                        to = to.minusDays(1);
                    }
                    int months = Months.monthsBetween(from.toLocalDate(), to.toLocalDate()).getMonths();

                    if (months < thresholdNumber) {
                        valid = true;
                    } else {
                        addViolationDescription("the %s is %d month(s)", rangeDescription, months);
                    }
                }
            }
        }

        return valid;
    }

}
