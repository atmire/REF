package org.dspace.ref.compliance.rules;

import java.util.*;
import org.apache.commons.collections.*;
import static org.apache.commons.collections.CollectionUtils.*;
import org.apache.commons.lang3.*;
import org.dspace.content.*;
import org.dspace.ref.compliance.definition.model.*;
import org.joda.time.*;

/**
 * Rule to check if a date value of a given metadata field is smaller than a specified value
 */
public class DateSmallerThanRule extends AbstractFieldCheckRule implements ComplianceRule {

    private Value thresholdValue;

    public DateSmallerThanRule(final String fieldDescription, final String metadataField, final List<Value> thresholdValues) {
        super(fieldDescription, metadataField);

        thresholdValue = CollectionUtils.isEmpty(thresholdValues) ? null : thresholdValues.get(0);
    }

    protected boolean checkFieldValues(final List<Metadatum> fieldValueList) {
        boolean valid = false;
        if (isEmpty(fieldValueList)) {
            addViolationDescription("The %s field has no value", fieldDescription);

        } else if (thresholdValue == null || StringUtils.isBlank(thresholdValue.getValue())) {
            addViolationDescription("The threshold value cannot be blank");

        } else {
            try {
                DateTime thresholdDate = parseDateTime(thresholdValue.getValue());
                DateTime dateToCheck = parseDateTime(fieldValueList.get(0).value);

                if(dateToCheck == null) {
                    addViolationDescription("there is no valid value for the field " + metadataFieldToCheck);
                } else if (thresholdDate != null && dateToCheck.compareTo(thresholdDate) < 0) {
                    valid = true;
                } else {
                    addViolationDescription("the %s is after %s", fieldDescription,
                            thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
                }

            } catch (IllegalArgumentException ex) {
                addViolationDescription("the metadata field %s is invalid because it has too few tokens or contains an invalid date", metadataFieldToCheck);
            }
        }

        return valid;
    }

    protected String getRuleDescriptionCompliant() {
        return String.format("the %s (%s) is before %s", fieldDescription, metadataFieldToCheck,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }

    protected String getRuleDescriptionViolation() {
        return String.format("the %s (%s) must be before %s", fieldDescription, metadataFieldToCheck,
                thresholdValue == null ? "ERROR" : getValueDescription(thresholdValue));
    }
}
