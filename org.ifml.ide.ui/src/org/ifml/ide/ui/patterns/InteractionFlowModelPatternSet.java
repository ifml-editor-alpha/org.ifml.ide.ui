package org.ifml.ide.ui.patterns;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.pattern.IConnectionPattern;
import org.eclipse.graphiti.pattern.IPattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * The full collection of Graphiti patterns for the interaction flow model.
 */
public final class InteractionFlowModelPatternSet {

    private static final Collection<IPattern> SHAPE_PATTERNS;

    private static final Collection<IConnectionPattern> CONNECTION_PATTERNS;

    static {
        SHAPE_PATTERNS = ImmutableList.copyOf(createShapePatternList());
        CONNECTION_PATTERNS = ImmutableList.copyOf(createConnectionPatternList());
    }

    private InteractionFlowModelPatternSet() {
    }

    /**
     * Returns the collection of shape-based patterns.
     * 
     * @return the collection of shape-based patterns.
     */
    public static Collection<IPattern> getShapePatterns() {
        return SHAPE_PATTERNS;
    }

    /**
     * Returns the collection of connection-based patterns.
     * 
     * @return the collection of connection-based patterns.
     */
    public static Collection<IConnectionPattern> getConnectionPatterns() {
        return CONNECTION_PATTERNS;
    }

    private static List<IPattern> createShapePatternList() {
        List<IPattern> shapePatterns = Lists.newArrayList();
        shapePatterns.add(new ActionPattern());
        shapePatterns.add(new ActionEventPattern());
        shapePatterns.add(new ActivationExpressionPattern());
        shapePatterns.add(new AnnotationPattern());
        shapePatterns.add(new ConditionalExpressionPattern());
        shapePatterns.add(new DataBindingPattern());
        shapePatterns.add(new DetailsPattern());
        shapePatterns.add(new FormPattern());
        shapePatterns.add(new ListPattern());
        shapePatterns.add(new ParameterBindingPattern());
        shapePatterns.add(new ParameterBindingGroupPattern());
        shapePatterns.add(new SelectEventPattern());
        shapePatterns.add(new SelectionFieldPattern());
        shapePatterns.add(new SimpleFieldPattern());
        shapePatterns.add(new SubmitEventPattern());
        shapePatterns.add(new SystemEventPattern());
        shapePatterns.add(ViewContainerPattern.newInstance());
        shapePatterns.add(new ViewComponentPattern());
        shapePatterns.add(new ViewComponentPartPattern());
        shapePatterns.add(ViewElementEventPattern.newInstance());
        shapePatterns.add(new WindowPattern());
        return shapePatterns;
    }

    private static List<IConnectionPattern> createConnectionPatternList() {
        List<IConnectionPattern> connPatterns = Lists.newArrayList();
        connPatterns.add(new DataFlowPattern());
        connPatterns.add(new NavigationFlowPattern());
        return connPatterns;
    }

    /**
     * Returns the {@link EClass} the pattern is associated with.
     * 
     * @param pattern
     *            the pattern.
     * @return the {@link EClass}.
     * @throws IllegalArgumentException
     *             if {@code pattern} is not an {@link AbstractIfmlShapePattern}.
     */
    public static EClass getEClass(IPattern pattern) {
        Preconditions.checkArgument(pattern instanceof AbstractIfmlShapePattern);
        return ((AbstractIfmlShapePattern<?>) pattern).getEClass();
    }

}
