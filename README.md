# WinVMJ Project Payment Gateway
This repository consists a FeatureIDE project with WinVMJ composer for Payment Gateway case study.

## Requirements
Install Eclipse and required plugins to run this project:
- Eclipse Modeling Tools  (2020-12): https://www.eclipse.org/downloads/packages/release/2020-12/r/eclipse-modeling-tools
- Plugin FeatureIDE 3.9:  http://featureide.cs.ovgu.de/update/v3/
- Plugin WinVMJ composer: https://amanah.cs.ui.ac.id/priceside/winvmj-composer/updatesite
- Java 17
- PostgreSQL 11

## Getting Started
- Make sure that all requirements installed in your Eclipse
- Clone this repository into your directory
- Import your directory into Eclipse workspace (do not include the .git folder)
- Open the project, the structure directory is:
    ```
    .
    ├── src
    ├── configs
    ├── external
    ├── modules
    ├── src-gen
    ├── db.properties
    ├── feature_to_module.json
    ├── model.uvl
    ```
- Don't forget to edit file `db.properties` in the project with your PostgreSQL credentials

## Development
WinVMJ framework is designed based on Varibility Modules for Java (VMJ).
VMJ is an architectural pattern in Java to implement software product lines
and multi product lines based on Delta Oriented Programming (DOP).
VMJ combines Java module system and design pattern to implement DOP. 

To implement a new variation:
1. Add a new feature in the feature diagram (model.uvl)
2. Develop a module in the directory `module`.
Naming convention for the folder:
- Core module [productlinename].[modulename].core
- Delta module [productlinename].[coremodulename].[deltamodulename]
FYI, the core and delta module can be generated from the UML-DOP diagram,
you can also create/update the UML-DOP diagram and generate the source.
See this repository: https://gitlab.com/RSE-Lab-Fasilkom-UI/PricesIDE/uml-to-vmj
3. Define a mapping between feature name and delta name in file `feature_to_module.json`

## Generate and Run Product
### Generate Product
Generate product is started with creating a new configuration:
1. The configuration is defined in directory `configs`. 
2. Right click on the project -> NEW -> OTHER -> FeatureIDE -> Configuration File
3. Defined the config file's name that represents the product's name
4. Select required features, right click on the config file -> FeatureIDE -> select `Set As Current Configuration`
5. Generated modules are available in directory `src`
6. Compile the generated module: right click on directory `src` -> FeatureIDE -> WinVMJ -> Compile
The generated application is placed in directory `src-gen`

### Run Product
1. Choose Run -> External Tools -> External Tools Configurations. Define the configuration's name.
2. Fill in location of the generated product, e.g., `${workspace_loc:/paymentgateway-winmvj/src-gen/{product_name}/run.bat}`
3. Fill in the working directory by defining the product's directory, e.g.,`${workspace_loc:/paymentgateway-winmvj/src-gen/{product_name}}`
4. Click Run
5. If succeed, the product is ready and a list of avalaible endpoints is printed on the console, for example:
    ```
    http://localhost:7776/call/agent
    ```

## Postman Collection Generation for a Product
In the product composition flow in the [API for the Software as a Service web for product creation and deployment](https://gitlab.com/RSE-Lab-Fasilkom-UI/PricesIDE/payment-gateway/payment-gateway-saas-cli), the Postman collection of the product is generated and saved in the `postman-collections-gen` directory that is located in the root directory of the `vmj-payment-gateway` project. The name of the file is in the format `<product-name>_postman_collection.json`. The creation of the Postman collection of the product is done by filtering the collection items from the main Postman collection file (which is the `paymentgateway_postman_collection.json` file located in the root directory of the `vmj-payment-gateway` project) based on the endpoints that are in the product. The `paymentgateway_postman_collection.json` contains all of the positive and negative cases of the endpoints that are in the VMJ Payment Gateway product line (except the callback endpoints).

If there are new endpoints in this product line, the `paymentgateway_postman_collection.json` file needs to be updated. This can be done by importing the file into Postman and adding the positive and negative case of the new endpoints.

## Swagger UI Configuration Generation for a Product
In this product composition flow in [API for the Software as a Service web for product creation and deployment](https://gitlab.com/RSE-Lab-Fasilkom-UI/PricesIDE/payment-gateway/payment-gateway-saas-cli), the Swagger UI configuration of the product is generated and saved in the `swagger-configs-gen` directory that is located in the root directory of the `vmj-payment-gateway` project. There are 2 files that are generated, which are `<product-name>-swagger-ui.html` and `<product-name>-swagger-ui-initializer.js`. These 2 files are needed to display the Swagger UI, which contains the endpoints that are in the product. The creation of the Swagger UI configuration for a product is done by filtering the endpoint configurations from the main OpenAPI specification file (which is the `openapi_spec.json` file located in the root directory of the `vmj-payment-gateway` project) based on the endpoints that are in the product. The `openapi_spec.json` contains all of the endpoints that are in the VMJ Payment Gateway product line (except the callback endpoints).

If there are new endpoints in this product lline, the `openapi_spec.json` file needs to be updated. This can be done by manually modifying the file.