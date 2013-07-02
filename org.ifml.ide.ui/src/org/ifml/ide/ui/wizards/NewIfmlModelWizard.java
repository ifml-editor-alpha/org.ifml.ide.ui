package org.ifml.ide.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.ifml.eclipse.core.runtime.Logs;
import org.ifml.eclipse.osgi.Bundles;
import org.ifml.eclipse.ui.Workbenches;
import org.ifml.eclipse.ui.dialogs.ErrorDialogBuilder;
import org.ifml.eclipse.ui.widgets.Displays;
import org.ifml.eclipse.ui.wizards.CommonWizard;
import org.ifml.eclipse.ui.wizards.WizardConfiguration;
import org.ifml.ide.ui.IfmlIdeUi;
import org.ifml.ide.ui.operations.CreateIfmlModelOperation;

/**
 * The wizard able to create a new IFML model.
 */
public final class NewIfmlModelWizard extends CommonWizard implements IWorkbenchWizard {

    private IStructuredSelection selection;

    private NewIfmlModelWizardPage wizardPage;

    /**
     * Constructs a new wizard.
     */
    public NewIfmlModelWizard() {
        super(new WizardConfiguration.Builder().windowTitle("New IFML Model").needsProgressMonitor(true).build());
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

    @Override
    public void addPages() {
        wizardPage = new NewIfmlModelWizardPage(selection);
        addPage(wizardPage);
    }

    @Override
    public boolean performFinish() {
        CreateIfmlModelOperation op = new CreateIfmlModelOperation(wizardPage.getWorkspaceContainer(), wizardPage.getModelName());
        try {
            getContainer().run(true, true, op);
        } catch (InterruptedException e) {
            return false;
        } catch (final InvocationTargetException e) {
            Logs.logError(e.getCause(), null, Bundles.getDeclaringBundle(getClass()));
            Displays.getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    new ErrorDialogBuilder().shell(Workbenches.getActiveShell()).title("Error")
                            .message("An error occurred creating the IFML model").exception(e.getCause()).build().open();
                }
            });
            return false;
        }
        IWorkbenchPage page = Workbenches.getActivePage();
        if (page != null) {
            try {
                IDE.openEditor(page, op.getIfmlFile(), true);
                if (page.getActivePart() instanceof ISetSelectionTarget) {
                    ISetSelectionTarget selectionTarget = (ISetSelectionTarget) page.getActivePart();
                    selectionTarget.selectReveal(new StructuredSelection(op.getIfmlFile()));
                }
            } catch (PartInitException e) {
                Logs.logError(e, "Unable to open editor", IfmlIdeUi.getDefault().getBundle());
            }
        }
        return true;

    }

}
