package org.ifml.ide.ui.operations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.ifml.eclipse.core.runtime.Statuses;
import org.ifml.eclipse.graphiti.diagrams.Diagrams;
import org.ifml.eclipse.osgi.Bundles;
import org.ifml.ide.ui.diagrams.IfmlDiagrams;

import com.google.common.collect.ImmutableList;

/**
 * A workspace operation which creates a new IFML model.
 */
public final class CreateIfmlModelOperation extends WorkspaceModifyOperation {

    private final IContainer container;

    private final String name;

    private IFile ifmlFile;

    /**
     * Constructs a new operation.
     * 
     * @param container
     *            the parent container.
     * @param name
     *            the model name.
     * 
     */
    public CreateIfmlModelOperation(IContainer container, String name) {
        this.container = container;
        this.name = name;
    }

    @Override
    protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        try {
            doExecute(monitor);
        } catch (IOException e) {
            throw new CoreException(Statuses.getErrorStatus(e, null, Bundles.getDeclaringBundle(getClass())));
        }
    }

    private void doExecute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException,
            IOException {
        monitor.beginTask("Creating IFML model", 100);
        ifmlFile = container.getFile(new Path(name));
        Diagram interactionFlowModelDiagram = Graphiti.getPeCreateService().createDiagram(
                IfmlDiagrams.INTERACTION_FLOW_MODEL_DIAGRAM_TYPE_ID, "Interaction Flow", true);
        Diagram contentModelDiagram = Graphiti.getPeCreateService().createDiagram(IfmlDiagrams.CONTENT_MODEL_DIAGRAM_TYPE_ID,
                "Content", true);
        URI uri = URI.createPlatformResourceURI(ifmlFile.getFullPath().toString(), true);
        Diagrams.createEmfFileForDiagram(uri, ImmutableList.of(interactionFlowModelDiagram, contentModelDiagram),
                new SubProgressMonitor(monitor, 50));
    }

    /**
     * Returns the IFML file.
     * 
     * @return the IFML file.
     */
    public IFile getIfmlFile() {
        return ifmlFile;
    }

}
