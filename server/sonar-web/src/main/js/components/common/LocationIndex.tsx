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
import classNames from 'classnames';
import * as React from 'react';
import './LocationIndex.css';

interface Props {
  children?: React.ReactNode;
  leading?: boolean;
  onClick?: () => void;
  selected?: boolean;
  [x: string]: any;
}

export default function LocationIndex(props: Props) {
  const { children, leading, onClick, selected, ...other } = props;
  const clickAttributes = onClick ? { onClick, role: 'button', tabIndex: 0 } : {};
  // put {...others} because Tooltip sets some event handlers
  return (
    <div
      className={classNames('location-index', {
        'is-leading': leading,
        selected
      })}
      {...clickAttributes}
      {...other}>
      {children}
    </div>
  );
}
