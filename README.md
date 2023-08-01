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


**Developed by one person ([Ryuu Mitsuki][mitsuki31])** :fire:  

**JMatrix** is an educational [Java][java-wiki] library and designed to facilitate and simplify [matrix operations][matrix-ops].  
It offers a range of intuitive methods to perform common matrix operations with ease, making it an ideal learning tool for high school students exploring [linear algebra][linear-algebra-wiki] concepts.
> **Note** This project is currently in development and is intended for educational purposes only.  
> It is not recommended for use in large-scale projects or production environments.

**JMatrix** provides following basic matrix operations:

- [Addition][matrix-add]
- [Subtraction][matrix-sub]
- [Multiplication][mult-matrices]
- [Transposition][transpose-matrix]
- *(additional matrix operations will be added in the future)*

In addition to the fundamental matrix operations, **JMatrix** also includes matrix type checkers, allowing students or users to identify certain characteristics of matrices:

- `isDiagonal`        - Check whether the matrix is diagonal.
- `isSquare`          - Check whether the matrix is square.
- `isLowerTriangular` - Check whether the matrix is lower triangular.
- `isUpperTriangular` - Check whether the matrix is upper triangular.
- *(more matrix type checkers will be added in the future)*.


<details>
<summary><b>What is Matrix?</b></summary>

> Matrices are rectangular arrays of numbers or symbols, arranged in [rows][matrix-row] and [columns][matrix-col].  
> They are widely used in various fields, including mathematics, physics, computer science, and engineering.  
> Matrices provide a concise and organized way to represent and manipulate data.  
>
> Refer to [:books: JMatrix Wikis][what-is-matrix], if want to know about matrix with simplified informations.
</details>

---

If you are looking for the latest stable version of the project, please check the [latest version][latest-ver]. You can download the archived package containing compiled classes from there.

For improved stability and better usability, we highly recommend downloading the archived package that also includes the source files.
This package contains all the necessary documentation about classes, methods, and other aspects related to **JMatrix**, making it easier to explore and understand the project.

**Prerequisites (For Normal Use):**
To use **JMatrix** in your project, you will need the following prerequisites:

- [**Java**][java] *(minimum version 11, recommended is 17)*
- [**Git Bash**][git-bash] *(optional, but recommended)*

**Prerequisites (For Build the Project):**
If you plan to build the **JMatrix** project, please ensure you have the following prerequisites:

- [**Java**][java] *(minimum version 11, recommended is 17)*.
- [**Python**][python] *(minimum version 3.7, recommended is 3.11)*.
- Latest version of [**Make/MinGW**][mingw] or [**Maven**][maven].
- [**Git Bash**][git-bash].  
> **Note** If you choose to build the project using [Maven][maven], you don't need to install [Python][python].  
>
> For a smoother building experience using [Make][mingw], we highly recommend using [Git Bash][git-bash] as the shell environment.
> The `Makefile` utilizes [Bash][bash-wiki], making it more compatible with [Git Bash][git-bash].  
> For more information about build the project, you can refer to [:bookmark:Getting Started][get-started] page.

Once you have the necessary prerequisites, you can start exploring and using **JMatrix** in your projects. The documentation included in the archived package will guide you through the classes, methods, and functionalities offered by the library.

---

<details>
<summary><b><a name="table-of-contents"></a>Table of Contents</b>
</summary>

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

## <a name="constructor-summary"></a> Constructor Summary
There are 5 constructors that can be used for constructing the matrix.  
> :man::question: Still don't understand about matrix? Check the [:bookmark:About Matrix][what-is-matrix] section
> to get little knowledge about matrix before dive into matrix constructor.

### <a name="cr_matrix-1"></a> Matrix()
This constructor doesn't need any arguments, but it would constructs the **Matrix**
with `null` entries or can be called **null matrix**. In this case, the matrix itself can't do any operations until
get initialized and have valid elements. For example:

```java
// Create null entries matrix
Matrix m = new Matrix();
```

> **Note** Don't be confused with **null matrix** and **zero matrix**.  
> **Null matrix** is a matrix that has `null` entries, whereas the **zero matrix** is a matrix with all elements are zero.
>
> <details><summary><b>Examples</b></summary>
>
> ### Zero Matrix
>
> ```math
> \begin{bmatrix}
>   0.0 & 0.0 & 0.0
>   0.0 & 0.0 & 0.0
> \end{bmatrix}_{2 \times 3}
> ```
>
> ### Null Matrix
>
> ```java
> null
> ```
>
> Yes, that is **null matrix**. It has none or `null` entries inside it. The output above is the result of this code below:
>
> ```java
> Matrix nullM = new Matrix();
> System.out.println(
>     (nullM.getEntries() == null) ? nullM.getEntries() : nullM.toString());
> ```

</details> 


