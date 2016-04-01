package delta.games.lotro.lore.items.essences;

import delta.common.utils.text.EndOfLine;

/**
 * Set of essences.
 * @author DAM
 */
public class EssencesSet
{
  private Essence[] _essences;

  /**
   * Constructor.
   * @param size Maximum number of essences in this set.
   */
  public EssencesSet(int size)
  {
    _essences=new Essence[size];
  }

  /**
   * Get the maximum number of essences in this set.
   * @return a number of essences.
   */
  public int getSize()
  {
    return _essences.length;
  }

  /**
   * Get the essence stored at the given index.
   * @param index Index, starting at 0.
   * @return An essence or <code>null</code> if none set.
   */
  public Essence getEssence(int index)
  {
    return _essences[index];
  }

  /**
   * Set the essence at the given index.
   * @param index Index, starting at 0.
   * @param essence Essence to set.
   */
  public void setEssence(int index, Essence essence)
  {
    _essences[index]=essence;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    for(int i=0;i<_essences.length;i++)
    {
      Essence essence=getEssence(i);
      if (essence!=null)
      {
        sb.append(essence);
      }
      else
      {
        sb.append('-');
      }
      sb.append(EndOfLine.NATIVE_EOL);
    }
    return sb.toString().trim();
  }
}