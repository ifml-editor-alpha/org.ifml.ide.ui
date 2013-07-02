package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.SystemEvent;

final class SystemEventPattern extends EventPattern<SystemEvent> {

    public SystemEventPattern() {
        super(SystemEvent.class);
    }

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.SYSTEM_EVENT;
    }

}
