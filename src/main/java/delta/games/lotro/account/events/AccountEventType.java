package delta.games.lotro.account.events;

/**
 * Type of account events.
 * @author DAM
 */
public enum AccountEventType
{
  /**
   * A new account was added.
   */
  ACCOUNT_ADDED,
  /**
   * An account summary was updated.
   */
  ACCOUNT_SUMMARY_UPDATED,
  /**
   * An account was removed.
   */
  ACCOUNT_REMOVED,
  /**
   * Storage update.
   */
  STORAGE_UPDATED
}
