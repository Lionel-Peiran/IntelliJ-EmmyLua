

package com.tang.intellij.lua.debugger.remote;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.tang.intellij.lua.lang.LuaIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 *
 * Created by TangZX on 2016/12/30.
 */
public class LuaMobConfigurationType implements ConfigurationType {

    private final LuaMobConfigurationFactory factory = new LuaMobConfigurationFactory(this);

    public static LuaMobConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(LuaMobConfigurationType.class);
    }

    @Override
    public String getDisplayName() {
        return "Lua Remote(Mobdebug)";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Lua Remote Debugger";
    }

    @Override
    public Icon getIcon() {
        return LuaIcons.FILE;
    }

    @NotNull
    @Override
    public String getId() {
        return "lua.mobdebug";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] { factory };
    }
}
