package delta.games.lotro.lore.trade.barter;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.lore.agents.npcs.NpcDescription;

/**
 * Barter data for a NPC.
 * @author DAM
 */
public class BarterNpc implements Identifiable
{
  // Parent NPC
  private NpcDescription _npc;
  // Requirements
  private UsageRequirement _requirements;
  // Profile(s)
  private List<BarterProfile> _profiles;

  /**
   * Constructor.
   * @param npc Associated NPC.
   */
  public BarterNpc(NpcDescription npc)
  {
    _npc=npc;
    _requirements=new UsageRequirement();
    _profiles=new ArrayList<BarterProfile>();
  }

  /**
   * Get the identifier of the parent NPC.
   * @return a NPC identifier.
   */
  public int getIdentifier()
  {
    return _npc.getIdentifier();
  }

  /**
   * Get the associated NPC.
   * @return a NPC.
   */
  public NpcDescription getNpc()
  {
    return _npc;
  }

  /**
   * Get the usage requirements for this barterer.
   * @return some requirements.
   */
  public UsageRequirement getRequirements()
  {
    return _requirements;
  }

  /**
   * Add a barter profile.
   * @param profile Profile to add.
   */
  public void addBarterProfile(BarterProfile profile)
  {
    _profiles.add(profile);
  }

  /**
   * Get the barter profiles.
   * @return a list of barter profiles.
   */
  public List<BarterProfile> getBarterProfiles()
  {
    return _profiles;
  }

  /**
   * Get the barter entries for this barterer.
   * @return a list of barter entries.
   */
  public List<BarterEntry> getEntries()
  {
    List<BarterEntry> ret=new ArrayList<BarterEntry>();
    for(BarterProfile list : _profiles)
    {
      ret.addAll(list.getEntries());
    }
    return ret;
  }

  /**
   * Dump the contents of this NPC barter data as a readable string.
   * @return A displayable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append(_npc);
    sb.append(EndOfLine.NATIVE_EOL);
    for(BarterProfile profile : _profiles)
    {
      sb.append("\tProfile: ").append(profile.getName()).append(EndOfLine.NATIVE_EOL);
      for(BarterEntry entry : profile.getEntries())
      {
        sb.append("\t\t").append(entry).append(EndOfLine.NATIVE_EOL);
      }
    }
    return sb.toString().trim();
  }
}
