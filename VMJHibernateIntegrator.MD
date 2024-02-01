# VMJ Hibernate Integrator

Dokumentasi penggunaan VMJ Hibernate Integrator.

## Getting Started

VMJ Hibernate Integrator memiliki beberapa aset, yaitu :

- HibernateUtil
- RepositoryUtil

### HibernateUtil

HibernateUtil merupakan \f{utility class} untuk membuat atau mendapatkan objek SessionFactory Hibernate yang merupakan \f{singleton}. 

Session bisa diambil menggunakan command :

```
Session session = (HibernateUtil.getSessionFactory()).openSession()
```

### RepositoryUtil
RepositoryUtil merupakan kelas generik yang mendefinisikan segala operasi ke database

Contoh pembuatan repository :
```
this.financialReportRepository = new RepositoryUtil<FinancialReport>(aisco.financialreport.core.FinancialReportComponent.class);
```

Operasi yang ada pada RepositoryUtil adalah :
- saveObject(Y object) : menyimpan sebuah objek ke database.
- updateObject(Y object) : merubah sebuah objek yang ada pada database.
- getObject(int id) : mendapatkan sebuah objek pada database.
- getListObject(String tableName, String columnName, Z columnValue) : mendapatkan list objek berdasarkan filter yang diberikan.
- getAllObject(String tableName) : mendapatkan semua objek yang ada pada suatu tabel.
- deleteObject(int id) : menghapus sebuah objek.
- getProxyObject(Class<T> type, int idObject) : mendapatkan sebuah objek proxy.

Notes: RepositoryUtil hanya berisi operasi dasar yang biasa digunakan saat mengakses database, RepositoryUtil juga bisa di extends untuk memenuhi kebutuhan yang lebih spesifik