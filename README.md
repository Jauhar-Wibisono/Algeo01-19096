# tubes1-algeo-kelompok54
Program kami memiliki 13 Class, dengan 1 class yang memiliki main method yaitu menu.

Cara menjalankan program :
1. Menjalankan menu.class di folder bin pada command line dengan command "java menu".
2. Memilih fitur yang ingin digunakan di halaman MENU, ada 6 pilihan dengan pilihan terakhir adalah keluar dari program.
3. Kelima pilihan lainnya antara lain :
    1. Sistem Persamaan Linear
    2. Determinan
    3. Matriks Balikan
    4. Interpolasi Polinom
    5. Regresi Linear Berganda
4. Khusus fitur 1-3 akan meminta metode yang ingin dilakukan, contoh fitur Sistem Persamaan Linear akan mendapat pilihan metode :
    1. Eliminasi Gauss
    2. Eliminasi Gauss-Jordan
    3. Matriks Balikan
    4. Kaidah Cramer
5. Untuk fitur Determinan dan Matriks Balikan akan mendapat pilihan metode :
    1. Reduksi Baris
    2. Ekspansi Kofaktor
6. Ketika sudah memilih fitur atau metodenya, akan diminta input matriks yang ingin dioperasi. Input dapat berupa langsung dari command line atau file .txt
7. Untuk input melalui file .txt, file perlu dibuat terlebih dahulu dan diletakan pada folder test. Lalu masukan nama file tersebut pada command line.
  - Pastikan matriks dalam file berbentuk nxm, dan setiap elemen dipisah menggunakan spasi atau ' '
  - Perlu diperhatikan, ada beberapa fitur yang tidak dapat menerima input matriks jika jumlah baris dan kolomnya tidak sesuai ketentuannya
  - Jika matriks tidak sesuai ketentuan fitur akan dilakukan validasi input
8. Untuk input langsung dari command line, tinggal mengikuti prosedur yang sudah ditentukan pada setiap fitur atau metodenya.
9. Ketika matriks sudah berhasil di-operasi, program kami dapat mencetak output dalam bentuk file .txt di folder test.
  - File tidak perlu dibuat terlebih dahulu, program kami dapat membuat file baru dengan nama yang diinput secara otomatis.
10. Output juga akan dicetak secara otomatis pada command line.
11. Tampilan akan kembali ke MENU dengan ke-6 pilihan pada langkah 3&4. Program akan berjalan terus hingga user memilih pilihan Keluar.
   
