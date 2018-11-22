package delta.games.lotro.common.stats;

import delta.games.lotro.character.stats.STAT;

/**
 * Constant stat value provider.
 * @author DAM
 */
public class ConstantStatProvider extends AbstractStatProvider
{
  private Float _value;

  /**
   * Constructor.
   * @param stat Targeted stat.
   * @param value Stat value.
   */
  public ConstantStatProvider(STAT stat, float value)
  {
    super(stat);
    _value=Float.valueOf(value);
  }

  /**
   * Get the stat value.
   * @return the stat value.
   */
  public float getValue()
  {
    return _value.floatValue();
  }

  @Override
  public Float getStatValue(int tier, int level)
  {
    return _value;
  }
}