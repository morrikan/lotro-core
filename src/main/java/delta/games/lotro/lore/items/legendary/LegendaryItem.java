package delta.games.lotro.lore.items.legendary;

import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemCategory;

/**
 * Legendary item description.
 * @author DAM
 */
public class LegendaryItem extends Item implements Legendary
{
  private LegendaryAttrs _attrs;

  /**
   * Constructor.
   */
  public LegendaryItem()
  {
    setCategory(ItemCategory.LEGENDARY_ITEM);
    _attrs=new LegendaryAttrs();
  }

  @Override
  public LegendaryAttrs getLegendaryAttrs()
  {
    return _attrs;
  }
}
