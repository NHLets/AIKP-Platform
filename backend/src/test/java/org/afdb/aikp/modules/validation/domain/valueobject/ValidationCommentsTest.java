package org.afdb.aikp.modules.validation.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ValidationCommentsTest {

    @Test
    void shouldCreateComments() {

        ValidationComments comments =
                ValidationComments.of(
                        "Data collection reviewed successfully.");

        assertThat(comments)
                .isNotNull();

        assertThat(comments.getValue())
                .isEqualTo(
                        "Data collection reviewed successfully.");
    }

    @Test
    void shouldTrimComments() {

        ValidationComments comments =
                ValidationComments.of(
                        "  Validation completed.  ");

        assertThat(comments.getValue())
                .isEqualTo(
                        "Validation completed.");
    }

    @Test
    void shouldReturnNullForNullComments() {

        ValidationComments comments =
                ValidationComments.of(null);

        assertThat(comments)
                .isNull();
    }

    @Test
    void shouldReturnNullForBlankComments() {

        ValidationComments comments =
                ValidationComments.of("   ");

        assertThat(comments)
                .isNull();
    }

    @Test
    void shouldAllowCommentsWithMaximumLength() {

        String value =
                "a".repeat(4000);

        ValidationComments comments =
                ValidationComments.of(value);

        assertThat(comments.getValue())
                .hasSize(4000);
    }

    @Test
    void shouldRejectCommentsExceedingMaximumLength() {

        String value =
                "a".repeat(4001);

        assertThatThrownBy(() ->
                ValidationComments.of(value))
                .isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage(
                        "Validation comments cannot exceed "
                                + "4000 characters.");
    }
}
