package org.ifml.ide.ui.properties;

import org.ifml.eclipse.emf.ui.properties.EmfPropertyConfigurationSet;
import org.ifml.ide.ui.viewers.IfmlViewers;

final class IfmlPropertyConfigurations {

    static final EmfPropertyConfigurationSet CONFIG_SET;

    static {
        CONFIG_SET = new EmfPropertyConfigurationSet.Builder().featureLabelProvider(IfmlViewers.LABEL_PROVIDER)
                .configuration(new InteractionFlowElementPropertyConfiguration())
                .configuration(new ViewComponentPartPropertyConfiguration()).configuration(new ViewElementPropertyConfiguration())
                .build();
    }

}
