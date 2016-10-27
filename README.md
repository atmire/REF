# REF Open Access Policy Checker Plugin

## Introduction
The REF Compliance Checker patch for DSpace has been developed to aid institutions in complying with the Open Access policy of the UK HE Funding Bodies. The REF Open Access policy sets out the details of a requirement that certain research outputs should be made open-access to be eligible for submission to the next Research Excellence Framework (REF). This requirement will apply to journal articles and conference proceedings with an International Standard Serial Number accepted for publication after 1 April 2016. More information can be found [here](http://www.hefce.ac.uk/pubs/year/2014/201407/). 
            
To be compliant with this policy, an item must adhere to a set of rules divided over three categories: Deposit requirements, Discovery requirements and Access requirements. These categories are evaluated in order and a category can only be compliant if the previous category was compliant. The deposit requirements state that the final research publication must have been deposited in an institutional or subject repository as soon after the point of acceptance as possible. To be compliant with the Discovery requirements, the output must be presented in a way that allows it to be discovered by (anonymous) readers and by automated tools such as search engines. The Access requirements require that it must be possible for anyone with internet access to search electronically within the text, read it and download it without charge, while respecting any constraints on timing (like embargoes) but without too much delay. There are a number of exceptions to the various categories that are allowed by the policy. These exceptions cover circumstances where deposit was not possible, or where open access to deposited material could not be achieved within the policy requirements. If an item is applicable for one of the predefined exceptions, all individual category results become irrelevant and the output is compliant.

The purpose of the REF Compliance Checker is to make it easier for researcher and repository administrators to submit REF compliant items. Currently this patch offers
* Detailed information on the compliance of an item in submission, workflow or already in archive.
* Resolution hints on how an item can be made REF compliant.
* Configuration options to make REF compliance mandatory for a specific collection or the whole repository.
* Support for showing detailed information on other policies like RIOXX.

This patch was developed by Atmire commissioned by Jisc. It is freely available to all DSpace users. More information on how to install this patch on your DSpace repository can be found below.

## Quick Start
### Prerequisites

**__Important note__**: Below, we will explain you how to apply the patch to your existing installation. This will affect your source code. Before applying a patch, it is **always** recommended to create a backup of your DSpace source code.

In order to apply the patch, you will need to locate the **DSpace source code** on your server. That source code directory contains a directory _dspace_, as well as the following files:  _LICENSE_,  _NOTICE_ ,  _README_ , ....

For every release of DSpace, generally two release packages are available. One package has "src" in its name and the other one doesn't. The difference is that the release labelled "src" contains ALL of the DSpace source code, while the other release retrieves precompiled packages for specific DSpace artifacts from Maven central. **The REF Compliance Checker patches were designed to work on both "src" and other release packages of DSpace**. 

To be able to install the patch, you will need the following prerequisites:

* A running DSpace 4.x or 5.x instance. 
* Git should be installed on the machine. The patch will be applied using several git commands as indicated in the next section. 

### Obtaining a recent patch file
Atmire's modifications for the REF Open Access Policy Checker patch are tracked on Github. Using the provided tools, a patch file can be generated from GIT.
By comparing the latest codebase against a clean DSpace, we can select the customizations that have been applied

DSPACE 5.x [https://github.com/atmire/REF/compare/dspace_5x...stable_5x.diff](https://github.com/atmire/REF/compare/dspace_5x...stable_5x.diff)
DSPACE 4.x [https://github.com/atmire/REF/compare/dspace_4x...stable_4x.diff](https://github.com/atmire/REF/compare/dspace_4x...stable_4x.diff) 

### Patch installation
To install the patch, the following steps will need to be performed.

#### 1. Go to the DSpace Source directory.####

This folder should have a structure similar to:
* dspace
  * modules
  * config
  * ...
* pom.xml

#### 2. Run the Git command to check whether the patch can be correctly applied.####

Run the following command where <patch file> needs to be replaced with the name of the patch:

```
git apply --check <patch file>
```

This command will return whether it is possible to apply the patch to your installation. This should pose no problems in case the DSpace is not customized or in case not much customizations are present.
In case, the check is successful, the patch can be installed as explained in the next steps. If the check is not ok, you can still apply the patch but you will have to manually resolve any conflicts identified by Git.

#### 3. Apply the patch ####

To apply the patch, the following command should be run where <patch file> is replaced with the name of the patch file.

```
git apply <patch file>
```

There may be various warnings about whitespaces, but these will pose no problems when applying the patch and can be ignored.

#### 4. Rebuild and redeploy your repository####

After the patch has been applied, the repository will need to be rebuild.
DSpace repositories are typically built using the Maven and deployed using Ant.

Specifically for DSpace 4, it is important to know that the database changes to add the refterms fields to the metadata registry, are called in the "update_registries" ant target.
This ant target is part of "ant update". However, it is not part of "ant fresh_install".

If you are not seeing the fields in your registry, you can import the refterms and rioxx fields manually by executing:

```
dspace/bin/dspace dsrun org.dspace.administer.MetadataImporter -f <dspace.dir>/config/registries/refterms-types.xml -u
```
as well as
```
dspace/bin/dspace dsrun org.dspace.administer.MetadataImporter -f <dspace.dir>/config/registries/rioxx-types.xml -u
```

For the refterm and rioxx fields respectively. After these commands, make sure that the new fields are in fact present in the registry.

#### 5. Restart your Tomcat ####

After the repository has been rebuild and redeployed, the Tomcat will need to be restarted to bring the changes to production.

### REF Compliance Checker Configuration
#### Configuring the submission input fields

The submission input fields are configured in file *dspace/config/input-forms.xml*.

##### Fields that need to be present:

* rioxxterms.type
   * Select the content type of the item. Only item with RIOXX type 'Conference Paper/Proceeding/Abstract' or 'Journal Article/Review' are applicable for REF compliance.
* dcterms.dateAccepted
   * Enter the date of acceptation, all three fields are required (year, month and day).
* dc.date.issued
   * The date of previous print publication or public distribution.
* refterms.dateFirstOnline
   * The date the publication first became available online.
* refterms.panel
   * Metadata field that holds information on the REF Panel for which the submitter applies.
   
An example submission form configuration can be found [here](https://github.com/atmire/REF/blob/master/dspace/config/input-forms.xml). *This configuration is not part of the patch and has to be applied manually.*


#### Configuring the submission steps
The submission steps are configured in file *dspace/config/item-submission.xml*.

##### Exception Step

An additional submission step has been added to give the user the possibility to enter REF exceptions (**NOTE**:Only 1 exception can be given for a single item).

The "Exceptions" step should occur after the upload step and contains a couple of options:

The initial page preselects the "No Exception Applicable" checkbox.

![Exception Step initial](../../raw/docs/images/initial.png "Exception Step initial")

Upon clicking a different option, extra fields are presented to the user.
These extra fields are also configurable in the same file as where the configuration for what options to show is set.

![Exception Step new option clicked](../../raw/docs/images/new-option-clicked.png "Exception Step new option clicked")

Apart from the "No Exception applicable" There are 2 different "types" of exception-views.

1. Deposit, Access and Technical Exception -> These Exceptions all have subdivisions. This is shown as a dropdown box containing extra options. The help text underneath the textarea changes depending on what option was selected.
2. Other Exception -> This only contains the input field where the user can fill in the explanation of the exception.

![Exception Step no dropdown](../../raw/docs/images/no-dropdown.png "Exception Step no dropdown")

As stated before, only 1 exception can be present on a single item, if a new option/explanation is set, the previous is removed.
If, during the review step or the workflow review, the exceptions step is shown again, the page preselects and prefills the saved data.

![Exception Step prefilled](../../raw/docs/images/prefilled.png "Exception Step prefilled")

Above the Previous, Save and Next buttons the general result of the compliance check is shown. This allows the user to see if the submission is compliant or if he needs to select an exception to make the submission compliant.

![Exception Step compliance text](../../raw/docs/images/hefce-exception-compliance-text.png "Exception Step compliance text")

##### REF Compliance Step 

###### Introduction

The REF Compliance step provides an overview of the submission's compliance with REF Open Access policy. If the submission is not compliant then this page will explain why and how the submission can be made compliant with the REF Open Access policy. This step should come after the Exception step and preferrably also after the Review step. 

###### Top Section

The color of the bar at the top of the page content will immediately make it clear if the item is compliant. The bar will have a green background if the submission is compliant, or an orange background when a rule has been violated. A blue background is shown when the REF compliance check is not applicable for the submission (when the submission does not concern a journal article or conference proceeding with an International Standard Serial Number or if the publication was accepted before 1 April 2016).

If there is a violation then another orange bar is shown under the top bar with a summary of the actions the submitter can perform to make the submission compliant.

![Compliance Step top](../../raw/docs/images/hefce-compliancestep-1.png "Compliance Step top")

###### Technical and Other Exceptions

When the submission contains a 'technical' or 'other' exception, a section "Technical and Other Exceptions" is visible. This section contains information about the exception.

![Compliance Step Technical and Other Exceptions](../../raw/docs/images/hefce-compliancestep-2.png "Compliance Step Technical and Other Exceptions")

###### Requirements sections

The Compliance page contains a separate section for each category of requirements. 

If a requirements section title is grayed out, none of the requirements in that section are applicable on the submission. If a single requirement is grayed out then this requirement is not applicable on the submission.

Requirements that are not applicable to a submission are hidden by default. To show all requirements, link "show all rules" under the top bar(s) can be clicked.

![Compliance Step show all rules](../../raw/docs/images/hefce-compliancestep-3.png "Compliance Step show all rules")

Requirements and requirements section titles have a question mark next to their name. When the submitter hovers over these question marks, hints appear with extra information about that specific requirement or category. 

![Compliance Step hints](../../raw/docs/images/hefce-compliancestep-4.png "Compliance Step hints")

###### REF Compliance related data

At the bottom of the page, the section "REF Compliance related data" shows an overview of the submission's metadata that is relevant for determining if a submission is compliant. 

![Compliance Step related data](../../raw/docs/images/hefce-compliancestep-5.png "Compliance Step related data")

Some of these values contain text *(estimated)*. These values were estimated so that they could be used in the compliance check. They will receive a “real” value upon item archival or at the end of an embargo period. 

##### Type dependent steps

A submission step can be configured to only be shown for specific item types.
Currently, the type-dependency is set to only show the Exceptions and Compliance step for items with rioxxterms.type "Conference Paper/Proceeding/Abstract" or "Journal Article/Review". If you don't mind that the Exception and Compliance step are shown for each RIOXX type, you can skip this section.

###### Configuration

Spring file *dspace/config/spring/api/type-dependent-submission.xml* contains the configuration of the conditions that are used to determine if a submission step should be enabled.

```
   <bean id="SubmissionStepConditionCheck" class="com.atmire.submission.typebound.check.SubmissionStepConditionCheck"/>

    <bean name="condition.ref.applicable.step" class="com.atmire.submission.typebound.condition.ConditionWrapper" scope="prototype">
        <property name="submissionStepConditions">
            <list>
                <ref bean="refComplianceVersion"/>
            </list>
        </property>
    </bean>

    <bean id="refComplianceVersion" class="com.atmire.submission.typebound.condition.MetadataCondition">
        <property name="metadatum" value="rioxxterms.type"/>
        <property name="allowedValues">
            <list>
                <value>Conference Paper/Proceeding/Abstract</value>
                <value>Journal Article/Review</value>
            </list>
        </property>
    </bean>
```

Bean "SubmissionStepConditionCheck" is used during the submission to check between steps if the conditions of the type-dependency are met.
It uses the "ConditionWrapper" bean to check each condition in the "submissionStepConditions" list.
Currently only one type of condition is available. The condition to check on metadata. 

The name of the "ConditionWrapper" bean that applies for a certain submission step is referenced in the "type-binding" field of the submission step configuration in *dspace/config/item-submission.xml*.

```
     <step>
           <heading>submit.progressbar.exception</heading>
           <processing-class>com.atmire.ref.compliance.submission.ExceptionStep</processing-class>
           <xmlui-binding>com.atmire.xmlui.compliance.submission.ExceptionStep</xmlui-binding>
           <workflow-editable>true</workflow-editable>
           <type-binding>condition.ref.applicable.step</type-binding>
      </step>
```

If more conditions are added to the "ConditionWrapper", all of them will need to be met before the step is visible.

##### Hide step in submission or workflow

By default the REF compliance submission step is enabled during the submission and when editing an item in the workflow. However, you can also configure it to only show during workflow editing or only during submission.

In the item-submission.xml file, the step can be disabled for the submission by setting the submission-editable element to false:

```
       <step>
           <heading>submit.progressbar.ref.compliance</heading>
           <processing-class>org.dspace.ref.compliance.submission.REFComplianceStep</processing-class>
           <xmlui-binding>com.atmire.xmlui.compliance.submission.REFComplianceStep</xmlui-binding>
           <type-binding>condition.ref.applicable.step</type-binding>
           <submission-editable>false</submission-editable>
       </step>
```

The step can be disabled during the workflow when editing an item by setting the workflow-editable element to false:

```
       <step>
           <heading>submit.progressbar.ref.compliance</heading>
           <processing-class>org.dspace.ref.compliance.submission.REFComplianceStep</processing-class>
           <xmlui-binding>com.atmire.xmlui.compliance.submission.REFComplianceStep</xmlui-binding>
           <type-binding>condition.ref.applicable.step</type-binding>
           <workflow-editable>false</workflow-editable>
       </step>
```

To do this on a per-collection basis, you should create a separate submission-process in the item-submission.xml file for that collection.

#### Enabling an additional workflow step

The REF compliance check workflow step is a separate workflow step that performs the REF compliance check. 
 
By default the REF compliance check workflow step is disabled. 

This workflow step can be enabled by uncommenting the step "refcompliancestep" configuration in *dspace/config/workflow.xml*:

```
        <step id="refcompliancestep" role="finaleditor" userSelectionMethod="claimaction">
            <outcomes>
                <step status="0">finaleditstep</step>
                <step status="1">editstep</step>
            </outcomes>

            <actions>
                <action id="refcomplianceaction"/>
            </actions>
        </step>
```

Also configure the REF compliance check as next step for the "editstep":

```
      <step id="editstep" role="editor" userSelectionMethod="claimaction">
            <outcomes>
                <step status="0">refcompliancestep</step>
            </outcomes>
            <actions>
                <action id="editaction"/>
            </actions>
      </step>
```

If you enable the separate compliance workflow step, we recommend you to set "workflow-editable" to false for the REFComplianceStep in the item-submission.xml file.

#### Configuring the navigation menu

The REF item compliance check is accessible *for archived items* from the "Compliance" sidebar menu on an item page.

The message key of the sidebar menu option for the REF compliance check can be configured in spring file *dspace/config/spring/xmlui/item-validation-services.xml*.
The message key is configured in property "navigationKey". 

```
 <bean class="org.dspace.app.xmlui.aspect.compliance.ComplianceUI" id="refComplianceUI">
        <property name="complianceCheckService"  ref="refComplianceCheckService" />
        <property name="name" value="REF Open Access policy"/>
        <property name="shortname" value="REF"/>
        <property name="identifier" value="ref"/>
        <property name="navigationKey" value="xmlui.Compliance.Navigation.item-ref-compliance"/>
        <property name="complianceRelatedData" ref="refComplianceRelatedData"/>
 </bean>
```

This message key should also be configured in messages file *dspace/modules/xmlui/src/main/webapp/i18n/messages.xml*.

```
<message key="xmlui.Compliance.Navigation.item-ref-compliance">Item REF compliance</message>
```

The REF compliance check sidebar navigation menu option can be disabled by removing the value of the "navigationKey" property.

```
<property name="navigationKey" value=""/>
```

#### Updating the rule descriptions and resolution hints

The REF rules XML file *dspace/config/ref-validation-rules.xml* can be edited to update descriptions and resolution hints.

Each rule element has a "description" attribute and a "resolutionHint" attribute. The values of these attributes are the messages shown on compliance pages for a certain rule.
The "description" attribute contains a general description of the rule.
The "resolutionHint" attribute contains instructions on how to make an item compliant with the rule. 

An example rule from the XML configuration:

```
         <rule type="countGreaterThan"
                    description="A compliant deposit must have at least one file."
                    resolutionHint="Please upload the author’s accepted and final peerreviewed text to this item.">
                <field>bitstream.count</field>
                <fieldDescription>attached files</fieldDescription>
                <fieldValue>
                    <value>0</value>
                </fieldValue>
         </rule>
```

#### Enforcing mandatory compliance for specific collections or the whole repository

The REF Compliance Checker also allows you to configure if the compliance step should allow a submission of an item that is not compliant to be completed or not.

This configuration is found in *dspace/config/modules/item-compliance.cfg*.

```
ref.submission.block.on.rule.violation.default = false
ref.submission.block.on.rule.violation.123456789/7 = true

ref.workflow.block.on.rule.violation.default = false
ref.workflow.block.on.rule.violation.123456789/7 = true
```

Configuration property "ref.submission.block.on.rule.violation.default" is the default configuration for all collections that do not have their own configuration, thus the repository default. 
Setting this property to true will enforce compliance with the REF Open Access. This means the submitter will not be able to continue past the compliance submission step until the item is compliant.

This property can be configured separately for a specific collection by adding the collection handle as suffix instead of "default".

Configuration property "ref.workflow.block.on.rule.violation.default" is the default workflow configuration for all collections that do not have their own configuration. 
Setting this property to true will enforce compliance with the REF Open Access. This means the workflow reviewer will not be able to continue past the compliance workflow step until the item is compliant.

This property can also be configured separately for a specific collection by adding the collection handle as suffix instead of "default".

#### Configure authorization groups for seeing compliancy information
By default, information on the compliance of an item is only visible to administrators and the users that are member of the "REF Compliance Viewers" group. A submitter can also always see the compliance status of the items he or she submitted. 

To allow a different group or multiple groups to see compliancy information or to disable submitter access, the following configuration possibilities have been added in *dspace/config/spring/api/authorization-service.xml*

```
<bean id="ComplianceAuthorizationChecker" class="com.atmire.authorization.AuthorizationChecker" lazy-init="true">

        <!-- If any of the checks returns true, the user is authorized -->
        <property name="authorizationChecks">
            <list>
                <bean class="com.atmire.authorization.checks.SubmitterCheck"/>
                <ref bean="allowedGroupsForCompliance"/>
            </list>
        </property>
    </bean>

    <bean id="allowedGroupsForCompliance" class="com.atmire.authorization.checks.GroupAuthorizationCheck" lazy-init="true">
        <property name="allowedGroups">
            <list>
                <value>REF Compliance Viewers</value>
            </list>
        </property>
    </bean>
```

The configuration uses the checks configured in "authorizationChecks" to authorize a user or not. If ANY of the checks present in this configuration is passed, the user is allowed to view the compliance checks.
The default "authorizationChecks" configuration contains the following

* SubmitterCheck
* allowedGroupsForCompliance

'SubmitterCheck' contains the validation to see if the currently logged in user was the original submitter of the item.

'allowedGroupsForCompliance' is a reference the an implementation of "GroupAuthorizationCheck".
The authorization present in this class is done against the group names that are configured in the "allowedGroups" property.
The Administrator group is ALWAYS considered to be a part of these allowedGroups and will never be denied authorization.

The default group can be altered by changing the values in the list or adding new ones.

_Something to note:_ If a configured group does not exist in the current repository, it is created and the Administrator group is added to it by default

#### Schedule curation tasks

##### Date of first compliant Open Access curation task

A curation task to update the Date of first compliant Open Access of an item is available from the list of curation tasks for items, collections and communities.

This curation task checks the embargo of an item's bitstreams and updates the Date of first compliant Open Access to the end date of the embargo only if the embargo has expired.

The Date of first compliant Open Access is stored in metadata field refterms.dateFOA.

This curation task can be run from the command line and *should be scheduled in CRONTAB to run at least on a daily basis*:

```
[dspace dir]/bin/dspace curate -t openaccess -i "all"
```

### Other Policy Configurations
#### Supporting other compliancy policies
The REF Compliance Checker has been implemented with a generic rule-evaluating framework that can also be used to define and check compliance for other policy frameworks. As an example, we included a configuration to the RIOXX policy as this patch already uses the RIOXX metadata schema.

#### RIOXX Configuration
##### Configuring the submission step

The submission steps are configured in file *dspace/config/item-submission.xml*. To enable visibility on RIOXX compliance in the submission form, you have to add this step similar to the REF compliance step:

```
       <step>
           <heading>submit.progressbar.rioxx.compliance</heading>
           <processing-class>org.dspace.ref.compliance.submission.RIOXXComplianceStep</processing-class>
           <xmlui-binding>com.atmire.xmlui.compliance.submission.RIOXXComplianceStep</xmlui-binding>
           <submission-editable>true</submission-editable>
           <workflow-editable>true</workflow-editable>
       </step>
```

To do this on a per-collection basis, you should create a separate submission-process in the item-submission.xml file for that collection.

##### Enabling an additional workflow step

The RIOXX compliance check workflow step is a separate workflow step that performs the RIOXX compliance check. 
 
By default the RIOXX compliance check workflow step is disabled. 

This workflow step can be enabled by uncommenting the step "rioxxcompliancestep" configuration in *dspace/config/workflow.xml*:

```
       <step id="rioxxcompliancestep" role="finaleditor" userSelectionMethod="claimaction">
            <outcomes>
                <step status="0">finaleditstep</step>
                <step status="1">refcompliancestep</step>
            </outcomes>

            <actions>
                <action id="rioxxcomplianceaction"/>
            </actions>
        </step>
```

Also configure the REF compliance check as next step for the "editstep":

```
      <step id="editstep" role="editor" userSelectionMethod="claimaction">
            <outcomes>
                <step status="0">rioxxcomplianceaction</step>
            </outcomes>
            <actions>
                <action id="editaction"/>
            </actions>
      </step>
```

When enabling the separate RIOXX Compliance workflow step, we recommend you to set the workflow-editable property of the RIOXXComplianceStep in the item-submission.xml file to false.

##### Configuring the navigation menu

The RIOXX item compliance check is accessible *for archived items* from the "Compliance" sidebar menu on an item page.

The message key of the sidebar menu option for the RIOXX compliance check can be configured in spring file *dspace/config/spring/xmlui/item-validation-services.xml*.
The message key is configured in property "navigationKey". 

```
    <bean class="org.dspace.app.xmlui.aspect.compliance.ComplianceUI" id="rioxxComplianceUI">
        <property name="complianceCheckService"  ref="rioxxComplianceCheckService" />
        <property name="name" value="RIOXX Open Access policy"/>
        <property name="shortname" value="RIOXX"/>
        <property name="identifier" value="rioxx"/>
        <property name="navigationKey" value="xmlui.Compliance.Navigation.item-rioxx-compliance"/>
    </bean>
```

This message key should also be configured in messages file *dspace/modules/xmlui/src/main/webapp/i18n/messages.xml*.

```
<message key="xmlui.Compliance.Navigation.item-rioxx-compliance">Item RIOXX compliance</message>
```

The RIOXX compliance check sidebar navigation menu option can be disabled by removing the value of the "navigationKey" property.

```
<property name="navigationKey" value=""/>
```

##### Updating the rule descriptions and resolution hints

The RIOXX rules XML file *dspace/config/rioxx-validation-rules.xml* can be edited to update descriptions and resolution hints. The format of these rules is identical to the one used for the REF Open Access policy (see above).

## In-Depth Documentation
### Metadata fields
The REF Compliance Checker patch uses a few specific metadata fields which we explain in this section.

#### RIOXX Type (rioxxterms.type)
RIOXX type refers to the 'type' - the nature or genre of the content of the resource. Take care not to confuse this with dc:format.

Values recorded here MUST be from the following controlled list of types:
* Book
* Book chapter
* Book edited
* Conference Paper/Proceeding/Abstract
* Journal Article/Review
* Manual/Guide
* Monograph
* Policy briefing report
* Technical Report
* Technical Standard
* Thesis
* Other
* Consultancy Report
* Working paper

Only "Conference Paper/Proceeding/Abstract" and "Journal Article/Review" are applicable for the REF Open Access policy.

#### Date of Acceptance (dcterms.dateAccepted)
The date on which the resource was accepted for publication. The date MUST be encoded using ISO 8601 (post–2004 versions) specifying year, month and day.

#### Print Publication Date (dc.date.issued)
The date the resource was published on print. An item must be deposited as soon after the point of acceptance as possible, and no later than three months after this date. During the first year of the policy (before 1 April 2017), items can be deposited up to three months after the date of print publication instead of the date of acceptance.

#### Online Publication Date (refterms.dateFirstOnline)
If the resource was not published on print, you can also fill in the online publication date in this field. It is also allowed to fill in both values. During the first year of the policy (before 1 April 2017), items can be deposited up to three months after the date of first online publication instead of the date of acceptance.

#### Version of first compliant deposit (refterms.versionFCD)
This field holds the compliant RIOXX version of the resource at the moment of deposit by the submitter. In case of workflows, the version can also be set by the corrected value of a reviewer. The compliant RIOXX versions are:
 * Accepted Manuscript (AM)
 * Proof (P)
 * Version of Record (VoR)
 * Corrected Version of Record (CVoR)
 * Enhanced Version of Record (EVoR)
 
*This field is automatically populated by the REF Compliance Checker and should NOT be part of the input forms or altered manually!*

#### Date of first compliant deposit (refterms.dateFCD)
The date the compliant version of the resource was deposited in the repository by the submitter (see also refterms.versionFCD). In case of workflows, this date can also be the moment a reviewer corrected the submission to be compliant.

*This field is automatically populated by the REF Compliance Checker and should NOT be part of the input forms or altered manually!*

#### Date of first compliant Open Access (refterms.dateFOA)
The first day the compliant version of the resource became available to download as open access.

*This field is automatically populated by the REF Compliance Checker and should NOT be part of the input forms or altered manually!*

#### REF Panel (refterms.panel)
The REF policy makes a number of assumptions about aspects of the next REF that have not yet been formally decided. This has been necessary to provide due notice to the sector of the policy requirement. The main assumption is that there will be a post-2014 REF that operates on substantially the same basis as the 2014 REF. For example, they assume that there will be four main panels with disciplinary remits broadly similar to those of the REF 2014 main panels. This metadata field holds the panel to which the submitter wants to apply.

#### Embargo End Date
The last day of the embargo period of the resource. In a default DSpace installation, the item will be publicly available as open access past this date.

### REF applicable items
The REF Open Access policy only applies to items that have RIOXX type 'Conference Paper/Proceeding/Abstract' (with an ISSN) or 'Journal Article/Review'. In addition, the item must have been accepted after 1 April 2016. 

### REF exceptions
There are a number of allowed exceptions to the requirements defined by the REF Open Access policy. These exceptions cover circumstances where a deposit was not possible, or where open access to deposited material could not be achieved within the policy requirements.

#### Deposit Exceptions (refterms.depositException and refterms.depositExceptionExplanation)
The following deposit exceptions are possible:
 * "noRepositoryAtAcceptance": The individual whose output is being submitted to the REF was unable to secure the use of a repository at the point of acceptance.
 * "delaySecuringText": The individual whose output is being submitted to the REF experienced a delay in securing the final peer-reviewed text (for instance, where a paper has multiple authors).
 * "notEmployedAtUKHEI": The individual whose output is being submitted to the REF was not employed by a UK HEI at the time of submission for publication.
 * "unlawfulDeposit": It would be unlawful to deposit, or request the deposit of, the output.
 * "securityRisk": Depositing the output would present a security risk.
 * "publishedGoldOA": The output was published as 'gold' open access (e.g. RCUK-funded projects where an open access article processing charge has been paid).

Additional motivation of why the exception applies, will be filled in by the submitter in field *refterms.depositExceptionExplanation*.

#### Access Exceptions (refterms.accessException and refterms.accessExceptionExplanation)
The following deposit exceptions are possible:
 * "thirdPartyRights": The output depends on the reproduction of third party content for which open access rights could not be granted (either within the specified timescales, or at all).
 * "publicationExceedsMaxEmbargo": The publication concerned requires an embargo period that exceeds the stated maxima, and was the most appropriate publication for the output.
 * "publicationDisallowsOA": The publication concerned actively disallows open-access deposit in a repository, and was the most appropriate publication for the output.

Additional motivation of why the exception applies, will be filled in by the submitter in field *refterms.accessExceptionExplanation*.

#### Technical Exceptions (refterms.technicalException and refterms.technicalExceptionExplanation)
Sometimes an item is unable to meet the criteria due to a technical issue. In the event of audit, the institution must be able to provide proof justifying the exception. The following technical exceptions are possible:
 * "conferenceOutsideDefinition": Output is a conference proceeding, but not within definition (i.e., it does not have an ISSN, or the proceedings are published as part of book series).
 * "differentUKHEI": At the point of acceptance, the individual whose output is being submitted to the REF was at a different UK HEI which failed to comply with the criteria.
 * "technicalFailure": The repository experienced a short term or transient technical failure that prevented compliance with the criteria (this should not apply to systemic issues).
 * "externalServiceProvider": An external service provider failure prevented compliance (for instance, a subject repository did not enable open access at the end of the embargo period, or a subject repository ceased to operate).
 
Additional proof of why the exception applies, will be filled in by the submitter in field *refterms.technicalExceptionExplanation*.

#### Other Exceptions (refterms.exceptionFreeText)
In very exceptional cases, it may not be possible for an output to meet the open access requirements set out by this policy for a reason not covered by the other exceptions and a short explanation for this is provided. In the event of audit, the institution must be able to provide proof justifying the exception in field *refterms.exceptionFreeText*.

### Estimated values
The values for metadata fields refterms.versionFCD, refterms.dateFCD and refterms.dateFOA are determined by the REF Compliance Checker. At some points in the lifecycle of an item, these metadata fields will not have a value. 
For example, during the submission the "version of first compliant deposit" and "date of first compliant deposit" cannot have a value since the item has not been deposited yet. Items that are under embargo cannot have a "date of first open access" as long as the embargo still applies.

To determine REF compliance of items that do not have a value for all REF related metadata fields, the REF Compliance Checker will make an estimate of the future value for those metadata fields. Estimated values are indicated by a star (*) on the item compliance overview page. 

### Disabling or Adding Additional Rules
All REF (or RIOXX) compliance policy rules are defined in XML (*config/ref-validation-rules.xml* and *config/rioxx-validation-rules.xml* respectively). This means a repository administrator can disable certain rules by removing them from the XML file or add new, institution-specific rules. The format in which to write new rules is described in the XSD schema in file *config/item-validation-rules.xsd*.

Currently, the rule framework has support for following rule types (defined in file *config/spring/api/item-validation-services.xml*):
 * "value" rules: This type of rule can check if a specified metadata field has a certain value. The rule definition allows you to specify a list of possible values.
   * The required definition fields are: field, fieldDescription and fieldValue.
 * "notBlank" rules: These rules will check if the specified metadata field has a non-blank value.
   * The required definition fields are: field and fieldDescription.
 * "discoverable" rules: This rule type will check if an item is discoverable within DSpace by an anonymous user.
   * There are no required definition fields.
 * "countGreaterThan" rules: You can use this rule type to check if a given item field counts more values then the specified threshold. The field "bitstreams" is a special field that applies to the bitstreams of the item.
   * The required definition fields are: field, fieldDescription and fieldValue.
 * "dateRangeSmallerThanMonths" rules: These rules check if the date range specified by the from and to values, is smaller than the given threshold (in months).
   * The required definition fields are: from, to, fieldDescription and fieldValue.
 * "dateSmallerThan" rules: With this type of rule, you can check if the date in the provided metadata field is smaller than the specified threshold.
   * The required definition fields are: field, fieldDescription and fieldValue.
 * "dateGreaterThanOrEqual" rules: A rule to check if the date in a metadata field is greater or equal to a provided value.
   * The required definition fields are: field, fieldDescription and fieldValue.
 * "atLeastOneNotBlank" rules: This rules allows you to check if at least of the defined metadata fields has a non-blank value.
   * The required definition fields are: field (multiple times) and fieldDescription (general description of all listed fields together).

## Troubleshooting
### Patch installation
If you are receiving errors similar to the message below, then you are most likely using the **wrong directory** (i.e., not the parent directory of your DSpace installation). Please make sure that the current directory contains a directory called "dspace", which contains (amongst others) the subdirectories "bin", "config", "modules" and "solr". If this is not the case, then you will most probably receive errors such as:

```
error: dspace/config/crosswalks/oai/xoai.xml: No such file or directory
...
```

Another problem could occur if the DSpace installation has been customized in such a way that the Application Profile patch cannot be applied with creating versioning conflicts with your own customizations. This will trigger errors similar to the message below:

```
...
error: patch failed: dspace/pom.xml:62
error: dspace/pom.xml: patch does not apply
...
```
This can be solved by "rejecting" the files where the patch does not apply. This will allow the patch to be applied to the "non-failing" files.
If you use this option, you must still hand-apply each failing patch, and figure out what to do with the rejected portions.