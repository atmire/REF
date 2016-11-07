package com.atmire.utils;

import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Roeland Dillen (roeland at atmire dot com)
 * Date: 16/04/13
 * Time: 14:22
 */
public class EmbargoUtils {
    private static Logger log = Logger.getLogger(EmbargoUtils.class);


    public static Date getEmbargo(Bitstream bitstream, Context context) {
        java.util.List<ResourcePolicy> policiesByDSOAndType = null;
        try {
            policiesByDSOAndType = AuthorizeManager.getPoliciesActionFilter(context, bitstream, Constants.READ);
        } catch (SQLException e) {
            return null;
        }

        for(ResourcePolicy pol:policiesByDSOAndType){
            //This will be the start date of the Anonymous policy: Anonymous will get read access on
            Date date=pol.getStartDate();
            if(date!=null)
                return date;

        }
        return null;
    }

    public static Date getLastEmbargo(Item item, Context context){
        Date lastEmbargo=null;
        try{
            Bundle[] bundles = item.getBundles();
            for(Bundle bundle : bundles){
                Bitstream[] bitstreams = bundle.getBitstreams();
                for(Bitstream bitstream:bitstreams)
                {
                    java.util.List<ResourcePolicy> policiesByDSOAndType = AuthorizeManager.getPoliciesActionFilter(context, bitstream, Constants.READ);

                    for(ResourcePolicy pol:policiesByDSOAndType){
                        //This will be the start date of the Anonymous policy: Anonymous will get read access on
                        Date date=pol.getStartDate();
                        if(date!=null)
                            if(lastEmbargo==null){
                                lastEmbargo=date;
                            } else if(date.after(lastEmbargo)) {
                                lastEmbargo=date;
                            }
                    }
                }
            }
        }catch(Exception e){
            log.error("error in in getting embargo action", e);

        }
        return lastEmbargo;
    }
}
