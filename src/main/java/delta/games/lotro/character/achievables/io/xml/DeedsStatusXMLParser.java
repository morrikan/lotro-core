package delta.games.lotro.character.achievables.io.xml;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.DeedsStatusManager;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;
import delta.games.lotro.character.achievables.edition.OldMarkersStateUpdater;

/**
 * Parser for the deeds status stored in XML.
 * @author DAM
 */
public class DeedsStatusXMLParser
{
  private static final Logger LOGGER=Logger.getLogger(DeedsStatusXMLParser.class);

  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed status or <code>null</code>.
   */
  public DeedsStatusManager parseXML(File source)
  {
    DeedsStatusManager status=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      status=parseStatus(root);
    }
    return status;
  }

  private DeedsStatusManager parseStatus(Element root)
  {
    DeedsStatusManager status=new DeedsStatusManager();
    List<Element> deedStatusTags=DOMParsingTools.getChildTagsByName(root,DeedStatusXMLConstants.DEED_STATUS_TAG,false);
    for(Element deedStatusTag : deedStatusTags)
    {
      parseDeedStatus(status,deedStatusTag);
    }
    return status;
  }

  private void parseDeedStatus(DeedsStatusManager status, Element deedStatusTag)
  {
    NamedNodeMap attrs=deedStatusTag.getAttributes();
    String key=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.DEED_STATUS_KEY_ATTR,null);
    if (key==null)
    {
      // No deed key!
      LOGGER.warn("No deed key!");
      return;
    }
    // Create deed status
    AchievableStatus deedStatus=status.get(key,true);
    if (deedStatus==null)
    {
      // Unknown deed!
      LOGGER.warn("Unknown deed: "+key);
      return;
    }
    // State
    String stateStr=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.DEED_STATUS_STATE_ATTR,null);
    if (stateStr!=null)
    {
      AchievableElementState state=parseState(stateStr);
      deedStatus.setState(state);
    }
    else
    {
      boolean completed=DOMParsingTools.getBooleanAttribute(attrs,DeedStatusXMLConstants.DEED_STATUS_COMPLETED_ATTR,false);
      deedStatus.setCompleted(completed);
    }
    // Completion date
    String completionDateStr=DOMParsingTools.getStringAttribute(attrs,DeedStatusXMLConstants.DEED_STATUS_COMPLETION_DATE_ATTR,null);
    if (completionDateStr!=null)
    {
      Long completionDate=NumericTools.parseLong(completionDateStr);
      deedStatus.setCompletionDate(completionDate);
    }
    // Objectives status
    parseObjectivesStatus(deedStatusTag,deedStatus);
    // Old markers status
    if (deedStatus.getState()!=AchievableElementState.COMPLETED)
    {
      parseOldMarkersStatus(deedStatusTag,deedStatus);
    }
    // Update internal states
    deedStatus.updateInternalState();
  }

  private void parseOldMarkersStatus(Element deedStatusTag, AchievableStatus deedStatus)
  {
    Set<Integer> completedPointIds=new HashSet<Integer>();
    List<Element> pointStatusTags=DOMParsingTools.getChildTagsByName(deedStatusTag,"geoPointStatus");
    if (pointStatusTags.size()>0)
    {
      for(Element pointStatusTag : pointStatusTags)
      {
        NamedNodeMap pointAttrs=pointStatusTag.getAttributes();
        int pointId=DOMParsingTools.getIntAttribute(pointAttrs,"pointId",0);
        boolean completed=DOMParsingTools.getBooleanAttribute(pointAttrs,"completed",false);
        if (completed)
        {
          completedPointIds.add(Integer.valueOf(pointId));
        }
      }
    }
    if (completedPointIds.size()>0)
    {
      OldMarkersStateUpdater.updateDeedStatus(deedStatus,completedPointIds);
    }
  }

  /**
   * Load achievable objectives status from the given XML stream.
   * @param deedStatusTag Status tag.
   * @param status Status to write.
   */
  private void parseObjectivesStatus(Element deedStatusTag, AchievableStatus status)
  {
    List<Element> objectiveStatusTags=DOMParsingTools.getChildTagsByName(deedStatusTag,DeedStatusXMLConstants.OBJECTIVE_STATUS_TAG);
    for(Element objectiveStatusTag : objectiveStatusTags)
    {
      NamedNodeMap objectiveAttrs=objectiveStatusTag.getAttributes();
      // Find objective by index
      int objectiveIndex=DOMParsingTools.getIntAttribute(objectiveAttrs,DeedStatusXMLConstants.OBJECTIVE_STATUS_INDEX_ATTR,-1);
      AchievableObjectiveStatus objectiveStatus=status.getObjectiveStatus(objectiveIndex);
      if (objectiveStatus==null)
      {
        LOGGER.warn("Objective not found: achievable ID="+status.getAchievableId()+" - index="+objectiveIndex);
        continue;
      }
      // State
      String stateStr=DOMParsingTools.getStringAttribute(objectiveAttrs,DeedStatusXMLConstants.OBJECTIVE_STATUS_STATE_ATTR,null);
      AchievableElementState state=parseState(stateStr);
      objectiveStatus.setState(state);
      // Conditions
      parseConditionsStatus(objectiveStatusTag,objectiveStatus);
    }
  }

  /**
   * Load objective conditions status from the given XML stream.
   * @param objectiveStatusTag Status tag.
   * @param objectiveStatus Status to write.
   */
  private void parseConditionsStatus(Element objectiveStatusTag, AchievableObjectiveStatus objectiveStatus)
  {
    List<Element> conditionStatusTags=DOMParsingTools.getChildTagsByName(objectiveStatusTag,DeedStatusXMLConstants.CONDITION_STATUS_TAG);
    for(Element conditionStatusTag : conditionStatusTags)
    {
      NamedNodeMap conditionAttrs=conditionStatusTag.getAttributes();
      // Find condition by index
      int conditionIndex=DOMParsingTools.getIntAttribute(conditionAttrs,DeedStatusXMLConstants.CONDITION_STATUS_INDEX_ATTR,-1);
      ObjectiveConditionStatus conditionStatus=objectiveStatus.getConditionStatus(conditionIndex);
      if (conditionStatus==null)
      {
        LOGGER.warn("Condition not found: objective index="+objectiveStatus.getObjective().getIndex()+" - condition index="+conditionIndex);
        continue;
      }
      // State
      String stateStr=DOMParsingTools.getStringAttribute(conditionAttrs,DeedStatusXMLConstants.CONDITION_STATUS_STATE_ATTR,null);
      AchievableElementState state=parseState(stateStr);
      conditionStatus.setState(state);
      // Count
      int countValue=DOMParsingTools.getIntAttribute(conditionAttrs,DeedStatusXMLConstants.CONDITION_STATUS_COUNT_ATTR,-1);
      Integer count=(countValue>=0)?Integer.valueOf(countValue):null;
      conditionStatus.setCount(count);
      // Keys
      String keys=DOMParsingTools.getStringAttribute(conditionAttrs,DeedStatusXMLConstants.CONDITION_STATUS_KEYS_ATTR,null);
      if (keys!=null)
      {
        String[] keysArray=keys.split(",");
        for(String key : keysArray)
        {
          conditionStatus.addKey(key);
        }
      }
    }
  }

  private AchievableElementState parseState(String stateStr)
  {
    AchievableElementState ret=null;
    if (stateStr!=null)
    {
      try
      {
        ret=AchievableElementState.valueOf(stateStr);
      }
      catch(Exception e)
      {
        // Ignored
      }
    }
    if (ret==null)
    {
      ret=AchievableElementState.UNDEFINED;
    }
    return ret;
  }
}
