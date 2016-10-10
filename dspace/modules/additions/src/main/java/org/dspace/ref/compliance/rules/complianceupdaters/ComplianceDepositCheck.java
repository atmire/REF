package org.dspace.ref.compliance.rules.complianceupdaters;

import org.dspace.content.Item;
import org.dspace.core.Context;

/**
 * Created by jonas - jonas@atmire.com on 23/03/16.
 */
public interface ComplianceDepositCheck {

    public boolean checkAndUpdateCompliance(Context context, Item item);


}
