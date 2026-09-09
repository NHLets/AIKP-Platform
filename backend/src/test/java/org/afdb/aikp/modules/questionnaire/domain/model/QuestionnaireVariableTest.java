package org.afdb.aikp.modules.questionnaire.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireGroupId;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireId;
import org.junit.jupiter.api.Test;

class QuestionnaireVariableTest {

    @Test
    void shouldCreateVariable() {

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        QuestionnaireVariable variable =
                QuestionnaireVariable.create(
                        questionnaireId,
                        groupId,
                        "GDP_001",
                        "Gross Domestic Product",
                        "Total GDP",
                        QuestionnaireVariableDataType.NUMBER,
                        "USD",
                        true,
                        2);

        assertThat(variable.getId()).isNotNull();
        assertThat(variable.getQuestionnaireId())
                .isEqualTo(questionnaireId);
        assertThat(variable.getQuestionnaireGroupId())
                .isEqualTo(groupId);
        assertThat(variable.getSeriesCode())
                .isEqualTo("GDP_001");
        assertThat(variable.getName())
                .isEqualTo("Gross Domestic Product");
        assertThat(variable.getDefinition())
                .isEqualTo("Total GDP");
        assertThat(variable.getDataType())
                .isEqualTo(QuestionnaireVariableDataType.NUMBER);
        assertThat(variable.getUnit())
                .isEqualTo("USD");
        assertThat(variable.isRequired())
                .isTrue();
        assertThat(variable.getDisplayOrder())
                .isEqualTo(2);
        assertThat(variable.isActive())
                .isTrue();
    }

    @Test
    void shouldNormalizeSeriesCode() {

        QuestionnaireVariable variable =
                createVariable("  gdp_001  ");

        assertThat(variable.getSeriesCode())
                .isEqualTo("GDP_001");
    }

