#!/bin/bash

cleanup() {
    echo "Exiting script..."
    pkill -P $$
    exit 1
}

trap cleanup SIGINT

read -p "Enter the path to the frontend directory: " frontend_dir

echo "SELECT 'CREATE DATABASE paymentgateway_product_rustle' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_rustle') \gexec" | psql "postgresql://postgres:@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:@localhost/paymentgateway_product_rustle"
done

java -cp paymentgateway.product.rustle --module-path paymentgateway.product.rustle -m paymentgateway.product.rustle &

cd $frontend_dir && {
    npm install && {
        npm run json:server &
        npm run start &
    }
}

wait