package com.github.kennedyoliveira.ultimatepastebin;

/**
 * Created by kennedy on 11/9/15.
 */
public class UltimatePasteBinConstants {

  /**
   * Plugin Version
   */
  public final static String VERSION = "1.3.1";
  /**
   * URL For donating
   */
  public final static String DONATION_URL = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=CR4K3FDKKK5FA&lc=GB&item_name=Kennedy%20Oliveira&item_number=ultimate%2dpastebin&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted";
  /**
   * Link with the documentation for the translations
   */
  public final static String TRANSLATION_CONTRIBUTION_URL = "https://github.com/kennedyoliveira/ultimate-pastebin-intellij-plugin/blob/master/TRANSLATION-CONTRIB.md";
  /**
   * Link for reporting issues or suggestions
   */
  public final static String ISSUE_URL = "https://github.com/kennedyoliveira/ultimate-pastebin-intellij-plugin/issues";
  /**
   * URL For the project site.
   */
  public final static String PROJECT_URL = "https://kennedyoliveira.github.io/ultimate-pastebin-intellij-plugin";
  /**
   * Default total of user pastes to fetch
   */
  public final static int DEFAULT_TOTAL_PASTES_TO_FETCH = 50;
  /**
   * <p>Maximum user pastes that can be fetched, this value is provided be the API.</p>
   * <p>The currently public api supports a maximum of 1000 pastes.</p>
   */
  public final static int MAX_PASTES_TO_FETCH = 1000;

  /**
   * @deprecated Constants only class
   */
  private UltimatePasteBinConstants() {
  }
}
