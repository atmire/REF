package com.atmire.submission.typebound.condition;

import com.atmire.utils.Metadatum;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

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
        DCValue[] dcValues = item.getMetadata(metadatum.toString());
        for (DCValue dcValue : dcValues) {
            if(allowedValues.contains(dcValue.value)){
                return true;
            }
        }
        return false;
    }
}
