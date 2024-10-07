echo SELECT 'CREATE DATABASE paymentgateway_product_' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_') \gexec | psql "postgresql://postgres:@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:@localhost/paymentgateway_product_"

java -cp paymentgateway.product. --module-path paymentgateway.product. -m paymentgateway.product.