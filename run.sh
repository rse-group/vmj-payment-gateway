productline=$1
mainclass=$2
productline_lower="$(echo $productline | tr '[A-Z]' '[a-z]')"
mainclass_lower="$(echo $mainclass | tr '[A-Z]' '[a-z]')"
product="$productline_lower.product.$mainclass_lower"
java -cp $product --module-path $product -m $product
