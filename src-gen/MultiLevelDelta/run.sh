#!/bin/bash
source ~/.zshrc  

cleanup() {
    pkill -P $$
    rm java.log
    exit 1
}

trap cleanup SIGINT

echo "SELECT 'CREATE DATABASE paymentgateway_product_multileveldelta' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_multileveldelta') \gexec" | psql "postgresql://postgres:postgres@localhost"

java -cp paymentgateway.product.multileveldelta --module-path paymentgateway.product.multileveldelta -m paymentgateway.product.multileveldelta 2>&1 | tee java.log &
JAVA_PID=$!
TEE_PID=$(pgrep -n tee)
tail -f java.log --pid=$TEE_PID | while read -r LINE; do
    if [[ "$LINE" == *"== CREATING OBJECTS AND BINDING ENDPOINTS =="* ]]; then
        break
    fi
done

for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:postgres@localhost/paymentgateway_product_multileveldelta"
done

wait