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
package org.sonar.server.platform.db.migration.version.v93;

import java.sql.SQLException;
import org.sonar.db.Database;
import org.sonar.db.DatabaseUtils;
import org.sonar.server.platform.db.migration.sql.CreateTableBuilder;
import org.sonar.server.platform.db.migration.step.DdlChange;

import static org.sonar.server.platform.db.migration.def.BigIntegerColumnDef.newBigIntegerColumnDefBuilder;
import static org.sonar.server.platform.db.migration.def.VarcharColumnDef.UUID_SIZE;
import static org.sonar.server.platform.db.migration.def.VarcharColumnDef.newVarcharColumnDefBuilder;

public class CreateNewCodeReferenceBranchIssues extends DdlChange {

  private static final String TABLE_NAME = "new_code_reference_issues";

  public CreateNewCodeReferenceBranchIssues(Database db) {
    super(db);
  }

  @Override
  public void execute(Context context) throws SQLException {
    if (tableExists()) {
      return;
    }

    context.execute(new CreateTableBuilder(getDialect(), TABLE_NAME)
      .addPkColumn(newVarcharColumnDefBuilder().setColumnName("uuid").setIsNullable(false).setLimit(UUID_SIZE).build())
      .addColumn(newVarcharColumnDefBuilder().setColumnName("issue_key").setIsNullable(false).setLimit(50).build())
      .addColumn(newBigIntegerColumnDefBuilder().setColumnName("created_at").setIsNullable(false).build())
      .build());
  }

  private boolean tableExists() throws SQLException {
    try (var connection = getDatabase().getDataSource().getConnection()) {
      return DatabaseUtils.tableExists(TABLE_NAME, connection);
    }
  }
}
