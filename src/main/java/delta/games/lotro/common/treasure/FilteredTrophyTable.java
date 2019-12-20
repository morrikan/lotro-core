package delta.games.lotro.common.treasure;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.Identifiable;

/**
 * Filtered trophy table.
 * @author DAM
 */
public class FilteredTrophyTable implements Identifiable
{
  private int _id;
  private List<FilteredTrophyTableEntry> _entries;

  /**
   * Constructor.
   * @param identifier Identifier.
   */
  public FilteredTrophyTable(int identifier)
  {
    _id=identifier;
    _entries=new ArrayList<FilteredTrophyTableEntry>();
  }

  public int getIdentifier()
  {
    return _id;
  }

  /**
   * Add an entry.
   * @param entry Entry to add.
   */
  public void addEntry(FilteredTrophyTableEntry entry)
  {
    _entries.add(entry);
  }

  /**
   * Get the entries.
   * @return a list of entries.
   */
  public List<FilteredTrophyTableEntry> getEntries()
  {
    return _entries;
  }

  /**
   * Dump contents.
   * @param sb Output.
   * @param level Indentation level.
   */
  public void dump(StringBuilder sb, int level)
  {
    for(int i=0;i<level;i++) sb.append('\t');
    sb.append("Filtered trophy table ID=").append(_id).append(EndOfLine.NATIVE_EOL);
    for(FilteredTrophyTableEntry entry : _entries)
    {
      entry.dump(sb,level+1);
    }
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    dump(sb,0);
    return sb.toString().trim();
  }
}
