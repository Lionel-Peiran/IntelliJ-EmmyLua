

package com.tang.intellij.lua.project;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CompletionSettingsPanel implements SearchableConfigurable, Configurable.NoScroll {

    private JPanel panel;

    @NotNull
    @Override
    public String getId() {
        return "lua.completion";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Emmy Completion";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
