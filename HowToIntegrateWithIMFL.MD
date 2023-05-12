IFML merupakan generator FE yang dibuat di lab RSE.
Repository ini mengintegrasikan Back End WinVMJ dengan IFML pada repository
https://gitlab.com/sheilarezkia/ifml-pwa-transformation-tool/-/tree/react-component

Untuk mengintegrasikannya hal-hal yang perlu dilakukan adalah :

1. Mengubah routing pada module product agar mengarah pada port yang sesuai
contoh, pada file src/aisco.product.sheilaschool/aisco/product/sheilaschool/SheilaSchool.java :
```
activateServer("localhost", 7776);
```

2. Menyesaikan route CRUD yang digunakan dengan endpoint yang diminta IFML, hal ini dapat diatur pada berkas 
src/vmj.routing.route/vmj/routing/route/VMJServer.java
```
    public void createABSCRUDEndpoint(String theEndpoint, String tableName, ArrayList<String> tableColumns) {
        server.createContext(("/api/" + theEndpoint), new HttpHandler() {
            ...
                    if (exchange.getRequestMethod().equals("GET") && uriParts[2].equals("save.abs")) {
                    ...
                    } else if (exchange.getRequestMethod().equals("GET") && uriParts[2].equals("list.abs")) {
                    ...
                    } else if (exchange.getRequestMethod().equals("GET") && uriParts[2].equals("detail.abs")) {
                    ...
                    } else if (exchange.getRequestMethod().equals("GET") && uriParts[2].equals("delete.abs")) {
                    ...
                    } else if (exchange.getRequestMethod().equals("GET") && uriParts[2].equals("update.abs")) {
                    ...
                    }
            ...
        }
    }
```

3. Menyesuaikan format response yang dikirim dengan response yang diminta oleh IFML
Response yang diminta oleh IFML biasa dalam bentuk JSON dengan key paling luar `{ data: .... }`, sehingga bungkus semua response yang ada dengan string `{ data: [RESPONSE] }`
contoh pada src/vmj.routing.route/vmj/routing/route/VMJServer.java
```
respText = "{ \"data\": " + respText + "}";
```
