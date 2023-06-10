# JMatrix

<!-- Badges -->
<div id="workflows" align="center">
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
</div>
<div id="other-badges" align="center">
    <a href="https://github.com/mitsuki31/jmatrix/blob/master/.github/dependabot.yml">
        <img src="https://img.shields.io/static/v1?label=Dependabot&message=active&color=31c753&logo=dependabot&logoColor=white&labelColor=0366d6" />
    </a>
    <a href="https://github.com/mitsuki31/jmatrix/blob/master/LICENSE">
        <img src="https://img.shields.io/github/license/mitsuki31/jmatrix.svg?label=License&color=f9f9f9&labelColor=yellow&logo=github" />
    </a>
    <a>
        <img src="https://img.shields.io/static/v1?label=Java&message=11&labelColor=orange&color=f9f9f9&logo=openjdk" />
    </a>
    <a>
        <img src="https://img.shields.io/static/v1?label=Python&message=3.7&labelColor=383838&color=f9f9f9&logo=python" />
    </a>
</div>
<!-- [END] Badges -->


**Developed by one person ([Ryuu Mitsuki](https://github.com/mitsuki31))**  

**JMatrix** is a [Java][java] library designed to simplify [matrix operations](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-matrix-operations). It provides a set of intuitive methods to perform common [matrix operations](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-matrix-operations) with ease.

With **JMatrix**, you can create matrices of various dimensions, initialize them with values, and perform some basic [matrix operations](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-matrix-operations). It supports both square and rectangular matrix.

**JMatrix** provides basic matrix operations, such as:

- [Addition](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-addition)
- [Subtraction](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-subtraction)
- [Multiplication](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-multiplication)
- [Transposition](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-transposition)
- *(other matrix operations will be added soon)*

<details>
<summary><b>What is Matrix?</b></summary>

> Matrices are rectangular arrays of numbers or symbols, arranged in [rows][matrix-row] and [columns][matrix-col].  
> They are widely used in various fields, including mathematics, physics, computer science, and engineering.  
> Matrices provide a concise and organized way to represent and manipulate data.  
>  
> For more information, please refer to :books: [JMatrix Wikis][what-is-matrix].
</details>

---

**Prerequisites:**
- [**Java**][java] *(min. version 11)*.
- [**Python**](https://www.python.org) *(min. version 3.7)*.
- Latest version of [**Make**](https://www.mingw-w64.org/downloads/) or [**Maven**](https://maven.apache.org/download.cgi).
- [**Git Bash**](https://git-scm.com/downloads) *(Optional)*.  
> **Note** If you use [Maven](https://maven.apache.org) to build the project, you don't need to install [Python](https://www.python.org).

<details>
<summary><h2><a name="table-of-contents"></a>Table of Contents</h2>
</summary>

- [How to Build the Project?](#build-project)
- [Constructor Summary](#constructor-summary)
    * [Matrix\()](#cr_matrix-1)
    * [Matrix\(int, int)](#cr_matrix-2)
    * [Matrix\(int, int, int)](#cr_matrix-3)
    * [Matrix\(double[][])](#cr_matrix-4)
    * [Matrix.identity\(int)](#cr_matrix-5)
- [Matrix Operations](#matrix-ops)
    * [Addition](#matrix-add)
    * [Subtraction](#matrix-sub)
    * [Scalar Multiplication](#scalar-mult)
    * [Matrix Multiplication](#matrix-mult)
    * [Transposition](#matrix-transpose)
- [Author](#author)
- [License](#license)
</details>

## <a name="build-project"></a> How to Build the Project?
Want to build the project with different [Java][java] version?  
Please consider refer to [:bookmark:Build the Project](https://github.com/mitsuki31/jmatrix/wiki/Getting%20Started#build-project)
section for more information.

## <a name="constructor-summary"></a> Constructor Summary
There are 5 constructors that can be used for constructing the matrix.  
> :man::question: Still don't understand about matrix? Check the [:bookmark:About Matrix][what-is-matrix] section
> to get little knowledge about matrix before dive into matrix constructor.

### <a name="cr_matrix-1"></a> Matrix()
This constructor doesn't need any arguments, but it would constructs the **Matrix**
with `null` entries. In this case, the matrix itself can't do any operations until
get initialized and have valid elements. For example:

```java
// Create null entries matrix
Matrix m = new Matrix();
```

But don't worry, you can also check whether the matrix has `null` entries with your own method ...

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

... or using the **JMatrix**'s built-in method in `MatrixUtils` interface, which has been added to **JMatrix** from [#35](https://github.com/mitsuki31/jmatrix/pull/35).

```java
MatrixUtils.isNullEntries(Matrix m);
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
This constructor is similar with [`Matrix(int, int)`](#cr_matrix-2) but with an additional
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
    { 1, 2, 3 },
    { 4, 5, 6 }
};

// Convert to Matrix
Matrix m = new Matrix(a);
```

Or you can do the same thing with this code:

```java
// Create new matrix
Matrix m = new Matrix(new double[][] {
    { 1, 2, 3 },
    { 4, 5, 6 }
});
```

### <a name="cr_matrix-5"></a> Matrix.identity\(int)
This constructor would creates new **identity matrix** with size `nxn` (the `n` is from input).  
**Identity matrix** itself is a square matrix with ones on the main diagonal and zeros elsewhere. And the **identity matrix** is often denoted by **`I`**.  
> **Note** In code you should avoid using neither **`I`** or **`i`** for the **Matrix**'s variable name, it because **`i`** is often used by `for-loop` statement.  
> Instead you can use **`mI`** or similar with it.  

For example:

```java
// Create new identity matrix with size 5x5
Matrix mI = Matrix.identity(5);

mI.display();
```

That should output like this:

```
[   [1.0, 0.0, 0.0, 0.0, 0.0],
    [0.0, 1.0, 0.0, 0.0, 0.0],
    [0.0, 0.0, 1.0, 0.0, 0.0],
    [0.0, 0.0, 0.0, 1.0, 0.0],
    [0.0, 0.0, 0.0, 0.0, 1.0]   ]
```


## <a name="matrix-ops"></a> Matrix Operations

There are some basic matrix operations you can do with **JMatrix**, here's the list:
- [Addition](#matrix-add)
- [Subtraction](#matrix-sub)
- [Scalar Multiplication](#scalar-mult)
- [Matrix Multiplication](#matrix-mult)
- [Transposition](#matrix-transpose)

Please refer to [:books: JMatrix Wikis](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-matrix-operations), for more detailed about matrix operations.

### <a name="matrix-add"></a> Addition

:book: **Wiki:** [Matrix Addition](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-addition)

> **Note** Ensure the two matrices are same dimensions before operating **addition**.

**Example code:**
```java
// Construct new matrices
Matrix m = new Matrix(new double[][] {
    { 6, 7, 0, 1 },
    { 2, 6, 1, 8 }
});

Matrix n = new Matrix(new double[][] {
    { 1, 9, 3, -4 },
    { 7, 1, -1, 5 }
});

// Operate addition and display immediately
Matrix.sum(m, n).display();
```

**Output:**
```
[   [7.0, 16.0, 3.0, -3.0],
    [9.0, 7.0, 0.0, 13.0]   ]
```

### <a name="matrix-sub"></a> Subtraction

:book: **Wiki:** [Matrix Subtraction](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-subtraction)
  
> **Note** Ensure the two matrices are same dimensions before operating subtraction.

**Example code:**
```java
// Construct new matrices
Matrix m = new Matrix(new double[][] {
    { 1, -5, 8, -2, 3 },
    { 2, 12, -2, 7, 0 },
    { 6, 7, 9, 1, -5 }
});

Matrix n = new Matrix(new double[][] {
    { 4, 6, 8, -3, 9 },
    { -10, 6, 8, 1, 1 },
    { 6, -7, 2, 3, 5 }
});

// Operate subtraction and display immediately
Matrix.sub(m, n).display();
```

**Output:**
```
[   [-3.0, -11.0, 0.0, 1.0, -6.0],
    [12.0, 6.0, -10.0, 6.0, -1.0],
    [0.0, 14.0, 7.0, -2.0, -10.0]   ]
```

### <a name="scalar-mult"></a> Scalar Multiplication

:book: **Wiki:** [Scalar Multiplication](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-multiplication-by-a-scalar)

**Example code:**
```java
// Create new matrix
Matrix m = new Matrix(new double[][] {
    { 9, 6, 4 },
    { 2, 1, 5 }
});

// Operate scalar multiplication and
// display immediately
Matrix.mult(m, 5).display();
```

**Output:**
```
[   [45.0, 30.0, 20.0],
    [10.0, 5.0, 25.0]   ]
```

### <a name="matrix-mult"></a> Matrix Multiplication

:book: **Wiki:** [Matrix Multiplication](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-multiplication-of-two-matrices)

> **Note** Ensure the number of [columns][matrix-col] in the first matrix and the number of [rows][matrix-row] in the second matrix are equal before operating matrix multiplication.

**Example code:**
```java
// Create and construct new matrices
Matrix m = new Matrix(new double[][] {
    { 2, 2, 2, 2 },
    { 2, 2, 2, 2 }
});

Matrix n = new Matrix(new double[][] {
    { 4, 4 },
    { 4, 4 },
    { 4, 4 },
    { 4, 4 }
});

// Operate matrix multiplication and
// display immediately
Matrix.mult(m, n).display();
```

**Output:**
```
[   [32.0, 32.0],
    [32.0, 32.0]   ]
```

### <a name="matrix-transpose"></a> Transposition

:book: **Wiki:** [Matrix Transposition](https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#-transposition)

**Example code:**
```java
// Create and construct new matrix
Matrix m = new Matrix (new double[][] {
    { 1, 2, 3, 4 },
    { 5, 6, 7, 8 }
});

// Transpose the matrix and display immediately
Matrix.transpose(m).display();
```

**Output:**
```
[   [1.0, 5.0],
    [2.0, 6.0],
    [3.0, 7.0],
    [4.0, 8.0]   ]
```


## <a name="author"></a> Author
[Ryuu Mitsuki](https://github.com/mitsuki31)

## <a name="license"></a> License
**JMatrix** is under licensed [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0), for more details you can check it on [LICENSE](https://github.com/mitsuki31/jmatrix/blob/master/LICENSE) file.


<!-- Shortcut -->
[java]: https://www.oracle.com/java/technologies/downloads
[jmatrix]: https://github.com/mitsuki31/jmatrix.git
[what-is-matrix]: https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix
[matrix-row]: https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#matrix-row
[matrix-col]: https://github.com/mitsuki31/jmatrix/wiki/About%20Matrix#matrix-column
