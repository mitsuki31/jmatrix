# JMatrix
**Author : Ryuu Mitsuki<br>**

> JMatrix is a matrix builder written in Java.<br>
> Developed by Ryuu Mitsuki.<br>
> It can create, summarize, subtract, multiply and clear the matrix array.<br>
> Check `LIST FUNCTION` below to see all functions.

---

Make sure you've `Java` installed in your device:warning:<br>
If not, download here:<br>
1. [https://openjdk.org](https://openjdk.org)
2. [https://developers.redhat.com/products/openjdk/download](https://developers.redhat.com/products/openjdk/download)

## LIST FUNCTION:rocket:

### add()
Fill column of matrix array with push method.<br>
If you attempt to call `add()` function again and you've matrix size 2x3, but you've already call `add()` 2 times (same as total rows), it'll throw `MatrixArrayFullException`.<br>
<br>
Function parameters:
- void add(int... values)
- void add(int value)

### sum()
Summarize current matrix array with other matrix.<br>
<br>
Function parameters:
- void sum(Matrix object)
- void sum(int[ ][ ] array)
<br>

- static int[ ][ ] sum(int[ ][ ] array, int[ ][ ] array)
- static Matrix sum(Matrix obj1, Matrix obj2)

### sub()
Subtract current matrix array with other matrix.<br>
<br>
Function parameters:
- void sub(Matrix obj)
- void sub(int[ ][ ] arr)
<br>

- static int[ ][ ] sub(int[ ][ ] arr, int[ ][ ] arr)
- static Matrix sub(Matrix obj1, Matrix obj2)

### mult()
Multiply current matrix with other matrix.<br>
<br>
Function parameters:
- void mult(Matrix obj)
- void mult(int[ ][ ] arr)
<br>

- static int[ ][ ] mult(int[ ][ ] arr, int[ ][ ] arr)
- static Matrix mult(Matrix obj1, Matrix obj2)

### transpose()
Transpose current matrix or current matrix transpose to other matrix.<br>
<br>
Function parameters:
- void transpose()
<br>

- static int[ ][ ] transpose(int[ ][ ] arr)
- static Matrix transpose(Matrix obj)


### create()
Create a new matrix with specified rows and columns.<br>
<br>
Function parametera:
- void create(int rows, int cols)

### select()
Select row matrix by given index.<br>
<br>
Function parameters:
- void select()

### change()
Change values of selected row with given values.<br>
> Use this together with `select` function.<br>
> Example:  `matrixA.select(<index>).change(<values>)`
<br>

Function parameters:
- void change(int... values)
- void change(int value)

### copy()
Copy current matrix to another matrix object.<br>
<br>
Function parameters:
- Matrix copy()

### sort()
Sort all columns inside matrix array.<br>
<br>
Function parameters:
- void sort()
- static void sort(int[ ][ ] arr)

### getSize()
Return list of matrix size [rows, columns].<br>
<br>
Function parameters:
- int[ ] getSize()

### clear()
Clear all each column inside matrix array, and change all values with 0.<br>
<br>
Function parameters:
- void clear()

### display()
Display the matrix to console output.<br>
<br>
Function parameters:
- void display()
- void display(int index)
<br>

- static void display(int[ ][ ] arr)
- static void display(int[ ][ ] arr, int index)

<br>

## USAGE
Compile and create a `JAR` file with `Makefile`
```bash|powershell
make
```

Run the program
```bash|powershell
make run
```

## EDIT MANIFEST
If you want to change the `Main` file while running the program,
change `Main-Class` in `META-INF/MANIFEST.MF`.<br>
Default: `lib.matrix.ExampleMatrix`
