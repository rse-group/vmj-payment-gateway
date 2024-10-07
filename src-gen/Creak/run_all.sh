#!/bin/bash

cleanup() {
    echo "Exiting script..."
    pkill -P $$
    exit 1
}

trap cleanup SIGINT

read -p "Enter the path to the frontend directory: " frontend_dir

echo "SELECT 'CREATE DATABASE paymentgateway_product_creak' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_creak') \gexec" | psql "postgresql://postgres:@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:@localhost/paymentgateway_product_creak"
done

java -cp paymentgateway.product.creak --module-path paymentgateway.product.creak -m paymentgateway.product.creak &

cd $frontend_dir && {
    npm install && {
        npm run json:server &
        npm run start &
    }
}

wait