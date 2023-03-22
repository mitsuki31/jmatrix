# JMatrix
**Author : Ryuu Mitsuki<br>**

> JMatrix is a matrix builder written in Java.<br>
> Developed by Ryuu Mitsuki.<br>
> It can create, summarize, subtract and clear the matrix array

---

Make sure you've `Java` installed in your device:warning:<br>
If not, download here:<br>
1. [https://openjdk.org](https://openjdk.org)
2. [https://developers.redhat.com/products/openjdk/download](https://developers.redhat.com/products/openjdk/download)

## LIST FUNCTION:rocket:
> NOTE:<br>
> All below functions isn't static, so create a new Matrix object first :)<br>
> Except for:
> - static void display(int[ ][ ] array)
> - static void sort(int[ ][ ] array)


### add()
Fill column of matrix array with push method.<br>
If you attempt to call `add()` function again and you've matrix size 2x3, but you've already call `add()` 2 times (same as total rows), it'll throw `MatrixArrayFullException`.<br>
<br>
Function parameters:
- void add(int... values)
- void add(int value)
> For the second parameter, it would create a single column with same value and for size equal to total matrix rows, then assign to matrix array.


### sum()
Summarize current matrix array with other matrix array.<br>
<br>
Function parameters:
- int[ ][ ] sum(Matrix object)
- int[ ][ ] sum(int[ ][ ] array)

### sub()
Subtract current matrix array with other matrix array.<br>
<br>
Function parameters:
- int[ ][ ] sub(Matrix object)
- int[ ][ ] sub(int[ ][ ] array)

### sort()
Sort all columns inside matrix array.<br>
<br>
Function parameters:
- void sort()
- static void sort(int[ ][ ] array)

### getSize()
Return list of matrix size.<br>
<br>
Function parameters:
- int[ ] getSize()  <- return [\<rows\>, \<cols\>]

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
- static void display(int[ ][ ] array)


## EXAMPLE USAGE

I've created example for `JMatrix` usage in `examples` directory (just for references).<br>

---

**Clone this repository and goto `jmatrix` directory<br>**
```shell|powershell
git clone https://github.com/mitsuki31/jmatrix.git
cd jmatrix
```

**Compile the program<br>**
- Linux / Unix
```shell
javac examples/ExampleMatrix.java
```
- Windows
```powershell
javac "examples\ExampleMatrix.java"
```

**Run the program<br>**
```shell|powershell
java examples.ExampleMatrix
```
