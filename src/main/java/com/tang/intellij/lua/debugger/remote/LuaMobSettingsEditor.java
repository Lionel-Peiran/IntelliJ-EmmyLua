

package com.tang.intellij.lua.debugger.remote;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.HoverHyperlinkLabel;
import com.intellij.ui.HyperlinkAdapter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

/**
 *
 * Created by tangzx on 2017/5/4.
 */
public class LuaMobSettingsEditor extends SettingsEditor<LuaMobConfiguration> {
    private JTextField port;
    private JPanel myPanel;
    private HoverHyperlinkLabel mobdebugLink;

    @Override
    protected void resetEditorFrom(@NotNull LuaMobConfiguration luaMobConfiguration) {
        port.setText(String.valueOf(luaMobConfiguration.getPort()));
    }

    @Override
    protected void applyEditorTo(@NotNull LuaMobConfiguration luaMobConfiguration) throws ConfigurationException {
        try {
            luaMobConfiguration.setPort(Integer.parseInt(port.getText()));
        } catch (NumberFormatException ignored) {

        }
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        port.addActionListener(e -> LuaMobSettingsEditor.this.fireEditorStateChanged());
        return myPanel;
    }

    private void createUIComponents() {
        mobdebugLink = new HoverHyperlinkLabel("Get mobdebug.lua 0.7+");
        mobdebugLink.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            protected void hyperlinkActivated(HyperlinkEvent hyperlinkEvent) {
                BrowserUtil.browse("https://github.com/pkulchenko/MobDebug/releases");
            }
        });
    }
}
