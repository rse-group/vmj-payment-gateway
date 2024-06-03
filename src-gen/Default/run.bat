echo SELECT 'CREATE DATABASE paymentgateway_product_default' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_default') \gexec | psql "postgresql://postgres:@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:@localhost/paymentgateway_product_default"

java -cp paymentgateway.product.default --module-path paymentgateway.product.default -m paymentgateway.product.default