### <a name="cr_matrix-2"></a> Matrix(int, int)
How to create **zero matrix**? Don't worry, with this constructor you can construct **zero matrix** with ease.
With just two arguments, each for size of [row][matrix-row] and [column][matrix-col].  
The matrix can be called **zero matrix** if all elements inside the matrix is zero.
For example:

```java
// Create null matrix with size 3x4
Matrix m = new Matrix(3, 4);
```

The code above would constructs a new **zero matrix** with size $3 \times 4$. The matrix should looks like this:

```math
\begin{bmatrix}
  0.0 & 0.0 & 0.0 & 0.0 \\
  0.0 & 0.0 & 0.0 & 0.0 \\
  0.0 & 0.0 & 0.0 & 0.0
\end{bmatrix}
```

### <a name="cr_matrix-3"></a> Matrix(int, int, int)
This constructor is similar with [`Matrix(int, int)`](#cr_matrix-2) but with an additional
argument which is the value to filled out the entire elements of constructed matrix. For example:

```java
// Create new matrix with size 4x4 and 5 as default elements
Matrix m = new Matrix(4, 4, 5);
```

The matrix constructed above should output the same like this:

```math
\begin{bmatrix}
  5.0 & 5.0 & 5.0 & 5.0 \\
  5.0 & 5.0 & 5.0 & 5.0 \\
  5.0 & 5.0 & 5.0 & 5.0 \\
  5.0 & 5.0 & 5.0 & 5.0
\end{bmatrix}
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
This constructor would creates new **identity matrix** with size $n \times n$ (the $n$ is from input).  
**Identity matrix** itself is a square matrix with ones on the main diagonal and zeros elsewhere. And the **identity matrix** is often denoted by $I$.  
> **Note** In code you should avoid using neither $I$ or $i$ for the **Matrix**'s variable name, it because $i$ is often used by `for-loop` statement.  
> Instead you can use $mI$ or similar with it.  

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

Please refer to [:books: JMatrix Wikis][matrix-ops], for more detailed about matrix operations.

### <a name="matrix-add"></a> Addition

:book: **Wiki:** [Matrix Addition][matrix-add]

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

:book: **Wiki:** [Matrix Subtraction][matrix-sub]

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

:book: **Wiki:** [Scalar Multiplication][scalar-mult]

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

:book: **Wiki:** [Matrix Multiplication][matrix-mult]

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

:book: **Wiki:** [Matrix Transposition][matrix-tranpose]

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
[Ryuu Mitsuki][mitsuki31]

## <a name="license"></a> License
**JMatrix** is under licensed [Apache License 2.0][apache-2.0], for more details you can check it on [LICENSE][license] file.



[mitsuki31]: https://github.com/mitsuki31
[jmatrix]: https://github.com/mitsuki31/jmatrix.git
[license]: https://github.com/mitsuki31/jmatrix/blob/master/LICENSE
[latest-ver]: https://github.com/mitsuki31/jmatrix/releases/latest


<!-- JMATRIX WIKI LINKS -->
[get-started]: https://github.com/mitsuki31/jmatrix/wiki/Getting-Started
[what-is-matrix]: https://github.com/mitsuki31/jmatrix/wiki/Introduction-to-Matrix#what-matrix
[matrix-row]: https://github.com/mitsuki31/jmatrix/wiki/Introduction-to-Matrix#matrix-row
[matrix-col]: https://github.com/mitsuki31/jmatrix/wiki/Introduction-to-Matrix#matrix-column

[matrix-ops]: https://github.com/mitsuki31/jmatrix/wiki/Matrix-Operations
[matrix-add]: https://github.com/mitsuki31/jmatrix/wiki/Matrix-Operations#matrix-add
[matrix-sub]: https://github.com/mitsuki31/jmatrix/wiki/Matrix-Operations#matrix-sub
[scalar-mult]: https://github.com/mitsuki31/jmatrix/wiki/Matrix-Operations#mult-scalar
[matrix-mult]: https://github.com/mitsuki31/jmatrix/wiki/Matrix-Operations#mult-matrices
[matrix-transpose]: https://github.com/mitsuki31/jmatrix/wiki/Matrix-Operations#transpose-matrix


<!-- WIKIPEDIA LINKS -->
[bash-wiki]: https://en.m.wikipedia.org/wiki/Bash_(Unix_shell)
[java-wiki]: https://en.m.wikipedia.org/wiki/Java_(programming_language)
[linear-algebra-wiki]: https://en.m.wikipedia.org/wiki/Linear_algebra


<!-- OTHER LINKS -->
[java]: https://www.oracle.com/java/technologies/downloads
[apache-2.0]: https://www.apache.org/licenses/LICENSE-2.0
[python]: https://www.python.org
[mingw]: https://www.mingw-w64.org/downloads
[maven]: https://maven.apache.org/download.cgi
[git-bash]: https://git-scm.com/downloads
