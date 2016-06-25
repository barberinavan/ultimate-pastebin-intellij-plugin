package com.github.kennedyoliveira.ultimatepastebin.settings;

import com.github.kennedyoliveira.pastebin4j.AccountCredentials;
import com.github.kennedyoliveira.pastebin4j.PasteBin;
import com.github.kennedyoliveira.ultimatepastebin.UltimatePasteBinConstants;
import com.github.kennedyoliveira.ultimatepastebin.i18n.MessageBundle;
import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

import static com.github.kennedyoliveira.ultimatepastebin.utils.UltimatePasteBinUtils.showErrorMessageBox;
import static com.github.kennedyoliveira.ultimatepastebin.utils.UltimatePasteBinUtils.showInfoMessageBox;

/**
 * Created by kennedy on 11/7/15.
 */
public class PasteBinConfigurationForm extends JPanel {

  private JTextField userName;
  private JTextField devkey;
  private JPasswordField password;
  private JPanel accountCredentialsPanel;
  private JButton checkAccountCredentialsButton;
  private JButton getDevKeyButton;
  private JPanel contentPanel;
  private JPanel generalSettingsPanel;
  private JComboBox language;
  private JFormattedTextField pasteToFetch;
  private JButton donateToHelpButton;
  private JButton helpTranslatingPluginButton;
  private JButton reportButton;
  private PasteBinConfigurationService configurationService;

  public PasteBinConfigurationForm(boolean canBeParent) {
    super(canBeParent);

    createListeners();

    this.configurationService = ServiceManager.getService(PasteBinConfigurationService.class);

    userName.setText(configurationService.getUsername());
    devkey.setText(configurationService.getDevkey());

    pasteToFetch.setValue(PasteBinConfigurationServiceImpl.getInstance().getTotalPastesToFetch());

    // Add all the languages aailable to the combobox
    MessageBundle.getAvailableLanguages().forEach(language::addItem);

    String currentLanguage = configurationService.getCurrentLanguage();

    // If there are some language, sets it
    if (currentLanguage != null) {
      language.setSelectedItem(currentLanguage);
    } else {
      language.setSelectedItem("English");
    }
  }

  /**
   * Adds the listeners to the components
   */
  public void createListeners() {
    getDevKeyButton.addActionListener(e -> BrowserUtil.browse("http://pastebin.com/api#1"));

    checkAccountCredentialsButton.addActionListener(e -> checkCredentialsAccount());

    donateToHelpButton.addActionListener(e -> BrowserUtil.browse(UltimatePasteBinConstants.DONATION_URL));

    helpTranslatingPluginButton.addActionListener(e -> BrowserUtil.browse(UltimatePasteBinConstants.TRANSLATION_CONTRIBUTION_URL));

    reportButton.addActionListener(e -> BrowserUtil.browse(UltimatePasteBinConstants.ISSUE_URL));
  }

  /**
   * Checks if the inserted credentials account is valid
   */
  public void checkCredentialsAccount() {
    String username = userName.getText();
    char[] password = this.password.getPassword();
    String devKey = devkey.getText();

    // Validation
    if (username == null || username.isEmpty()) {
      Balloon titulo = createErrorBalloon(MessageBundle.getMessage("ultimatepastebin.form.validation.required"),
                                          MessageBundle.getMessage("ultimatepastebin.form.validation.username.null"));

      titulo.show(new RelativePoint(userName, new Point(userName.getSize().width / 2, userName.getSize().height / 2)), Balloon.Position.below);

      userName.requestFocus();
      return;
    }

    if (password == null || password.length == 0) {
      Balloon errorBalloon = createErrorBalloon(MessageBundle.getMessage("ultimatepastebin.form.validation.required"),
                                                MessageBundle.getMessage("ultimatepastebin.form.validation.password.null"));

      errorBalloon.show(new RelativePoint(this.password, new Point(this.password.getSize().width / 2, this.password.getSize().height / 2)), Balloon.Position.below);

      this.password.requestFocus();
      return;
    }

    if (devKey == null || devKey.isEmpty()) {
      Balloon errorBalloon = createErrorBalloon(MessageBundle.getMessage("ultimatepastebin.form.validation.required"),
                                                MessageBundle.getMessage("ultimatepastebin.form.validation.devkey.null"));

      errorBalloon.show(new RelativePoint(this.devkey, new Point(this.devkey.getSize().width / 2, this.devkey.getSize().height / 2)), Balloon.Position.below);

      this.devkey.requestFocus();
      return;
    }

    // Run the check in background to keep responsive
    new Task.Backgroundable(null, MessageBundle.getMessage("ultimatepastebin.settings.login.validation.title"), false, PerformInBackgroundOption.DEAF) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        try {
          // Manually creates a pastebin passing the credentials so it doesn't alter the configuration
          new PasteBin(new AccountCredentials(devKey, username, new String(password))).fetchUserInformation();

          String okTitle = MessageBundle.getMessage("ultimatepastebin.settings.login.ok.title");
          String okMessage = MessageBundle.getMessage("ultimatepastebin.settings.login.ok.message");

          showInfoMessageBox(null, okMessage, okTitle);
        } catch (Exception e) {
          String errorTitle = MessageBundle.getMessage("ultimatepastebin.settings.login.error.title");

          showErrorMessageBox(null, e.getMessage() != null ? e.getMessage() : "Unknow", errorTitle);
        }
      }
    }.queue();
  }

  /**
   * Generates an Error Balloon
   *
   * @param title   Title
   * @param message Message for the baloon, will be inserted in a JLabel
   * @return The balloon ready to be displayed
   */
  @NotNull
  public Balloon createErrorBalloon(String title, String message) {
    return JBPopupFactory.getInstance()
                         .createBalloonBuilder(new JLabel(message))
                         .setTitle(title)
                         .setFadeoutTime(5000L)
                         .setShadow(true)
                         .setFillColor(HintUtil.ERROR_COLOR)
                         .createBalloon();
  }

  public JComboBox getLanguage() {
    return language;
  }

  @Nullable
  protected JComponent createCenterPanel() {
    return contentPanel;
  }

  public JTextField getUserName() {
    return userName;
  }

  public JTextField getDevkey() {
    return devkey;
  }

  public JPasswordField getPassword() {
    return password;
  }

  public JFormattedTextField getPasteToFetch() {
    return pasteToFetch;
  }

  /**
   * Custom initialization code from form
   */
  private void createUIComponents() {
    NumberFormat numberFormat = NumberFormat.getInstance();
    NumberFormatter formatter = new NumberFormatter(numberFormat);

    formatter.setValueClass(Integer.class);
    formatter.setMaximum(1000);
    formatter.setMinimum(1);
    formatter.setCommitsOnValidEdit(true);
    formatter.setOverwriteMode(true);

    pasteToFetch = new JFormattedTextField(formatter);
    pasteToFetch.setValue(50);
  }

}
