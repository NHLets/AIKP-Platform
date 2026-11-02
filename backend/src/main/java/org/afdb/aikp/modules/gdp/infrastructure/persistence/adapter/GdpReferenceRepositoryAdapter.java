package org.afdb.aikp.modules.gdp.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.afdb.aikp.modules.gdp.domain.repository.GdpReferenceRepository;
import org.afdb.aikp.modules.gdp.domain.repository.GdpReferenceRepository.GdpReferenceRow;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GdpReferenceRepositoryAdapter implements GdpReferenceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GdpReferenceRow> findGdp(String iso3Code, Integer year) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    c.iso3_code,
                    c.name AS country_name,
                    g.reference_year,
                    g.gdp_current_usd,
                    g.source_name,
                    g.source_revision,
                    g.source_series_code,
                    g.source_url
                FROM reference.country_gdp g
                JOIN reference.country c ON c.id = g.country_id
                WHERE c.active = TRUE
                """);

        List<Object> parameters = new ArrayList<>();
        if (iso3Code != null) {
            sql.append(" AND UPPER(c.iso3_code) = ?");
            parameters.add(iso3Code);
        }
        if (year != null) {
            sql.append(" AND g.reference_year = ?");
            parameters.add(year);
        }
        sql.append(" ORDER BY c.name, g.reference_year DESC");

        Query query = entityManager.createNativeQuery(sql.toString());
        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<GdpReferenceRow> result = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            result.add(new GdpReferenceRow(
                    (String) row[0],
                    (String) row[1],
                    ((Number) row[2]).intValue(),
                    (BigDecimal) row[3],
                    (String) row[4],
                    (String) row[5],
                    (String) row[6],
                    (String) row[7]));
        }
        return result;
    }
}
