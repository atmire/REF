package com.atmire.submission.typebound.condition;

import com.atmire.utils.Metadatum;
import java.util.*;
import org.dspace.content.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Created by jonas - jonas@atmire.com on 11/04/16.
 */
public class MetadataCondition implements SubmissionStepCondition {

    private Metadatum metadatum;
    private List<String> allowedValues;

    @Required
    public void setMetadatum(String metadata) {
        this.metadatum = new Metadatum(metadata);
    }

    @Required
    public void setAllowedValues(List<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public boolean conditionMet(Item item) {
        org.dspace.content.Metadatum[] dcValues = item.getMetadataByMetadataString(metadatum.toString());
        for (org.dspace.content.Metadatum dcValue : dcValues) {
            if(allowedValues.contains(dcValue.value)){
                return true;
            }
        }
        return false;
    }
}
