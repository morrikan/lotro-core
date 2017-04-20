package delta.games.lotro.lore.items.stats;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.Slice;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * COomputes item stats from slice-based formulas.
 * @author DAM
 */
public class SlicesBasedItemStatsProvider implements ItemStatsProvider
{
  private static final Logger LOGGER=Logger.getLogger(SlicesBasedItemStatsProvider.class);

  private ScaledArmourComputer _armorComputer;
  private List<ItemStatSliceData> _slices;
  private BasicStatsSet _stats;

  /**
   * Constructor.
   */
  public SlicesBasedItemStatsProvider()
  {
    _armorComputer=new ScaledArmourComputer();
    _slices=new ArrayList<ItemStatSliceData>();
    _stats=new BasicStatsSet();
  }

  /**
   * Add a new slice.
   * @param slice Slice parameters.
   */
  public void addSlice(ItemStatSliceData slice)
  {
    _slices.add(slice);
  }

  /**
   * Set a stat value.
   * @param stat Targeted stat.
   * @param value Stat value.
   */
  public void setStat(STAT stat, float value)
  {
    _stats.setStat(stat,new FixedDecimalsInteger(value));
  }

  /**
   * Set a stat value.
   * @param stat Targeted stat.
   * @param value Stat value.
   */
  public void setStat(STAT stat, int value)
  {
    _stats.setStat(stat,new FixedDecimalsInteger(value));
  }

  /**
   * Get the number of slices.
   * @return a count.
   */
  public int getSlices()
  {
    return _slices.size();
  }

  public BasicStatsSet getStats(int itemLevel)
  {
    BasicStatsSet stats=new BasicStatsSet();
    for(ItemStatSliceData slice : _slices)
    {
      STAT stat=slice.getStat();
      float statValue=getStatValue(slice,itemLevel);
      stats.setStat(stat,statValue);
    }
    stats.setStats(_stats);
    return stats;
  }

  private float getStatValue(ItemStatSliceData slice, int itemLevel)
  {
    double value=getSliceValue(itemLevel, slice);
    // For the moment, round value to keep computed values the same as old,
    // non computed values
    float statValue=Math.round(value);
    return statValue;
  }

  private double getSliceValue(int itemLevel, ItemStatSliceData slice)
  {
    STAT stat=slice.getStat();
    Float sliceCountFloat=slice.getSliceCount();
    float sliceCount=(sliceCountFloat!=null)?sliceCountFloat.floatValue():1;
    if ((stat==STAT.MIGHT) || (stat==STAT.AGILITY) || (stat==STAT.WILL) ||
        (stat==STAT.VITALITY) || (stat==STAT.FATE))
    {
      return Slice.getBaseStat(itemLevel,sliceCount);
    }
    if (stat==STAT.MORALE)
    {
      return Slice.getMorale(itemLevel,sliceCount);
    }
    if (stat==STAT.POWER)
    {
      return Slice.getPower(itemLevel,sliceCount);
    }
    if (stat==STAT.PHYSICAL_MASTERY)
    {
      return Slice.getPhysicalMastery(itemLevel,sliceCount);
    }
    if (stat==STAT.TACTICAL_MASTERY)
    {
      return Slice.getTacticalMastery(itemLevel,sliceCount);
    }
    if (stat==STAT.CRITICAL_RATING)
    {
      return Slice.getCriticalRating(itemLevel,sliceCount);
    }
    if (stat==STAT.FINESSE)
    {
      return Slice.getFinesse(itemLevel,sliceCount);
    }
    if (stat==STAT.INCOMING_HEALING)
    {
      return Slice.getIncomingHealing(itemLevel,sliceCount);
    }
    if (stat==STAT.RESISTANCE)
    {
      return Slice.getResist(itemLevel,sliceCount);
    }
    if ((stat==STAT.BLOCK) || (stat==STAT.PARRY) || (stat==STAT.EVADE))
    {
      return Slice.getBPE(itemLevel,sliceCount);
    }
    if (stat==STAT.PHYSICAL_MITIGATION)
    {
      return Slice.getPhysicalMitigation(itemLevel,sliceCount);
    }
    if (stat==STAT.TACTICAL_MITIGATION)
    {
      return Slice.getTacticalMitigation(itemLevel,sliceCount);
    }
    if (stat==STAT.CRITICAL_DEFENCE)
    {
      return Slice.getCriticalDefence(itemLevel,sliceCount);
    }
    if (stat==STAT.ARMOUR)
    {
      String armType=slice.getAdditionalParameter();
      return getArmorStat(armType.toUpperCase(),itemLevel,sliceCount);
    }
    return 0;
  }

  private double getArmorStat(String armType, int itemLevel, float sliceCount)
  {
    String armorClass=armType.substring(3,4);
    String armorType=armType.substring(4,5);
    String armorColor=armType.substring(5,6);
    if (("S".equals(armorType)) && ("H".equals(armorColor)))
    {
      armorType="SH";
      armorColor=armType.substring(6,7);
    }
    if (("C".equals(armorClass)) && ("L".equals(armorType)))
    {
      armorClass="L";
      armorType="CL";
      armorColor=armType.substring(5,6);
    }
    EquipmentLocation slot=null;
    if ("H".equals(armorType)) slot=EquipmentLocation.HEAD;
    if ("S".equals(armorType)) slot=EquipmentLocation.SHOULDER;
    if ("C".equals(armorType)) slot=EquipmentLocation.CHEST;
    if ("G".equals(armorType)) slot=EquipmentLocation.HAND;
    if ("L".equals(armorType)) slot=EquipmentLocation.LEGS;
    if ("B".equals(armorType)) slot=EquipmentLocation.FEET;
    if ("SH".equals(armorType)) slot=EquipmentLocation.OFF_HAND;
    if ("CL".equals(armorType)) slot=EquipmentLocation.BACK;
  
    ArmourType type=null;
    if ("H".equals(armorClass)) type=ArmourType.HEAVY;
    if ("M".equals(armorClass)) type=ArmourType.MEDIUM;
    if ("L".equals(armorClass)) type=ArmourType.LIGHT;
  
    ItemQuality quality=null;
    if ("G".equals(armorColor)) quality=ItemQuality.LEGENDARY;
    if ("P".equals(armorColor)) quality=ItemQuality.RARE;
    if ("T".equals(armorColor)) quality=ItemQuality.INCOMPARABLE;
    if ("Y".equals(armorColor)) quality=ItemQuality.UNCOMMON;
  
    if ((slot!=null) && (type!=null) && (quality!=null))
    {
      return _armorComputer.getArmour(itemLevel,type,slot,quality,sliceCount);
    }
    LOGGER.warn("Unmanaged armor type:" + armType);
    return 0;
  }

  /**
   * Build a new instance from a persisted string.
   * @param value Value to use.
   * @return the newly built instance or <code>null</code> if not valid.
   */
  public static SlicesBasedItemStatsProvider fromPersistedString(String value)
  {
    // TODO
    return null;
  }

  /**
   * Get a persistable string for this object.
   * @return A persistable stirng.
   */
  public String toPersistableString()
  {
    String ret="";
    if (_slices.size()>0)
    {
      StringBuilder sb=new StringBuilder();
      for(ItemStatSliceData slice :_slices)
      {
        if (sb.length()>0)
        {
          sb.append(';');
        }
        slice.toString(sb);
      }
      ret=sb.toString();
    }
    return ret;
  }
}
