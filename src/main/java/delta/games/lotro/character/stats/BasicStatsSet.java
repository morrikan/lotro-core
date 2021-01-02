package delta.games.lotro.character.stats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Set of basic stats.
 * @author DAM
 */
public class BasicStatsSet
{
  private static final Logger LOGGER=Logger.getLogger(BasicStatsSet.class);

  private List<StatsSetElement> _elements;

  /**
   * Constructor.
   */
  public BasicStatsSet()
  {
    _elements=new ArrayList<StatsSetElement>();
  }

  /**
   * Copy constructor.
   * @param source Source object.
   */
  public BasicStatsSet(BasicStatsSet source)
  {
    _elements=new ArrayList<StatsSetElement>();
    setStats(source);
  }

  /**
   * Get a stat value.
   * @param stat Stat to get.
   * @return A stat value or <code>null</code> if not found.
   */
  public FixedDecimalsInteger getStat(StatDescription stat)
  {
    StatsSetElement existing=findElement(stat);
    return (existing!=null)?existing.getValue():null;
  }

  /**
   * Remove all stats.
   */
  public void clear()
  {
    _elements.clear();
  }

  /**
   * Get all registered stats keys.
   * @return A set of stat keys.
   */
  public Set<StatDescription> getStats()
  {
    Set<StatDescription> ret=new HashSet<StatDescription>();
    for(StatsSetElement element : _elements)
    {
      ret.add(element.getStat());
    }
    return ret;
  }

  /**
   * Get the managed elements.
   * @return a list of stat elements.
   */
  public List<StatsSetElement> getStatElements()
  {
    return _elements;
  }

  /**
   * Get the number of defined stats in this set.
   * @return a count.
   */
  public int getStatsCount()
  {
    return _elements.size();
  }

  /**
   * Remove a stat.
   * @param stat Targeted stat.
   */
  public void removeStat(StatDescription stat)
  {
    int i=0;
    while (i<_elements.size())
    {
      if (_elements.get(i).getStat()==stat)
      {
        _elements.remove(i);
      }
      else
      {
        i++;
      }
    }
  }

  /**
   * Set stat value.
   * @param stat Stat to set.
   * @param value Value to set.
   */
  public void setStat(StatDescription stat, FixedDecimalsInteger value)
  {
    setStat(stat,new FixedDecimalsInteger(value),null);
  }

  /**
   * Set stat value.
   * @param stat Stat to set.
   * @param value Value to set.
   */
  public void setStat(StatDescription stat, int value)
  {
    setStat(stat,new FixedDecimalsInteger(value),null);
  }

  /**
   * Set stat value.
   * @param stat Stat to set.
   * @param value Value to set.
   */
  public void setStat(StatDescription stat, float value)
  {
    setStat(stat,new FixedDecimalsInteger(value),null);
  }

  /**
   * Set stat value.
   * @param stat Stat to set.
   * @param value Value to set.
   * @param descriptionOverride Description override.
   */
  public void setStat(StatDescription stat, FixedDecimalsInteger value, String descriptionOverride)
  {
    StatsSetElement element=new StatsSetElement(stat,value,descriptionOverride);
    internalSetStat(element);
  }

  private void internalSetStat(StatsSetElement elementToSet)
  {
    StatsSetElement existing=findElement(elementToSet.getStat());
    if (existing!=null)
    {
      // Update this one
      if (!Objects.equals(existing.getDescriptionOverride(),elementToSet.getDescriptionOverride()))
      {
        LOGGER.warn("Set stat will replace description!");
      }
      existing.setValue(elementToSet.getValue());
      existing.setDescriptionOverride(elementToSet.getDescriptionOverride());
    }
    else
    {
      _elements.add(elementToSet);
    }
  }

  /**
   * Copy stats from a source.
   * @param stats Source stats.
   */
  public void setStats(BasicStatsSet stats)
  {
    _elements.clear();
    for(StatsSetElement element : stats._elements)
    {
      FixedDecimalsInteger value=new FixedDecimalsInteger(element.getValue());
      StatsSetElement newElement=new StatsSetElement(element.getStat(),value,element.getDescriptionOverride());
      _elements.add(newElement);
    }
  }

  /**
   * Add stat value.
   * @param stat Stat to set.
   * @param value Value to set.
   */
  public void addStat(StatDescription stat, FixedDecimalsInteger value)
  {
    StatsSetElement element=new StatsSetElement(stat,value,null);
    addStat(element);
  }

  /**
   * Add some stats.
   * @param stats Stats to add.
   */
  public void addStats(BasicStatsSet stats)
  {
    for(StatsSetElement element : stats._elements)
    {
      addStat(element);
    }
  }

  /**
   * Add a new stat element.
   * @param elementToAdd Stat element to add.
   */
  public void addStat(StatsSetElement elementToAdd)
  {
    StatsSetElement existing=findElement(elementToAdd.getStat());
    if (existing!=null)
    {
      FixedDecimalsInteger total=new FixedDecimalsInteger(existing.getValue());
      total.add(elementToAdd.getValue());
      // Update this one
      if (!Objects.equals(existing.getDescriptionOverride(),elementToAdd.getDescriptionOverride()))
      {
        //LOGGER.warn("Add stat will replace description!");
      }
      existing.setDescriptionOverride(elementToAdd.getDescriptionOverride());
      existing.setValue(total);
    }
    else
    {
      _elements.add(new StatsSetElement(elementToAdd));
    }
  }

  private StatsSetElement findElement(StatDescription stat)
  {
    for(StatsSetElement element : _elements)
    {
      if (element.getStat()==stat)
      {
        return element;
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object object)
  {
    if (this==object) return true;
    if (!(object instanceof BasicStatsSet))
    {
      return false;
    }
    BasicStatsSet other=(BasicStatsSet)object;
    return _elements.equals(other._elements);
  }

  @Override
  public String toString()
  {
    return toString(" / ");
  }

  /**
   * Dump the contents of this stats set.
   * @return A string with one stat on each line.
   */
  public String dump()
  {
    return toString(EndOfLine.NATIVE_EOL);
  }

  private String toString(String separator)
  {
    StringBuilder sb=new StringBuilder();
    int index=0;
    for(StatsSetElement element : _elements)
    {
      if (index>0)
      {
        sb.append(separator);
      }
      sb.append(StatUtils.getStatDisplay(element));
      index++;
    }
    return sb.toString().trim();
  }
}
