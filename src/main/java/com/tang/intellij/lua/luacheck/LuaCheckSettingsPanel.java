

package com.tang.intellij.lua.luacheck;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.HoverHyperlinkLabel;
import com.intellij.ui.HyperlinkAdapter;
import com.intellij.ui.RawCommandLineEditor;
import com.tang.intellij.lua.LuaBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

/**
 * lua check settings panel
 * Created by Administrator on 2017/7/13.
 */
public class LuaCheckSettingsPanel implements SearchableConfigurable, Configurable.NoScroll {
    private JPanel myPanel;
    private RawCommandLineEditor myCmdLine;
    private TextFieldWithBrowseButton myLuaCheck;
    private HoverHyperlinkLabel luaCheckReleasePageLink;
    private HoverHyperlinkLabel commandLineOptionsLink;
    private LuaCheckSettings settings = LuaCheckSettings.getInstance();

    public LuaCheckSettingsPanel() {
        myLuaCheck.setText(settings.getLuaCheck());
        myCmdLine.setText(settings.getLuaCheckArgs());
    }

    @NotNull
    @Override
    public String getId() {
        return "luacheck";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "LuaCheck";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return myPanel;
    }

    @Override
    public boolean isModified() {
        return !StringUtil.equals(settings.getLuaCheck(), myLuaCheck.getText()) ||
                !StringUtil.equals(settings.getLuaCheckArgs(), myCmdLine.getText());
    }

    @Override
    public void apply() {
        settings.setLuaCheck(myLuaCheck.getText());
        settings.setLuaCheckArgs(myCmdLine.getText());
    }

    private void createUIComponents() {
        luaCheckReleasePageLink = new HoverHyperlinkLabel(LuaBundle.message("ui.luacheck.download"));
        luaCheckReleasePageLink.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            protected void hyperlinkActivated(HyperlinkEvent hyperlinkEvent) {
                BrowserUtil.browse("https://github.com/mpeterv/luacheck/releases");
            }
        });
        commandLineOptionsLink = new HoverHyperlinkLabel(LuaBundle.message("ui.luacheck.command_line_options"));
        commandLineOptionsLink.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            protected void hyperlinkActivated(HyperlinkEvent hyperlinkEvent) {
                BrowserUtil.browse("http://luacheck.readthedocs.io/en/stable/cli.html#command-line-options");
            }
        });
    }
}
