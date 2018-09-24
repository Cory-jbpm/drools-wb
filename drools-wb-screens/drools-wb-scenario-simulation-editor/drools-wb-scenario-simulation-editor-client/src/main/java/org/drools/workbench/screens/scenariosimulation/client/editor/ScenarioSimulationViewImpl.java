/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.scenariosimulation.client.editor;

import java.util.Set;

import javax.enterprise.context.Dependent;

import com.google.gwt.event.shared.HandlerRegistration;
import org.drools.workbench.screens.scenariosimulation.client.resources.i18n.ScenarioSimulationEditorConstants;
import org.drools.workbench.screens.scenariosimulation.client.widgets.ScenarioGridLayer;
import org.drools.workbench.screens.scenariosimulation.client.widgets.ScenarioGridPanel;
import org.drools.workbench.screens.scenariosimulation.model.Simulation;
import org.kie.workbench.common.widgets.metadata.client.KieEditorViewImpl;
import org.uberfire.workbench.model.menu.MenuItem;

/**
 * Implementation of the main view for the ScenarioSimulation editor.
 * <p>
 * This view contains a <code>ScenarioGridPanel</code>.
 */
@Dependent
public class ScenarioSimulationViewImpl
        extends KieEditorViewImpl
        implements ScenarioSimulationView {

    private ScenarioGridPanel scenarioGridPanel;

    private ScenarioSimulationEditorPresenter presenter;

    private ScenarioGridLayer scenarioGridLayer;

    private Set<HandlerRegistration> handlerRegistrations;

    /**
     * This method also set <code>ScenarioGridLayer</code> taken the instance from given <code>ScenarioGridPanel</code>
     * @param scenarioGridPanel
     */
    @Override
    public void setScenarioGridPanel(ScenarioGridPanel scenarioGridPanel) {
        this.scenarioGridPanel = scenarioGridPanel;
        this.scenarioGridLayer = scenarioGridPanel.getScenarioGridLayer();
    }

    @Override
    public void setHandlerRegistrations(Set<HandlerRegistration> handlerRegistrations) {
        this.handlerRegistrations = handlerRegistrations;
    }

    @Override
    public void init(final ScenarioSimulationEditorPresenter presenter) {
        this.presenter = presenter;
        scenarioGridLayer.enterPinnedMode(scenarioGridLayer.getScenarioGrid(), () -> {
        });  // Hack to overcome default implementation
        initWidget(scenarioGridPanel);
    }

    @Override
    public void setContent(Simulation simulation) {
        scenarioGridPanel.getScenarioGrid().setContent(simulation);
    }

    @Override
    public void clear() {
        if (handlerRegistrations != null) {
            handlerRegistrations.forEach(HandlerRegistration::removeHandler);
        }
    }

    @Override
    public MenuItem getRunScenarioMenuItem() {
        return new RunScenarioMenuItem(ScenarioSimulationEditorConstants.INSTANCE.runScenarioSimulation(),
                                       () -> presenter.onRunScenario());
    }

    @Override
    public ScenarioGridPanel getScenarioGridPanel() {
        return scenarioGridPanel;
    }

    @Override
    public ScenarioGridLayer getScenarioGridLayer() {
        return scenarioGridLayer;
    }
}