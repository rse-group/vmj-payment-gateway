echo SELECT 'CREATE DATABASE paymentgateway_product_creak' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_creak') \gexec | psql "postgresql://postgres:@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:@localhost/paymentgateway_product_creak"

java -cp paymentgateway.product.creak --module-path paymentgateway.product.creak -m paymentgateway.product.creak