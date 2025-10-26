#!/bin/bash
set -e

# Instalacja niezbędnych narzędzi
apt-get update -qq && apt-get install -y -qq cron postgresql-client curl

export PGPASSWORD=inzynierka
until pg_isready -h warehouse-db -p 5432 -U postgres; do sleep 3; done

PRODUCT_COUNT=$(psql -h warehouse-db -U postgres -d warehouse -tAc "SELECT COUNT(*) FROM products;")

if [ "$PRODUCT_COUNT" = "0" ]; then
  psql -h warehouse-db -U postgres -d warehouse -f /init_data.sql
else
  echo "Table 'products' already contains data"
fi
echo "50 15 * * * /bin/bash /db_sync.sh >> /var/log/db_sync.log 2>&1" > /etc/cron.d/db-sync
chmod 0644 /etc/cron.d/db-sync
crontab /etc/cron.d/db-sync
cron -f
