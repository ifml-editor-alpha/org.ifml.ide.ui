package org.ifml.ide.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.ui.handlers.HandlerUtil;
import org.ifml.ide.ui.diagrams.IfmlDiagrams;
import org.ifml.ide.ui.editors.IfmlModelEditor;
import org.ifml.model.IfmlModels;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.IFMLModel;
import org.ifml.model.core.ViewPoint;

import com.google.common.collect.Iterables;

/**
 * An handler for the creation of a new view point.
 */
public final class NewViewPointHandler extends AbstractHandler implements IHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IfmlModelEditor editor = (IfmlModelEditor) HandlerUtil.getActiveEditor(event);
        final TransactionalEditingDomain editingDomain = editor.getEditingDomain();
        CommandStack commandStack = editingDomain.getCommandStack();
        commandStack.execute(new RecordingCommand(editingDomain) {

            @Override
            protected void doExecute() {
                ResourceSet resourceSet = editingDomain.getResourceSet();
                Resource resource = resourceSet.getResources().get(0);

                /* creates the diagram */
                Diagram diagram = Graphiti.getPeCreateService().createDiagram(IfmlDiagrams.VIEWPOINT_DIAGRAM_TYPE_ID, "ViewPoint",
                        true);
                EList<EObject> resContents = resource.getContents();
                resource.getContents().add(Iterables.size(Iterables.filter(resContents, Diagram.class)), diagram);

                /* creates the viewpoint */
                IFMLModel ifmlModel = IfmlModels.getIfmlModel(resource).get();
                ViewPoint viewpoint = (ViewPoint) EcoreUtil.create(CorePackage.Literals.VIEW_POINT);
                ifmlModel.getInteractionFlowModelViewPoints().add(viewpoint);

                /* links the viewpoint to the diagram */
                PictogramLink link = PictogramsFactory.eINSTANCE.createPictogramLink();
                link.getBusinessObjects().add(viewpoint);
                diagram.setLink(link);
            }
        });
        return null;
    }

}