    @Test
    void shouldRejectBlankSeriesCode() {

        assertThatThrownBy(() ->
                createVariable("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullSeriesCode() {

        assertThatThrownBy(() ->
                createVariable(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Series code cannot be null or blank.");
    }

    @Test
    void shouldRejectSeriesCodeLongerThan100Characters() {

        assertThatThrownBy(() ->
                createVariable("A".repeat(101)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Series code cannot exceed 100 characters.");
    }

    @Test
    void shouldRejectNullName() {

        assertThatThrownBy(() ->
                QuestionnaireVariable.create(
                        QuestionnaireId.generate(),
                        null,
                        "GDP_001",
                        null,
                        null,
                        QuestionnaireVariableDataType.NUMBER,
                        null,
                        false,
                        0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Questionnaire variable name cannot be null or blank.");
    }

    @Test
    void shouldRejectNameLongerThan500Characters() {

        assertThatThrownBy(() ->
                QuestionnaireVariable.create(
                        QuestionnaireId.generate(),
                        null,
                        "GDP_001",
                        "A".repeat(501),
                        null,
                        QuestionnaireVariableDataType.NUMBER,
                        null,
                        false,
                        0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Questionnaire variable name cannot exceed 500 characters.");
    }

    @Test
    void shouldRejectNullDataType() {

        assertThatThrownBy(() ->
                QuestionnaireVariable.create(
                        QuestionnaireId.generate(),
                        null,
                        "GDP_001",
                        "GDP",
                        null,
                        null,
                        null,
                        false,
                        0))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Questionnaire variable data type cannot be null.");
    }

    @Test
    void shouldRejectUnitLongerThan100Characters() {

        assertThatThrownBy(() ->
                QuestionnaireVariable.create(
                        QuestionnaireId.generate(),
                        null,
                        "GDP_001",
                        "GDP",
                        null,
                        QuestionnaireVariableDataType.NUMBER,
                        "A".repeat(101),
                        false,
                        0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unit cannot exceed 100 characters.");
    }

    @Test
    void shouldRejectBlankName() {

        assertThatThrownBy(() ->
                QuestionnaireVariable.create(
                        QuestionnaireId.generate(),
                        null,
                        "GDP_001",
                        "   ",
                        null,
                        QuestionnaireVariableDataType.NUMBER,
                        null,
                        false,
                        0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNegativeDisplayOrder() {

        assertThatThrownBy(() ->
                QuestionnaireVariable.create(
                        QuestionnaireId.generate(),
                        null,
                        "GDP_001",
                        "GDP",
                        null,
                        QuestionnaireVariableDataType.NUMBER,
                        null,
                        false,
                        -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAssignAndRemoveGroup() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        variable.assignToGroup(groupId);

        assertThat(variable.getQuestionnaireGroupId())
                .isEqualTo(groupId);

        variable.removeFromGroup();

        assertThat(variable.getQuestionnaireGroupId())
                .isNull();
    }

    @Test
    void shouldChangeBusinessAttributes() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        variable.rename("Updated GDP");
        variable.changeDefinition("Updated definition");
        variable.changeDataType(
                QuestionnaireVariableDataType.TEXT);
        variable.changeUnit("EUR");
        variable.markAsOptional();
        variable.changeDisplayOrder(5);

        assertThat(variable.getName())
                .isEqualTo("Updated GDP");
        assertThat(variable.getDefinition())
                .isEqualTo("Updated definition");
        assertThat(variable.getDataType())
                .isEqualTo(QuestionnaireVariableDataType.TEXT);
        assertThat(variable.getUnit())
                .isEqualTo("EUR");
        assertThat(variable.isRequired())
                .isFalse();
        assertThat(variable.getDisplayOrder())
                .isEqualTo(5);
    }

    @Test
    void shouldRejectBlankNameWhenRenaming() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        assertThatThrownBy(() ->
                variable.rename("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Questionnaire variable name cannot be null or blank.");
    }

    @Test
    void shouldRejectNameLongerThan500CharactersWhenRenaming() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        assertThatThrownBy(() ->
                variable.rename("A".repeat(501)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Questionnaire variable name cannot exceed 500 characters.");
    }

    @Test
    void shouldRejectNullDataTypeWhenChangingDataType() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        assertThatThrownBy(() ->
                variable.changeDataType(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "Questionnaire variable data type cannot be null.");
    }

    @Test
    void shouldRejectUnitLongerThan100CharactersWhenChangingUnit() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        assertThatThrownBy(() ->
                variable.changeUnit("A".repeat(101)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unit cannot exceed 100 characters.");
    }

    @Test
    void shouldRejectNegativeDisplayOrderWhenChangingDisplayOrder() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        assertThatThrownBy(() ->
                variable.changeDisplayOrder(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Display order cannot be negative.");
    }

    @Test
    void shouldNormalizeBlankUnitWhenChangingUnit() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        variable.changeUnit("   ");

        assertThat(variable.getUnit())
                .isNull();
    }

    @Test
    void shouldNormalizeBlankDefinitionWhenChangingDefinition() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        variable.changeDefinition("   ");

        assertThat(variable.getDefinition())
                .isNull();
    }

    @Test
    void shouldActivateAndDeactivate() {

        QuestionnaireVariable variable =
                createVariable("GDP_001");

        variable.deactivate();

        assertThat(variable.isActive())
                .isFalse();

        variable.activate();

        assertThat(variable.isActive())
                .isTrue();
    }

    @Test
    void shouldRestoreVariable() {

        QuestionnaireId questionnaireId =
                QuestionnaireId.generate();

        QuestionnaireGroupId groupId =
                QuestionnaireGroupId.generate();

        QuestionnaireVariable original =
                QuestionnaireVariable.create(
                        questionnaireId,
                        groupId,
                        "GDP_001",
                        "GDP",
                        "Definition",
                        QuestionnaireVariableDataType.NUMBER,
                        "USD",
                        true,
                        3);

        QuestionnaireVariable restored =
                QuestionnaireVariable.restore(
                        original.getId(),
                        questionnaireId,
                        groupId,
                        "GDP_001",
                        "GDP",
                        "Definition",
                        QuestionnaireVariableDataType.NUMBER,
                        "USD",
                        true,
                        3,
                        false);

        assertThat(restored.getId())
                .isEqualTo(original.getId());
        assertThat(restored.isActive())
                .isFalse();
    }

    private QuestionnaireVariable createVariable(
            String seriesCode) {

        return QuestionnaireVariable.create(
                QuestionnaireId.generate(),
                null,
                seriesCode,
                "GDP",
                "Definition",
                QuestionnaireVariableDataType.NUMBER,
                "USD",
                true,
                0);
    }
}
