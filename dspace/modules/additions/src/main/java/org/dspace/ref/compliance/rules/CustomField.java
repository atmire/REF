package org.dspace.ref.compliance.rules;

import com.atmire.utils.EmbargoUtils;
import org.apache.commons.lang3.StringUtils;
import org.dspace.content.*;
import org.dspace.core.Context;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO TOM UNIT TEST
 */
public enum CustomField {

    BITSTREAM_COUNT("bitstream.count") {
        public List<DCValue> createValueList(final Context context, final Item item) throws SQLException {
            List<DCValue> output = new LinkedList<DCValue>();
            for (Bitstream bitstream : item.getNonInternalBitstreams()) {
                DCValue value = new DCValue();
                value.value = bitstream.getName();
                output.add(value);
            }

            return output;
        }
    },
    BITSTREAM_EMBARGO_ENABLED("bitstream.embargo.enabled") {
        public List<DCValue> createValueList(final Context context, final Item item) throws SQLException {
            List<DCValue> output = new LinkedList<DCValue>();
            DCValue value = new DCValue();
            if (EmbargoUtils.getLastEmbargo(item, context) == null) {
                value.value = "false";
            } else {
                value.value = "true";
            }
            output.add(value);
            return output;
        }
    },
    BITSTREAM_EMBARGO_ENDDATE("bitstream.embargo.enddate") {
        public List<DCValue> createValueList(final Context context, final Item item) throws SQLException {
            List<DCValue> output = new LinkedList<DCValue>();
            Date embargo = EmbargoUtils.getLastEmbargo(item, context);
            if(embargo != null) {
                DCValue value = new DCValue();
                value.value = AbstractComplianceRule.getDateTimePrinter().print(embargo.getTime());
                output.add(value);
            }
            return output;
        }
    },
    ITEM_LIFECYCLE_STATUS("item.status") {
        public List<DCValue> createValueList(final Context context, final Item item) throws SQLException {
            List<DCValue> output = new LinkedList<DCValue>();

            DCValue value = new DCValue();
            if(item.isArchived()) {
                value.value = "archived";
            } else if(item.isWithdrawn()) {
                value.value = "withdrawn";
            } else if(WorkspaceItem.findByItem(context, item) != null) {
                value.value = "workspace";
            } else {
                value.value = "workflow";
            }

            output.add(value);
            return output;
        }
    };

    private final String fieldName;

    CustomField(final String field) {
        this.fieldName = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public abstract List<DCValue> createValueList(final Context context, final Item item) throws SQLException;

    public static CustomField findByField(final String field) {
        CustomField result = null;

        for (CustomField customField : CustomField.values()) {
            if(StringUtils.equals(customField.getFieldName(), field)) {
                result = customField;
                break;
            }
        }

        return result;
    }

}
