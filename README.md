# JMatrix

<!-- Badges -->
<div align="center">
    <a href="https://github.com/mitsuki31/jmatrix/actions/workflows/codeql.yml">
        <img src="https://github.com/mitsuki31/jmatrix/actions/workflows/codeql.yml/badge.svg?branch=master"
             alt="codeql"
        />
    </a>
    <a href="https://github.com/mitsuki31/jmatrix/actions/workflows/release-drafter.yml">
        <img src="https://github.com/mitsuki31/jmatrix/actions/workflows/release-drafter.yml/badge.svg?branch=master"
             alt="release-drafter"
        />
    </a>
    <a href="https://github.com/mitsuki31/jmatrix/blob/master/.github/dependabot.yml">
        <img src="https://img.shields.io/static/v1?label=Dependabot&message=active&color=31c753&logo=dependabot&logoColor=white&labelColor=0366d6" />
    </a>
</div>
<!-- [END] Badges -->


**Developed by Ryuu Mitsuki<br>**

**Minimal Requirements:<br>**
- `Java 11`.
- `Python 3.7`.
- Latest version of `Make` or `Maven`.

> **Note**
> JMatrix is a matrix builder written in Java.  
> Can creates the matrix, operates basic matrix operations such as addition, subtraction, multiplication
> and transposition *(other operations will be added soon)*.<br>

<details>
<summary><h3><a name="table-of-contents"></a>Table of Contents</h3>
</summary>

- [How to Build the Project?](#build-project)
- [Constructor Summary](#constructor-summary)
    * [Matrix\()](#cr_matrix-1)
    * [Matrix\(int, int)](#cr_matrix-2)
    * [Matrix\(int, int, int)](#cr_matrix-3)
    * [Matrix\(double[][])](#cr_matrix-4)
- [Author](#author)
- [License](#license)
</details>

## <a name="build-project"></a> How to Build the Project?
Want to build the project with different Java version?  
Please consider refer to [:bookmark:Build the Project](https://github.com/mitsuki31/jmatrix/wiki/Getting%20Started#build-project)
section for more information.

## <a name="constructor-summary"></a> Constructor Summary
There are 4 constructors that can be used for constructing the matrix.  
> :man::question: Still don't understand about matrix? Check the [:bookmark:About Matrix][what-is-matrix] section
> to get little knowledge about matrix.

### <a name="cr_matrix-1"></a> Matrix()
This constructor doesn't need any arguments, but it would constructs the **Matrix**
with `null` entries. In this case, the matrix itself can't do any operations until
get initialized and have valid elements. For example:

```java
// Create null entries matrix
Matrix m = new Matrix();
```

But don't worry, you can also check whether the matrix has `null` entries with this code:

```java
boolean isNullEntries(Matrix m) {
    // Check whether the matrix has null entries using
    // "getEntries()" method
    if (m.getEntries() == null) {
        return true;
    }

    return false;
}
```

### <a name="cr_matrix-2"></a> Matrix(int, int)
Want to create `null matrix` (a.k.a. `zero matrix`)? This constructor is the answer.  
With just two arguments, which is for size of [row][matrix-row] and [column][matrix-col].  
The matrix can be called `null matrix` when all of matrix's elements is zero.
For example:

```java
// Create null matrix with size 3x4
Matrix m = new Matrix(3, 4);
```

Code above would constructs a new `null matrix` with size `3x4`. Use `display()` method to display
the matrix, and the output should looks like this:

```
[   [0.0, 0.0, 0.0, 0.0],
    [0.0, 0.0, 0.0, 0.0],
    [0.0, 0.0, 0.0, 0.0]   ]
```

### <a name="cr_matrix-3"></a> Matrix(int, int, int)
This constructor is similar with [Matrix\(int, int)](#cr_matrix-2) but with an additional
argument which is the value to filled out the entire elements of constructed matrix. For example:

```java
// Create new matrix with size 4x4 and 5 as default elements
Matrix m = new Matrix(4, 4, 5);

m.display();
```

Code above should output like this:

```
[   [5.0, 5.0, 5.0, 5.0],
    [5.0, 5.0, 5.0, 5.0],
    [5.0, 5.0, 5.0, 5.0],
    [5.0, 5.0, 5.0, 5.0]   ]
```

### <a name="cr_matrix-4"></a> Matrix(double\[]\[])
This constructor is very **RECOMMENDED** for constructing a new [matrix][what-is-matrix].  
It because you can declare the entries first, and then you can convert it to **Matrix** object whenever you want.
For example:

```java
// Declare and initialize entries "a"
double[][] a = {
    { 1, 2, 3 }
    { 4, 5, 6 }
};

// Convert to Matrix
Matrix m = new Matrix(a);
```

Or you can do the same thing with this code:

```java
// Create new matrix
Matrix m = new Matrix(new double[][] {
    { 1, 2, 3 }
    { 4, 5, 6 }
});
```


## <a name="author"></a> Author
[Ryuu Mitsuki](https://github.com/mitsuki31)

## <a name="license"></a> :balance_scale: License
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)


<!-- Shortcut -->
[jmatrix]: https://github.com/mitsuki31/jmatrix.git
[what-is-matrix]: https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix
[matrix-row]: https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#matrix-row
[matrix-col]: https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#matrix-column
