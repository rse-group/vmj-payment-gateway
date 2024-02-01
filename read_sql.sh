role=$1
password=$2
dbname=$3
sqlfile=$4
PGPASSWORD=$2 psql -U $1 -d $3 -a -f $4