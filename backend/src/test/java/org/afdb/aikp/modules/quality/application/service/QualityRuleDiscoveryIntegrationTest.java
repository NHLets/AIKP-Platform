package org.afdb.aikp.modules.quality.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.afdb.aikp.modules.quality.domain.service.QualityRule;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QualityRuleDiscoveryIntegrationTest {

    @Autowired
    private List<QualityRule> rules;

    @Test
    void shouldDiscoverAllFgRules() {

        List<String> fgRuleCodes = rules.stream()
                .map(QualityRule::code)
                .filter(code -> code.startsWith("FG-"))
                .sorted()
                .toList();

        assertThat(fgRuleCodes)
                .containsExactly(
                        "FG-001",
                        "FG-002",
                        "FG-003",
                        "FG-004",
                        "FG-005",
                        "FG-006",
                        "FG-007",
                        "FG-008",
                        "FG-009",
                        "FG-010",
                        "FG-011");
    }
}
