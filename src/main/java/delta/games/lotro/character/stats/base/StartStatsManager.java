package delta.games.lotro.character.stats.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.CharacterClass;

/**
 * Manager for character start stats.
 * @author DAM
 */
public class StartStatsManager
{
  private static final Logger LOGGER=Logger.getLogger(StartStatsManager.class);

  private HashMap<CharacterClass,HashMap<Integer,BasicStatsSet>> _startStatsByClass;

  /**
   * Constructor.
   */
  public StartStatsManager()
  {
    _startStatsByClass=new HashMap<CharacterClass,HashMap<Integer,BasicStatsSet>>();
  }

  /**
   * Set the start stats for a character class and level.
   * @param characterClass Character class.
   * @param level Level, starting at 1.
   * @param stats Start stats.
   */
  public void setStats(CharacterClass characterClass, int level, BasicStatsSet stats)
  {
    HashMap<Integer,BasicStatsSet> mapForClass=getMapForClass(characterClass);
    mapForClass.put(Integer.valueOf(level),stats);
  }

  /**
   * Get the available levels for a given class.
   * @param characterClass Character class.
   * @return A list of sorted levels.
   */
  public List<Integer> getLevels(CharacterClass characterClass)
  {
    HashMap<Integer,BasicStatsSet> mapForClass=getMapForClass(characterClass);
    List<Integer> levels=new ArrayList<Integer>(mapForClass.keySet());
    Collections.sort(levels);
    return levels;
  }

  /**
   * Get the start stats for a given character class and level.
   * @param characterClass Character class.
   * @param level Level, starting at 1.
   * @return Some stats or <code>null</code> if not supported.
   */
  public BasicStatsSet getStats(CharacterClass characterClass, int level)
  {
    BasicStatsSet ret=null;
    HashMap<Integer,BasicStatsSet> mapForClass=getMapForClass(characterClass);
    if (mapForClass!=null)
    {
      ret=mapForClass.get(Integer.valueOf(level));
    }
    if (ret==null)
    {
      LOGGER.warn("Could not find start stats for class="+characterClass+", level="+level);
      ret=new BasicStatsSet();
    }
    return ret;
  }

  private HashMap<Integer,BasicStatsSet> getMapForClass(CharacterClass characterClass)
  {
    HashMap<Integer,BasicStatsSet> ret=_startStatsByClass.get(characterClass);
    if (ret==null)
    {
      ret=new HashMap<Integer,BasicStatsSet>();
      _startStatsByClass.put(characterClass,ret);
    }
    return ret;
  }
}
