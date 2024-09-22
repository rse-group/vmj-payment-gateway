#!/bin/bash
source ~/.zshrc  

cleanup() {
    echo "Exiting script..."
    pkill -P $$
    exit 1
}

trap cleanup SIGINT

echo "Enter the path to the frontend directory: "
read -p "Enter the path to the frontend directory: " frontend_dir

echo "SELECT 'CREATE DATABASE paymentgateway_product_multileveldelta' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_multileveldelta') \gexec" | psql "postgresql://postgres:postgres@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:postgres@localhost/paymentgateway_product_multileveldelta"
done

java -cp paymentgateway.product.multileveldelta --module-path paymentgateway.product.multileveldelta -m paymentgateway.product.multileveldelta &

cd $frontend_dir && {
    npm install && {
        npm run json:server &
        npm run start &
    }
}

wait