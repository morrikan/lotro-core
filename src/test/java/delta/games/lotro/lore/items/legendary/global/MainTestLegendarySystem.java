package delta.games.lotro.lore.items.legendary.global;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.legendary.Legendary;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegaciesManager;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegacyTier;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacy;

/**
 * Test class for the legendary system.
 * @author DAM
 */
public class MainTestLegendarySystem
{
  private LegendarySystem _legendarySystem;

  private void doIt()
  {
    _legendarySystem=new LegendarySystem();
    //doItem(1879219224); // Reshaped Hunter's Crossbow of the First Age (75)
    //doItem(1879311761); // Reshaped Hunter's Axe of the First Age (100)
    doAllItems();
  }

  private void doAllItems()
  {
    ItemsManager itemsMgr=ItemsManager.getInstance();
    List<Item> items=itemsMgr.getAllItems();
    for(Item item : items)
    {
      if (item instanceof Legendary)
      {
        doItem(item.getIdentifier());
      }
    }
  }

  private Set<String> done=new HashSet<String>();

  private void doItem(int itemId)
  {
    Item item=ItemsManager.getInstance().getItem(itemId);
    CharacterClass characterClass=item.getRequiredClass();
    if (characterClass==null) return; // Skip bridles
    EquipmentLocation slot=item.getEquipmentLocation();
    ItemQuality quality=item.getQuality();
    Integer itemLevel=item.getItemLevel();
    String key=characterClass.getKey()+"#"+slot+"#"+quality.getKey()+"#"+itemLevel;
    if (done.contains(key))
    {
      return;
    }
    done.add(key);
    ItemInstance<? extends Item> itemInstance=ItemFactory.buildInstance(item);
    NonImbuedLegaciesManager legaciesMgr=NonImbuedLegaciesManager.getInstance();
    List<TieredNonImbuedLegacy> legacies=legaciesMgr.getTieredLegacies(characterClass,slot);
    for(TieredNonImbuedLegacy legacy : legacies)
    {
      System.out.println("Legacy: "+legacy.getStat().getName());
      List<NonImbuedLegacyTier> tiers=legacy.getTiers();
      //for(NonImbuedLegacyTier tier : tiers)
      NonImbuedLegacyTier tier=tiers.get(0);
      {
        System.out.println("\tTier "+tier.getTier()+": ");
        /*
        Integer rank=_legendarySystem.getRankForUiRank(itemInstance,tier,1);
        BasicStatsSet stats=tier.getEffect().getStatsProvider().getStats(1,rank.intValue());
        System.out.println("\t\t"+stats);
        */
        int[] ranks=_legendarySystem.getRanks(itemInstance,tier);
        if (ranks!=null)
        {
          for(int i=0;i<ranks.length;i++)
          {
            BasicStatsSet stats=tier.getEffect().getStatsProvider().getStats(1,ranks[i]);
            System.out.println("Rank "+(i+1)+": "+stats);
          }
        }
      }
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestLegendarySystem().doIt();
  }
}
