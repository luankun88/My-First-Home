/*
 * SonarQube
 * Copyright (C) 2009-2021 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import * as React from 'react';
import { Button } from '../../components/controls/buttons';
import { Alert } from '../../components/ui/Alert';
import { translate } from '../../helpers/l10n';
import { Application } from '../../types/application';
import ApplicationProjectBranch from './ApplicationProjectBranch';
import CreateBranchForm from './CreateBranchForm';
import { ApplicationBranch } from './utils';

interface Props {
  application: Application;
  canBrowseAllChildProjects: boolean;
  onUpdateBranches: (branches: ApplicationBranch[]) => void;
}

interface State {
  creating: boolean;
}

export default class ApplicationBranches extends React.PureComponent<Props, State> {
  state: State = { creating: false };

  handleCreate = (branch: ApplicationBranch) => {
    this.props.onUpdateBranches([...this.props.application.branches, branch]);
  };

  handleCreateFormClose = () => {
    this.setState({ creating: false });
  };

  handleCreateClick = () => {
    this.setState({ creating: true });
  };

  canCreateBranches = () => {
    return (
      this.props.application.projects &&
      this.props.application.projects.some(p => Boolean(p.enabled))
    );
  };

  renderBranches(createEnable: boolean, readonly: boolean) {
    const { application } = this.props;
    if (!createEnable) {
      return (
        <div className="app-branches-list">
          <p className="text-center big-spacer-top">
            {translate('application_console.branches.no_branches')}
          </p>
        </div>
      );
    }
    return (
      <div className="app-branches-list">
        <table className="data zebra">
          <tbody>
            {application.branches.map(branch => (
              <ApplicationProjectBranch
                application={application}
                branch={branch}
                key={branch.name}
                onUpdateBranches={this.props.onUpdateBranches}
                readonly={readonly}
              />
            ))}
          </tbody>
        </table>
      </div>
    );
  }

  render() {
    const { application, canBrowseAllChildProjects } = this.props;
    const createEnable = this.canCreateBranches();
    const readonly = !canBrowseAllChildProjects;
    return (
      <div className="app-branches-console">
        {readonly && (
          <Alert className="huge-spacer-top" variant="warning">
            {translate('application_console.branches.cannot_access_all_child_projects')}
          </Alert>
        )}

        <div className="boxed-group-actions">
          <Button disabled={!createEnable || readonly} onClick={this.handleCreateClick}>
            {translate('application_console.branches.create')}
          </Button>
        </div>
        <h2
          className="text-limited big-spacer-top"
          title={translate('application_console.branches')}>
          {translate('application_console.branches')}
        </h2>
        <p>{translate('application_console.branches.help')}</p>

        {this.renderBranches(createEnable, readonly)}

        {this.state.creating && (
          <CreateBranchForm
            application={application}
            enabledProjectsKey={application.projects.map(p => p.key)}
            onClose={this.handleCreateFormClose}
            onCreate={this.handleCreate}
          />
        )}
      </div>
    );
  }
}
