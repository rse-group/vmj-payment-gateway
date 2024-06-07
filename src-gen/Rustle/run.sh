#!/bin/bash
source ~/.zshrc  

cleanup() {
    pkill -P $$
    rm java.log
    exit 1
}

trap cleanup SIGINT

java -cp paymentgateway.product.rustle --module-path paymentgateway.product.rustle -m paymentgateway.product.rustle 2>&1 | tee java.log &
JAVA_PID=$!
TEE_PID=$(pgrep -n tee)
tail -f java.log --pid=$TEE_PID | while read -r LINE; do
    if [[ "$LINE" == *"== CREATING OBJECTS AND BINDING ENDPOINTS =="* ]]; then
        break
    fi
done

echo "SELECT 'CREATE DATABASE paymentgateway_product_rustle' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_rustle') \gexec" | psql "postgresql://postgres:@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:@localhost/paymentgateway_product_rustle"
done

wait