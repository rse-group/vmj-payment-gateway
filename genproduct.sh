#!/bin/bash
set -e

err(){
    echo "Error: $*" >>/dev/stderr
}

function copy_vmj_libraries_and_their_dependencies_to_module_path() {
    cp winvmj-libraries/*.jar $product
    echo "copy libraries"
}

function copy_databaseproperties() {
    cp auth.properties $product/
    cp hibernate.properties $product/
}

# function check_module() {
#     if [ $1 == "aisco.donation.pgateway" ]; then
#         cp external/payment.method.core.jar $product/
#         cp external/payment.method.dokudua.jar $product/
#         echo "  add external module for donation via payment gateway"
#     fi
# }

function validate_product() {
    if [ $1 == "$plname.config.core" ];then
      account=true
    fi
}

function build_product() {
    echo -e "building the product: $mainclass"
    javac -d cclasses --module-path $product $(find src/$product -name "*.java") src/$product/module-info.java
    jar --create --file $product/$mainclass.jar --main-class $product.$mainclass -C cclasses .
    rm -r cclasses
    echo "Product $mainclass is ready"
}

function external_module() {
    cp -r external/* $product/
}

function build_product_requirement() {
    echo " -- checking requirements --"
    product=$1
    targetpath="src/$product/module-info.java"
    # req=$(cat $targetpath | grep requires| awk '{print $2}' | cut -d';' -f 1 )
    req=$(cat $targetpath | grep "requires" | awk '{if ($2=="transitive") print $3; else print $2;}' | cut -d';' -f 1)

    copy_vmj_libraries_and_their_dependencies_to_module_path
    # validate
    for requ in $req; do
        validate_product $requ
    done

    if [ "$account" == true ]; then
        for reqprod in $req; do
            echo $reqprod
            if [[ $reqprod =~ "$plname" ]]; then
                echo -e "building requirement for $mainclass: $reqprod"
                #check_module $reqprod
                javac -d cclasses --module-path $product $(find src/$reqprod -name "*.java") src/$reqprod/module-info.java
                jar --create --file $product/$reqprod.jar -C cclasses .
                rm -r cclasses
            elif [[ $reqprod =~ "vmj" ]]; then
                echo "library $reqprod was added"  
            else
                echo "check requirement from another product line"
                external_module $reqprod
            fi
        done
        build_product
    else
        echo "product is not valid"
        echo "build failed"
    fi
}

product=$1
mainclass=$2
plname="paymentgateway"
echo $product
echo $mainclass
if [ -d "$1" ]; then rm -r $1; fi
if [ -d "classes" ]; then rm -r classes; fi
mkdir $1
build_product_requirement $product
#build_product
copy_databaseproperties
