package org.ifml.ide.ui.diagrams;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.ifml.eclipse.core.runtime.Statuses;
import org.ifml.eclipse.emf.ecore.EmfPredicates;
import org.ifml.eclipse.emf.ui.editparts.EditPartEmfSelections;
import org.ifml.eclipse.graphiti.diagrams.AbstractGfEmfContentOutlinePage;
import org.ifml.eclipse.ui.actions.ContextAction;
import org.ifml.ide.ui.IfmlIdeUi;
import org.ifml.ide.ui.actions.IfmlActions;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.core.IFMLModel;
import org.ifml.model.uml.Element;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

final class IfmlContentOutlinePage extends AbstractGfEmfContentOutlinePage {

    private final List<ContextAction<Element>> actions;

    private final EClass rootModelType;

    public IfmlContentOutlinePage(IDiagramTypeProvider dtp, EClass rootModelType) {
        super(dtp);
        this.actions = IfmlActions.createContentOutlineActions(dtp.getDiagramEditor().getEditingDomain());
        this.rootModelType = rootModelType;
    }

    @Override
    protected EObject getRootlElement(IDiagramTypeProvider dtp) throws CoreException {
        IFMLModel ifmlModel = Iterators.get(Iterables.filter(dtp.getDiagram().eResource().getContents(), IFMLModel.class).iterator(),
                0, null);
        if (ifmlModel == null) {
            throw new CoreException(Statuses.getErrorStatus(null, "The diagram does not contain a IFML model element", IfmlIdeUi
                    .getDefault().getBundle()));
        }
        Optional<EObject> model = Iterables.tryFind(ifmlModel.eContents(), EmfPredicates.isInstance(rootModelType));
        if (model.isPresent()) {
            return model.get();
        } else {
            throw new CoreException(Statuses.getErrorStatus(null, "The diagram does not contain a " + rootModelType.getName(),
                    IfmlIdeUi.getDefault().getBundle()));
        }
    }

    @Override
    protected ILabelProvider getLabelProvider() {
        return IfmlViewers.LABEL_PROVIDER;
    }

    @Override
    public void dispose() {
        for (ContextAction<Element> action : actions) {
            action.setContext(null);
        }
        super.dispose();
    }

    @Override
    protected ContextMenuProvider createContextMenuProvider() {
        return new IfmlContextMenuProvider();
    }

    private final class IfmlContextMenuProvider extends ContextMenuProvider {

        public IfmlContextMenuProvider() {
            super(IfmlContentOutlinePage.this.getViewer());
        }

        @Override
        public void buildContextMenu(IMenuManager mgr) {
            ISelection sel = IfmlContentOutlinePage.this.getViewer().getSelection();
            List<Element> modelObjects = EditPartEmfSelections.getModelObjects(sel, Element.class, true);
            if (modelObjects.size() == 1) {
                Element modelObject = modelObjects.get(0);
                for (ContextAction<Element> action : actions) {
                    action.setContext(modelObject);
                    if (action.isEnabled()) {
                        mgr.add(action);
                    }
                }
            }
        }
    }

}