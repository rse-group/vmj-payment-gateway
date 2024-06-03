#!/bin/bash
source ~/.zshrc  

cleanup() {
    pkill -P $$
    rm java.log
    exit 1
}

trap cleanup SIGINT

java -cp paymentgateway.product.whoosh --module-path paymentgateway.product.whoosh -m paymentgateway.product.whoosh 2>&1 | tee java.log &
JAVA_PID=$!
TEE_PID=$(pgrep -n tee)
tail -f java.log --pid=$TEE_PID | while read -r LINE; do
    if [[ "$LINE" == *"== CREATING OBJECTS AND BINDING ENDPOINTS =="* ]]; then
        break
    fi
done

echo "SELECT 'CREATE DATABASE paymentgateway_product_whoosh' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_whoosh') \gexec" | psql "postgresql://postgres:@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:@localhost/paymentgateway_product_whoosh"
done

wait