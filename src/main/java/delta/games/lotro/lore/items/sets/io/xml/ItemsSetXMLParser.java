package delta.games.lotro.lore.items.sets.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.common.stats.io.xml.StatsProviderXMLParser;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.ItemsSetBonus;
import delta.games.lotro.utils.Proxy;

/**
 * Parser for items sets descriptions stored in XML.
 * @author DAM
 */
public class ItemsSetXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed items set or <code>null</code>.
   */
  public List<ItemsSet> parseXML(File source)
  {
    List<ItemsSet> sets=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      sets=parseSets(root);
    }
    return sets;
  }

  private List<ItemsSet> parseSets(Element root)
  {
    List<ItemsSet> ret=new ArrayList<ItemsSet>();
    List<Element> itemsSetTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.ITEMS_SET_TAG);
    for(Element itemsSetTag : itemsSetTags)
    {
      ItemsSet set=parseSet(itemsSetTag);
      ret.add(set);
    }
    return ret;
  }

  private ItemsSet parseSet(Element root)
  {
    ItemsSet ret=new ItemsSet();

    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_ID_ATTR,0);
    ret.setIdentifier(id);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_NAME_ATTR,"");
    ret.setName(name);
    // Level
    int level=DOMParsingTools.getIntAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_LEVEL_ATTR,0);
    ret.setLevel(level);
    // Required level
    int requiredLevel=DOMParsingTools.getIntAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_REQUIRED_LEVEL_ATTR,1);
    ret.setRequiredLevel(requiredLevel);
    // Description
    String description=DOMParsingTools.getStringAttribute(attrs,ItemsSetXMLConstants.ITEMS_SET_DESCRIPTION_ATTR,"");
    ret.setDescription(description);

    // Items
    List<Element> itemTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.ITEM_TAG);
    if (itemTags!=null)
    {
      for(Element itemTag : itemTags)
      {
        int memberId=DOMParsingTools.getIntAttribute(itemTag.getAttributes(),ItemsSetXMLConstants.ITEM_ID_ATTR,0);
        //String memberName=DOMParsingTools.getStringAttribute(itemTag.getAttributes(),ItemsSetXMLConstants.ITEM_NAME_ATTR,"");
        Item member=ItemsManager.getInstance().getItem(memberId);
        if (member!=null)
        {
          Proxy<Item> proxy=new Proxy<Item>();
          proxy.setId(memberId);
          proxy.setName(member.getName());
          proxy.setObject(member);
          ret.addMember(proxy);
        }
      }
    }
    // Bonuses
    List<Element> bonusTags=DOMParsingTools.getChildTagsByName(root,ItemsSetXMLConstants.BONUS_TAG);
    for(Element bonusTag : bonusTags)
    {
      NamedNodeMap bonusAttrs=bonusTag.getAttributes();
      int piecesCount=DOMParsingTools.getIntAttribute(bonusAttrs,ItemsSetXMLConstants.BONUS_NB_ITEMS_ATTR,0);
      StatsProvider statsProvider=StatsProviderXMLParser.parseStatsProvider(bonusTag);
      if ((piecesCount!=0) && (statsProvider!=null))
      {
        ItemsSetBonus bonus=new ItemsSetBonus(piecesCount);
        bonus.setStatsProvider(statsProvider);
        ret.addBonus(bonus);
      }
    }
    return ret;
  }
}
