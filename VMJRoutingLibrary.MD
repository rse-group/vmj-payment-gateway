# VMJ Routing Library

Dokumentasi penggunaan VMJ Routing Library.

## Getting Started

VMJ Routing Library memiliki beberapa aset yang dapat digunakan, yaitu:

- VMJServer
- VMJExchange
- Route
- Router

### VMJServer

Aset yang digunakan untuk menginisiasi server. Method yang dimiliki:

#### getInstance

Untuk membuat objek VMJServer. Dapat dilakukan dengan memberikan informasi nama host serta nomor port.

Menginisiasi pertama kali:

```
VMJServer vmjServer = VMJServer.getInstance("localhost", 8080);
```

Membuat objek:

```
........ VMJServer.getInstance();
```

#### startServerGeneric

Aktivasi pasangan nama host serta nomor port yang sudah diinisiasi pada method getInstance.

```
vmjServer.startServerGeneric();
```

#### createTableCRUDEndpoint

Membuat endpoint untuk keperluan Create, Read, Update, Delete (CRUD) dari suatu tabel. Dipanggil dengan memberikan parameter sebagai berikut.

| Parameter Ke- | Parameter      | Tipe       |
|---------------|----------------|------------|
|1.| nama endpoint (suffix) | String     |
|2.| nama tabel     | String     |
|3.| nama-nama kolom tabel | ArrayList of Strings |

Contoh pemanggilan:

```
vmjServer.createTableCRUDEndpoint("programs", "program_core", VMJDatabaseMapper.getTableColumnsNames("aisco.program.core.ProgramComponent", false));
```

### VMJExchange

Aset yang berfungsi untuk menerima data yang dikirimkan oleh request melalui protokol HTTP. Beberapa method yang dimiliki:

#### getPOSTBodyForm

Mengakses value dari body form request yang bertipe POST dengan memberikan key berupa String sebagai parameter pemanggilan method.

```
Object executionDate = vmjExchange.getPOSTBodyForm("executionDate");
```
#### getGETParam

Mengakses value dari parameter URL request yang bertipe GET dengan memberikan key berupa String sebagai parameter pemanggilan method.

```
Object idProgram = vmjExchange.getGETParam("id");
```

### Route

Aset yang menyediakan anotasi yang dapat digunakan oleh method milik sebuah class untuk menentukan alamat atau endpoint yang dapat digunakan untuk memanggil method tersebut melalui HTTP request. Letakkan anotasi @Route serta mengisi parameter "url" untuk membuat method dapat dipanggil melalui HTTP request.

```
@Route(url="totalIncome")
public HashMap<String,Object> totalIncome(VMJExchange vmjExchange) {
```

### Router

Aset yang berfungsi untuk mengikat method-method yang dimiliki oleh sebuah class dengan URL yang ditentukannya melalui anotasi @Route. Memiliki satu method, yaitu:

#### bindMethod

Digunakan untuk mengikat satu method yang sudah diberikan anotasi @Route agar benar-benar dapat dipanggil melalui HTTP request. Parameter yang dibutuhkan untuk memanggil method ini:

| Parameter Ke- | Parameter      | Tipe       |
|---------------|----------------|------------|
|1|Nama method yang sudah diberikan anotasi @Route | String|
|2|Objek yang memiliki method yang ingin diikat| Object |

```
Blog shareBlog = BlogFactory.createBlog("blog.page.share.BlogImpl", BlogFactory.createBlog("blog.page.core.BlogImpl"));
Router.bindMethod("createPost", shareBlog);
```


