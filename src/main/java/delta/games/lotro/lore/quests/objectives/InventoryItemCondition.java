package delta.games.lotro.lore.quests.objectives;

/**
 * Inventory item condition.
 * @author DAM
 */
public class InventoryItemCondition extends ItemCondition
{
  @Override
  public ConditionType getType()
  {
    return ConditionType.INVENTORY_ITEM;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("#").append(getIndex());
    if (_item!=null)
    {
      sb.append(": Get item: ").append(_item);
      int count=getCount();
      if (count>1)
      {
        sb.append(count).append(" x");
      }
    }
    return sb.toString();
  }
}
