package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.ViewElementEvent;

class ViewElementEventPattern<T extends ViewElementEvent> extends EventPattern<T> {

    public static ViewElementEventPattern<ViewElementEvent> newInstance() {
        return new ViewElementEventPattern<ViewElementEvent>(ViewElementEvent.class);
    }

    public ViewElementEventPattern(Class<T> instanceClass) {
        super(instanceClass);
    }

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.VIEW_ELEMENT_EVENT;
    }

}
