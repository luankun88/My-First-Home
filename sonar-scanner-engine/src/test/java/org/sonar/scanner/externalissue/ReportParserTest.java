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
package org.sonar.scanner.externalissue;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import org.sonar.scanner.externalissue.ReportParser.Report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReportParserTest {

  @Test
  public void parse_sample() {
    ReportParser parser = new ReportParser(Paths.get("src/test/resources/org/sonar/scanner/externalissue/report.json"));

    Report report = parser.parse();

    assertThat(report.issues).hasSize(4);
    assertThat(report.issues[0].engineId).isEqualTo("eslint");
    assertThat(report.issues[0].ruleId).isEqualTo("rule1");
    assertThat(report.issues[0].severity).isEqualTo("MAJOR");
    assertThat(report.issues[0].effortMinutes).isEqualTo(40);
    assertThat(report.issues[0].type).isEqualTo("CODE_SMELL");
    assertThat(report.issues[0].primaryLocation.filePath).isEqualTo("file1.js");
    assertThat(report.issues[0].primaryLocation.message).isEqualTo("fix the issue here");
    assertThat(report.issues[0].primaryLocation.textRange.startColumn).isEqualTo(2);
    assertThat(report.issues[0].primaryLocation.textRange.startLine).isOne();
    assertThat(report.issues[0].primaryLocation.textRange.endColumn).isEqualTo(4);
    assertThat(report.issues[0].primaryLocation.textRange.endLine).isEqualTo(3);
    assertThat(report.issues[0].secondaryLocations).isNull();

    assertThat(report.issues[3].engineId).isEqualTo("eslint");
    assertThat(report.issues[3].ruleId).isEqualTo("rule3");
    assertThat(report.issues[3].severity).isEqualTo("MAJOR");
    assertThat(report.issues[3].effortMinutes).isNull();
    assertThat(report.issues[3].type).isEqualTo("BUG");
    assertThat(report.issues[3].secondaryLocations).hasSize(2);
    assertThat(report.issues[3].secondaryLocations[0].filePath).isEqualTo("file1.js");
    assertThat(report.issues[3].secondaryLocations[0].message).isEqualTo("fix the bug here");
    assertThat(report.issues[3].secondaryLocations[0].textRange.startLine).isOne();
    assertThat(report.issues[3].secondaryLocations[1].filePath).isEqualTo("file2.js");
    assertThat(report.issues[3].secondaryLocations[1].message).isNull();
    assertThat(report.issues[3].secondaryLocations[1].textRange.startLine).isEqualTo(2);
  }

  private Path path(String reportName) {
    return Paths.get("src/test/resources/org/sonar/scanner/externalissue/" + reportName);
  }

  @Test
  public void fail_if_report_doesnt_exist() {
    ReportParser parser = new ReportParser(Paths.get("unknown.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Failed to read external issues report 'unknown.json'");
  }

  @Test
  public void fail_if_report_is_not_valid_json() {
    ReportParser parser = new ReportParser(path("report_invalid_json.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("invalid JSON syntax");
  }

  @Test
  public void fail_if_primaryLocation_not_set() {
    ReportParser parser = new ReportParser(path("report_missing_primaryLocation.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("missing mandatory field 'primaryLocation'");
  }

  @Test
  public void fail_if_engineId_not_set() {
    ReportParser parser = new ReportParser(path("report_missing_engineId.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("missing mandatory field 'engineId'");
  }

  @Test
  public void fail_if_ruleId_not_set() {
    ReportParser parser = new ReportParser(path("report_missing_ruleId.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("missing mandatory field 'ruleId'");
  }

  @Test
  public void fail_if_severity_not_set() {
    ReportParser parser = new ReportParser(path("report_missing_severity.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("missing mandatory field 'severity'");
  }

  @Test
  public void fail_if_type_not_set() {
    ReportParser parser = new ReportParser(path("report_missing_type.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("missing mandatory field 'type'");
  }

  @Test
  public void fail_if_filePath_not_set_in_primaryLocation() {
    ReportParser parser = new ReportParser(path("report_missing_filePath.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("missing mandatory field 'filePath'");
  }

  @Test
  public void fail_if_message_not_set_in_primaryLocation() {
    ReportParser parser = new ReportParser(path("report_missing_message.json"));

    assertThatThrownBy(() -> parser.parse())
      .isInstanceOf(IllegalStateException.class)
      .hasMessageContaining("missing mandatory field 'message'");
  }
}
