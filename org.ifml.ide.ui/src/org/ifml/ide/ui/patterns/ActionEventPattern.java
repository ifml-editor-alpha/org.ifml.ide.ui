package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.core.ActionEvent;
import org.ifml.model.core.CorePackage;

final class ActionEventPattern extends EventPattern<ActionEvent> {

    public ActionEventPattern() {
        super(ActionEvent.class);
    }

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.ACTION_EVENT;
    }

}
