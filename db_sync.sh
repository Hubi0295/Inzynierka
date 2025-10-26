#!/bin/bash
set -e

export PGPASSWORD=inzynierka

# Eksport danych z głównej bazy
pg_dump -h warehouse-db -U postgres -d warehouse > /tmp/main_backup.sql

# Wgrywanie do backupowej bazy (ten sam kontener)
psql -h localhost -U postgres -d warehouse -f /tmp/main_backup.sql
