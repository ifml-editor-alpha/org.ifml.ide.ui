package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.core.ViewContainer;
import org.ifml.model.extensions.ExtensionsPackage;
import org.ifml.model.extensions.Window;

final class WindowPattern extends ViewContainerPattern<Window> {

    public WindowPattern() {
        super(Window.class);
    }

    @Override
    public EClass getEClass() {
        return ExtensionsPackage.Literals.WINDOW;
    }

    @Override
    protected String getModifiers(ViewContainer context) {
        StringBuilder modifiers = new StringBuilder();
        Window windowContext = (Window) context;

        if (windowContext.isIsNew() && windowContext.isIsModal()) {
            modifiers.append("[Modal]");
        } else if (windowContext.isIsNew() && !windowContext.isIsModal()) {
            modifiers.append("[Modeless]");
        }
        if (windowContext.isIsXor()) {
            modifiers.append("[XOR]");
        }
        if (windowContext.isIsLandMark()) {
            modifiers.append("[L]");
        }
        if (windowContext.isIsDefault()) {
            modifiers.append("[D]");
        }

        return modifiers.toString();
    }
}
