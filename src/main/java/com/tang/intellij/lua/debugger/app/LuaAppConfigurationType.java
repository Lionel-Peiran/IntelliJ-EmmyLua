

package com.tang.intellij.lua.debugger.app;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.tang.intellij.lua.lang.LuaIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 *
 * Created by tangzx on 2017/5/7.
 */
public class LuaAppConfigurationType implements ConfigurationType {

    private LuaAppConfigurationFactory factory = new LuaAppConfigurationFactory(this);

    @Override
    public String getDisplayName() {
        return "Lua Application";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Lua Application Runner";
    }

    @Override
    public Icon getIcon() {
        return LuaIcons.FILE;
    }

    @NotNull
    @Override
    public String getId() {
        return "lua.app";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] { factory };
    }

    @NotNull
    public static LuaAppConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(LuaAppConfigurationType.class);
    }
}
