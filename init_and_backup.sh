#!/bin/bash
set -e
apt-get update -qq && apt-get install -y -qq cron postgresql-client curl
export PGPASSWORD=inzynierka
PRODUCT_COUNT=$(psql -h warehouse-db -U postgres -d warehouse -tAc "SELECT COUNT(*) FROM products;")
if [ "$PRODUCT_COUNT" = "0" ]; then
  echo "🌱 Table 'products' is empty — seeding with init_data.sql"
  psql -h warehouse-db -U postgres -d warehouse -f /init_data.sql
else
  echo "✅ Table 'products' already contains data — skipping seed."
fi

echo "05 21 * * * /bin/bash /db_sync.sh >> /var/log/db_sync.log 2>&1" > /etc/cron.d/db-sync
chmod 0644 /etc/cron.d/db-sync
crontab /etc/cron.d/db-sync
echo "🚀 Starting cron daemon..."
cron -f
