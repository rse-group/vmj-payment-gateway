echo SELECT 'CREATE DATABASE paymentgateway_product_whoosh' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_whoosh') \gexec | psql "postgresql://postgres:@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:@localhost/paymentgateway_product_whoosh"

java -cp paymentgateway.product.whoosh --module-path paymentgateway.product.whoosh -m paymentgateway.product.whoosh