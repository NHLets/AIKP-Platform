# Migration Audit Report

## 1. Objectif

Ce document recense l'ensemble des migrations Flyway de l'AIKP Platform et leur état de synchronisation avec la base de données de développement.

## 2. État de référence

- Base de données : PostgreSQL 17
- Base : aikp
- Table Flyway : reference.flyway_schema_history
- Date du premier audit : 30 juillet 2026

## 3. Résumé

| Élément | Statut |
|----------|--------|
| Flyway installé | ✅ |
| Base opérationnelle | ✅ |
| Module Country validé | ✅ |
| Frontend connecté | ✅ |
| Synchronisation Git/Flyway | ⚠ À réconcilier |

## 4. Inventaire des migrations

| Version | Script | Exécutée | Statut | Observations |
|---------|--------|----------|--------|--------------|
| V1_000 | create_extensions | Oui | Conforme | À vérifier |
| V1_001 | create_schemas | Oui | Conforme | Validé |
| V1_002 | create_users_table | Non | Placeholder | 0 octet |
| V1_003 | create_roles_table | Non | Placeholder | 0 octet |
| … | … | … | … | … |

## 5. Schémas PostgreSQL

| Schéma | Statut |
|---------|--------|
| identity | Créé |
| reference | Créé |
| campaign | Créé |
| collection | Créé |
| validation | Créé |
| reporting | Créé |
| dashboard | Créé |
| notification | Créé |
| system | Créé |

## 6. Tables validées

### reference.country

- Validée
- Intégrée au backend
- Intégrée au frontend
- Déjà testée

## 7. Actions à mener

- Vérifier chaque migration
- Supprimer les placeholders inutiles
- Compléter les migrations manquantes
- Interdire toute modification d'une migration déjà exécutée

## 8. Historique des audits

| Date | Auteur | Résultat |
|------|---------|----------|
| 2026-07-30 | Équipe AIKP | Audit initial |