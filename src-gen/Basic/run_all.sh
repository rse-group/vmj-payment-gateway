#!/bin/bash

cleanup() {
    echo "Exiting script..."
    pkill -P $$
    exit 1
}

trap cleanup SIGINT

read -p "Enter the path to the frontend directory: " frontend_dir

echo "SELECT 'CREATE DATABASE paymentgateway_product_basic' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_basic') \gexec" | psql "postgresql://postgres:admin@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:admin@localhost/paymentgateway_product_basic"
done

java -cp paymentgateway.product.basic --module-path paymentgateway.product.basic -m paymentgateway.product.basic &

cd $frontend_dir && {
    npm install && {
        npm run json:server &
        npm run start &
    }
}

wait