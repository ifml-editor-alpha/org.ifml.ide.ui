package org.ifml.ide.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The IDE runtime class containing the core (UI-free) support for IFML.
 */
public class IfmlIdeUi extends AbstractUIPlugin {

    /** The identifier of this plug-in. */
    public static final String ID = IfmlIdeUi.class.getPackage().getName();

    private static IfmlIdeUi plugin;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     * 
     * @return the shared instance.
     */
    public static IfmlIdeUi getDefault() {
        return plugin;
    }

}
