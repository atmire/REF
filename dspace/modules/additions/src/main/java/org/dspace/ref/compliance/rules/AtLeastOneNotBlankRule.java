package org.dspace.ref.compliance.rules;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author philip at atmire.com
 *
 * Rule to check if an item has a value in at least one of the specified fields.
 */
public class AtLeastOneNotBlankRule extends AbstractComplianceRule {

    private List<String> fieldsToCheck;
    private String fieldDescription;

    public AtLeastOneNotBlankRule(final String fieldDescription, final List<String> fields) {
        this.fieldDescription = StringUtils.trimToEmpty(fieldDescription);
        fieldsToCheck = new ArrayList<String>(fields.size());
        for (String field : fields) {
            fieldsToCheck.add(StringUtils.trimToEmpty(field));
        }
    }

    protected String getRuleDescriptionCompliant() {
        return String.format("at least one of the %s metadata fields (%s) is filled in", fieldDescription, StringUtils.join(fieldsToCheck, ", "));
    }

    @Override
    protected String getRuleDescriptionViolation()  {
        return String.format("at least one of the %s metadata fields (%s) must be filled in", fieldDescription, StringUtils.join(fieldsToCheck, ", "));
    }

    @Override
    protected boolean doValidationAndBuildDescription(Context context, Item item) {
        for (String field : fieldsToCheck) {
            DCValue[] metadata = item.getMetadata(field);

            if(metadata.length > 0 && StringUtils.isNotBlank(metadata[0].value)){
                return true;
            }
        }

        return false;
    }

}
