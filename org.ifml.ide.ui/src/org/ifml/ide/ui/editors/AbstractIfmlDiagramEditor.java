package org.ifml.ide.ui.editors;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

abstract class AbstractIfmlDiagramEditor extends DiagramEditor {

    private TransactionalEditingDomain externalEditingDomain;

    void setEditingDomain(TransactionalEditingDomain externalEditingDomain) {
        this.externalEditingDomain = externalEditingDomain;
    }

    @Override
    protected DefaultUpdateBehavior createUpdateBehavior() {
        return new IfmlUpdateBehavior(this);
    }

    private class IfmlUpdateBehavior extends DefaultUpdateBehavior {

        public IfmlUpdateBehavior(DiagramEditor diagramEditor) {
            super(diagramEditor);
        }

        @Override
        protected void createEditingDomain() {

            /* reuses the same editing domain in all sub-editors of the multi-page editor */
            if (externalEditingDomain != null) {
                initializeEditingDomain(externalEditingDomain);
            } else {
                super.createEditingDomain();
            }
        }

        @Override
        protected void disposeEditingDomain() {

            /* no longer disposes here the shared editing domain */
            // TODO: someone must dispose it
        }

    }

}
