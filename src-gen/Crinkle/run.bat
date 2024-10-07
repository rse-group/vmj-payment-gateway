echo SELECT 'CREATE DATABASE paymentgateway_product_crinkle' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_crinkle') \gexec | psql "postgresql://postgres:@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:@localhost/paymentgateway_product_crinkle"

java -cp paymentgateway.product.crinkle --module-path paymentgateway.product.crinkle -m paymentgateway.product.crinkle