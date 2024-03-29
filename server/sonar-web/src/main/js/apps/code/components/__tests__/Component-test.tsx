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
import { shallow } from 'enzyme';
import * as React from 'react';
import { mockComponentMeasure } from '../../../../helpers/mocks/component';
import { mockMetric } from '../../../../helpers/testMocks';
import { Component } from '../Component';

it('should render correctly', () => {
  expect(shallowRender()).toMatchSnapshot();
  expect(shallowRender({ hasBaseComponent: true })).toMatchSnapshot('with base component');
  expect(shallowRender({ isBaseComponent: true })).toMatchSnapshot('is base component');
});

it('should render correctly for a file', () => {
  expect(shallowRender({ component: mockComponentMeasure(true) })).toMatchSnapshot();
});

function shallowRender(props: Partial<Component['props']> = {}) {
  return shallow(
    <Component
      component={mockComponentMeasure(false, {
        key: 'bar',
        name: 'Bar',
        measures: [
          { metric: 'bugs', value: '12' },
          { metric: 'vulnerabilities', value: '1' }
        ]
      })}
      hasBaseComponent={false}
      metrics={[mockMetric({ key: 'bugs' }), mockMetric({ key: 'vulnerabilities' })]}
      rootComponent={mockComponentMeasure()}
      {...props}
    />
  );
}
