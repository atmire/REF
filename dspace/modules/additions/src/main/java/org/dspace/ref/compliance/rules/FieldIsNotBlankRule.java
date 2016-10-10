package org.dspace.ref.compliance.rules;

import java.util.*;
import static org.apache.commons.collections.CollectionUtils.*;
import org.dspace.content.*;
import static org.dspace.util.DcValueUtils.*;

/**
 * Validation rule that will check if a field has a non-blank value.
 */
public class FieldIsNotBlankRule extends AbstractFieldCheckRule {

    public FieldIsNotBlankRule(final String fieldDescription, final String metadataField) {
        super(fieldDescription, metadataField);
    }

    @Override
    protected boolean checkFieldValues(final List<Metadatum> fieldValueList) {
        if (isEmpty(fieldValueList)) {
            addViolationDescription("the %s field has no value", fieldDescription);
            return false;
        } if(isBlank(fieldValueList.get(0))) {
            addViolationDescription("the %s field has a blank value", fieldDescription);
            return false;
        } else {
            return true;
        }
    }

    protected String getRuleDescriptionCompliant() {
        return String.format("the %s field (%s) is filled in", fieldDescription, metadataFieldToCheck);
    }

    protected String getRuleDescriptionViolation() {
        return String.format("the %s field (%s) must be filled in", fieldDescription, metadataFieldToCheck);
    }
}
