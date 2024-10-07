#!/bin/bash
source ~/.zshrc  

cleanup() {
    pkill -P $$
    rm java.log
    exit 1
}

trap cleanup SIGINT

echo "SELECT 'CREATE DATABASE paymentgateway_product_' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_') \gexec" | psql "postgresql://postgres:@localhost"

java -cp paymentgateway.product. --module-path paymentgateway.product. -m paymentgateway.product. 2>&1 | tee java.log &
JAVA_PID=$!
TEE_PID=$(pgrep -n tee)
tail -f java.log --pid=$TEE_PID | while read -r LINE; do
    if [[ "$LINE" == *"== CREATING OBJECTS AND BINDING ENDPOINTS =="* ]]; then
        break
    fi
done

for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:@localhost/paymentgateway_product_"
done

wait