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

<!-- TABLE OF CONTENTS::START -->

<details>
<summary>
  <h4><a name="table-of-contents" />Table of Contents</h4>
</summary>

- [🪐 About JMatrix](#about-jmatrix)
- [🔌 Installation](#installation)
    * [🎯 Prerequisites](#prerequisites)
      + [For Normal Use](#normal-use)
      + [For Building the Project](#build-use)
- [Constructor Summary](#constructor-summary)
    * [`Matrix()`](#cr_matrix-1)
    * [`Matrix(int, int)`](#cr_matrix-2)
    * [`Matrix(int, int, int)`](#cr_matrix-3)
    * [`Matrix(double[][])`](#cr_matrix-4)
    * [`Matrix.identity(int)`](#cr_matrix-5)
- [Matrix Operations](#matrix-ops)
    * [Addition](#matrix-add)
    * [Subtraction](#matrix-sub)
    * [Scalar Multiplication](#scalar-mult)
    * [Matrix Multiplication](#matrix-mult)
    * [Transposition](#matrix-transpose)
- [Author](#author)
- [Contributing](#contributing)
- [License](#license)
</details>

<!-- TABLE OF CONTENTS::END -->

## <a name="about-jmatrix" /> 🪐 About JMatrix

**JMatrix** is a lightweight [Java][java-wiki] library that provides essential matrix operations like addition, subtraction, multiplication, and determinant calculation. Designed with simplicity in mind, it is perfect for educational purposes and small-scale projects involving [linear algebra][linear-algebra-wiki]. The library also supports custom matrix sizes and efficient handling of matrix operations for both square and rectangular matrices.

> [!IMPORTANT]  
> This project was intended for educational purposes only. It is not recommended for use in large-scale projects.

**JMatrix** provides following basic matrix operations:

- [Addition][matrix-add]
- [Subtraction][matrix-sub]
- [Multiplication][matrix-mult]
- [Transposition][matrix-transpose]
- [Trace](https://en.wikipedia.org/wiki/Trace_(linear_algebra))
- [Determinant](https://en.wikipedia.org/wiki/Determinant)

In addition to the fundamental matrix operations, **JMatrix** also includes matrix type checkers, allowing users to identify certain characteristics of matrices:

- `isDiagonal`          - Check whether the matrix is [diagonal]( https://en.wikipedia.org/wiki/Diagonal_matrix).
- `isSquare`            - Check whether the matrix is [square](https://en.wikipedia.org/wiki/Square_matrix).
- `isLowerTriangular`   - Check whether the matrix is [lower triangular](https://en.wikipedia.org/wiki/Triangular_matrix).
- `isUpperTriangular`   - Check whether the matrix is [upper triangular](https://en.wikipedia.org/wiki/Triangular_matrix).
- `isPermutationMatrix` - Check whether the matrix is [permutation matrix](https://en.wikipedia.org/wiki/Permutation_matrix).


<details>
<summary><b>What is Matrix?</b></summary>

> Matrices are rectangular arrays of numbers or symbols, arranged in [rows][matrix-row] and [columns][matrix-col].  
> They are widely used in various fields, including mathematics, physics, computer science, and engineering.  
> Matrices provide a concise and organized way to represent and manipulate data.  
>
> Refer to [:books: JMatrix Wiki][what-is-matrix], if want to know about matrix with simplified informations.
</details>

---

## <a name="installation" /> 🔌 Installation

If you are interested in obtaining the latest stable version of the project, please check the [latest version][latest-ver]. You can download the archived package containing compiled classes from there.

For improved stability and better usability, we highly recommend downloading the archived package that also includes the source files (`jmatrix-<VERSION>_with_sources.jar`).
This package contains all the necessary documentation about classes, methods, and other aspects related to **JMatrix**, making it easier to explore and understand the library APIs.

> [!WARNING]  
> Currently, there is an issue with pre-build processes when using **Make** on Windows systems. The issue was that it failed while trying to create child processes to configure the build requirements.
> 
> For better functionality, we recommend using [**Maven**][maven] instead. Because using **Make** is an alternative way for flexibility on UNIX systems.

### <a name="prerequisites" /> 🎯 Prerequisites

#### <a name="normal-use" /> For Normal Use

To use **JMatrix** in your project, you will need the following prerequisites:

- [**Java**][java] *(min. JDK 11, recommended is JDK 17 and later)*
- [**Git Bash**][git-bash] *(optional, Windows only)*

#### <a name="build-use" /> For Building the Project

If you plan to build the **JMatrix** project, please ensure you have the following prerequisites:

- [**Java**][java] *(min. JDK 11, recommended is JDK 17 and later)*.
- [**Python**][python] *(min. version 3.7, recommended is 3.11 and later)*.
- Latest version of [**Make**][mingw] or [**Maven**][maven].

> [!IMPORTANT]  
> If you choose to build the project using [Maven][maven], you don't need [Python][python].
> 
> Sadly, we're having a problem with pre-build processes on Windows systems. It is recommended to use [Maven][maven] instead if you're using Windows to build the project, ensuring better functionality.

For more detailed instructions on building the project, you can refer to the [:bookmark:Getting Started][get-started] page.

Once you have the necessary prerequisites, you can start exploring and using **JMatrix** in your projects. The documentation included in the archived package will guide you through the classes, methods, and functionalities offered by the library.

---

## <a name="constructor-summary" /> Constructor Summary

There are five constructors available for constructing matrices in the **JMatrix** library. Each constructor serves a specific purpose, providing users with flexibility and ease of use.

> [!NOTE]  
> If you are unfamiliar with matrices or need a refresher, you can check the [:bookmark:Introduction to Matrix][what-is-matrix] page to gain a basic understanding before delving into matrix constructors.

### <a name="cr_matrix-1" /> `Matrix()`

```java
public Matrix();
```

This constructor does not require any arguments and constructs a Matrix with null entries, resulting in a null matrix. A null matrix cannot perform any operations until it is initialized with valid elements. For example:

```java
// Create a null entries matrix
Matrix m = new Matrix();
```

> [!NOTE]  
> Do not confuse the **null matrix** with the **zero matrix**.
> A **null matrix** has null entries, whereas the **zero matrix** contains all elements as zeros.

> <details><summary><b>Examples</b></summary>
>
> #### Zero Matrix
>
> ```math
> \begin{bmatrix}
>   0.0 & 0.0 & 0.0 \\
>   0.0 & 0.0 & 0.0
> \end{bmatrix}_{2 \times 3}
> ```
>
> #### Null Matrix
>
> ```console
> null
> ```
>
> Yes, that is a **null matrix**. It has none or null entries inside the matrix. The output above is the result of this code below:
>
> ```java
> Matrix nullM = new Matrix();
> System.out.println(
>     (nullM.getEntries() == null) ? nullM.getEntries() : nullM.toString());
> ```
>
> Also if you use either the `display()` or `prettyDisplay()` method from null matrix, this output will be printed.
>
> ```console
> <null_matrix>
> ```
>
> </details> 


### <a name="cr_matrix-2" /> `Matrix(int, int)`

```java
public Matrix(int rows, int cols);
```

With this constructor, you can create a **zero matrix** with ease by providing two arguments: the number of [rows][matrix-row] and [columns][matrix-col]. A **zero matrix** contains all elements as zeros. For example:

```java
// Create null matrix with size 3x4
Matrix m = new Matrix(3, 4);
```

The code above constructs a new **zero matrix** with size $3 \times 4$. The matrix will looks like this:

```math
\begin{bmatrix}
  0.0 & 0.0 & 0.0 & 0.0 \\
  0.0 & 0.0 & 0.0 & 0.0 \\
  0.0 & 0.0 & 0.0 & 0.0
\end{bmatrix}
```

### <a name="cr_matrix-3" /> `Matrix(int, int, double)`

```java
public Matrix(int rows, int cols, double val);
```

This constructor is similar to [`Matrix(int, int)`](#cr_matrix-2) but with an additional argument that sets the value for all elements of the constructed matrix. For example:

```java
// Create a new matrix with size 4x4 and set all elements to 5.0
Matrix m = new Matrix(4, 4, 5);
```

The constructed matrix will looks like this:

```math
\begin{bmatrix}
  5.0 & 5.0 & 5.0 & 5.0 \\
  5.0 & 5.0 & 5.0 & 5.0 \\
  5.0 & 5.0 & 5.0 & 5.0 \\
  5.0 & 5.0 & 5.0 & 5.0
\end{bmatrix}
```

### <a name="cr_matrix-4" /> `Matrix(double[][])`

```java
public Matrix(double[][] arr);
```

This constructor is **highly recommended** for constructing a new [matrix][what-is-matrix]. You can declare the entries first and then convert them into a **Matrix** object whenever needed.

> [!NOTE]  
> Please note, this constructor only accepts two-dimensional array with type of `double`.

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

Alternatively, you can directly create a new matrix using this code:

```java
// Create new matrix
Matrix m = new Matrix(new double[][] {
    { 1, 2, 3 },
    { 4, 5, 6 }
});
```

### <a name="cr_matrix-5" /> `Matrix.identity(int)`

```java
public static Matrix identity(int n);
```

This constructor creates a new **identity matrix** with a size of $n \times n$ (where $n$ is a non-floating number). An **identity matrix** is a square matrix with ones on the main diagonal and zeros elsewhere, often denoted as $I$.

Please avoid using $I$ or $i$ as variable names for matrices in code, as $i$ is commonly used in `for-loop` statements. Instead, consider using $mI$ or a similar alternative. For instance:

```java
// Create new identity matrix with size 5x5
Matrix mI = Matrix.identity(5);
```

The matrix will looks like this:

```math
\begin{bmatrix}
  1.0 & 0.0 & 0.0 & 0.0 & 0.0 \\
  0.0 & 1.0 & 0.0 & 0.0 & 0.0 \\
  0.0 & 0.0 & 1.0 & 0.0 & 0.0 \\
  0.0 & 0.0 & 0.0 & 1.0 & 0.0 \\
  0.0 & 0.0 & 0.0 & 0.0 & 1.0
\end{bmatrix}
```

## <a name="matrix-ops" /> Matrix Operations

The **JMatrix** library provides several basic matrix operations that allow users to perform common matrix calculations with ease. These operations include:

- [Addition](#matrix-add)
- [Subtraction](#matrix-sub)
- [Scalar Multiplication](#scalar-mult)
- [Matrix Multiplication](#matrix-mult)
- [Transposition](#matrix-transpose)
- [Trace](https://en.wikipedia.org/wiki/Trace_(linear_algebra))
- [Determinant](https://en.wikipedia.org/wiki/Determinant)

For more detailed information about each matrix operation, you can refer to the [:books: JMatrix Wiki][matrix-ops].

### <a name="matrix-add" /> Addition

```java
public void sum(Matrix m);
```
```java
public static Matrix sum(Matrix m, Matrix n);
```
```java
public void sum(double[][] a);
```
```java
public static double[][] sum(double[][] a, double[][] b);
```

:book: **Wiki:** [Matrix Addition][matrix-add]

In **matrix addition**, two matrices with the same dimensions are added together element-wise. Each element of the resulting matrix is the sum of the corresponding elements from the two input matrices.

> [!IMPORTANT]  
> Before performing **matrix addition**, ensure that the two matrices have the same dimensions (in other words, square matrix).

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

// Perform addition for both matrices and
// create a new matrix as the resultant matrix
Matrix k = Matrix.sum(m, n);
```

**Result:**

```math
\mathbf{k} =
\begin{bmatrix}
  7.0 & 16.0 & 3.0 & -3.0 \\
  9.0 & 7.0 & 0.0 & 13.0
\end{bmatrix}
```

In the example above, two matrices $m$ and $n$ are created. The `Matrix.sum(m, n)` method is used to add both matrices element-wise, and the resulting matrix $k$ is computed and stored. The output matrix $k$ is the sum of matrices $m$ and $n$.

### <a name="matrix-sub" /> Subtraction

```java
public void sub(Matrix m);
```
```java
public static Matrix sub(Matrix m, Matrix n);
```
```java
public void sub(double[][] a);
```
```java
public static double[][] sub(double[][] a, double[][] b);
```

:book: **Wiki:** [Matrix Subtraction][matrix-sub]

**Matrix subtraction** involves subtracting corresponding elements of one matrix from another.

> [!IMPORTANT]  
> Before performing **matrix subtraction**, ensure that the two matrices have the same dimensions (in other words, square matrix).

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

// Perform subtraction for both matrices and
// create a new matrix as the resultant matrix
Matrix k = Matrix.sub(m, n);
```

**Result:**

```math
\mathbf{k} =
\begin{bmatrix}
  -3.0 & -11.0 & 0.0 & 1.0 & -6.0 \\
  12.0 & 6.0 & -10.0 & 6.0 & -1.0 \\
  0.0 & 14.0 & 7.0 & -2.0 & -10.0
\end{bmatrix}
```

In the example above, two matrices $m$ and $n$ are created. The `Matrix.sub(m, n)` method is used to subtract $n$ from $m$ element-wise, and the resulting matrix $k$ is computed and stored. The output matrix $k$ is the difference between matrices $m$ and $n$.

### <a name="scalar-mult" /> Scalar Multiplication

```java
public void mult(double x);
```
```java
public static Matrix mult(Matrix m, double x);
```

:book: **Wiki:** [Scalar Multiplication][scalar-mult]

**Scalar multiplication** involves multiplying all elements of a matrix by a scalar value. The resulting matrix will have each of its elements multiplied by the given scalar value.

> [!NOTE]  
> The resulting matrix's sizes of **scalar multiplication** will be always the same with the sizes of the operand matrix.

**Example code:**

```java
// Construct new matrix
Matrix m = new Matrix(new double[][] {
    { 9, 6, 4 },
    { 2, 1, 5 }
});

// Perform scalar multiplication with the scalar equal to 5
// and create a new matrix as the resultant matrix
Matrix s = Matrix.mult(m, 5);
```

**Result:**

```math
\mathbf{s} =
\begin{bmatrix}
  45.0 & 30.0 & 20.0 \\
  10.0 & 5.0 & 25.0
\end{bmatrix}
```

In the example above, a matrix $m$ is created. The `Matrix.mult(m, 5)` method is used to multiply each element of matrix $m$ by the scalar value $5$, resulting in a new matrix $s$.

### <a name="matrix-mult" /> Matrix Multiplication

```java
public void mult(Matrix m);
```
```java
public static Matrix mult(Matrix m, Matrix n);
```
```java
public void mult(double[][] a);
```
```java
public static double[][] mult(double[][] a, double[][] b);
```

:book: **Wiki:** [Matrix Multiplication][matrix-mult]

**Matrix multiplication** involves multiplying two matrices together following a specific rule.

> [!IMPORTANT]  
> Before performing **matrix multiplication**, ensure the number of [columns][matrix-col] in the first matrix must be equal to the number of [rows][matrix-row] in the second matrix.

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

// Operate matrix multiplication for both matrices
// and create a new matrix as the resultant matrix
Matrix mn = Matrix.mult(m, n);
```

**Result:**
```math
\mathbf{mn} =
\begin{bmatrix}
  32.0 & 32.0 \\
  32.0 & 32.0
\end{bmatrix}
```

In the example above, two matrices $m$ and $n$ are created. The `Matrix.mult(m, n)` method is used to perform **matrix multiplication** between matrices $m$ and $n$, resulting in a new matrix $mn$.


### <a name="matrix-transpose" /> Transposition

```java
public void transpose();
```
```java
public static Matrix transpose(Matrix m);
```
```java
public static double[][] transpose(double[][] a);
```

:book: **Wiki:** [Matrix Transposition][matrix-transpose]

**Matrix transposition** involves swapping the [rows][matrix-row] and [columns][matrix-col] of a matrix. The resulting matrix will have its rows and columns interchanged.

> [!NOTE]  
> 💡 **Tip:** Repeating this operation to the transposed matrix will reset their indices position to the original position.

**Example code:**

```java
// Create and construct new matrix
Matrix m = new Matrix (new double[][] {
    { 1, 2, 3, 4 },
    { 5, 6, 7, 8 }
});

// Transpose the matrix and create a new matrix
// to store the transposed matrix
Matrix mT = Matrix.transpose(m);
```

**Result:**

```math
\mathbf{mT} =
\begin{bmatrix}
  1.0 & 5.0 \\
  2.0 & 6.0 \\
  3.0 & 7.0 \\
  4.0 & 8.0
\end{bmatrix}
```

In the example above, a matrix $m$ is created. The `Matrix.transpose(m)` method is used to transpose matrix $m$, resulting in a new matrix $mT$ with the [rows][matrix-row] and [columns][matrix-col] interchanged.


## <a name="author" /> Author

**JMatrix** is authored and maintained by [Ryuu Mitsuki][mitsuki31].

## <a name="contributing" /> Contributing

Please feel free to contribute to this project if you know of a problematic algorithm based on linear algebra concepts or if you wish to make any existing algorithm better. Any contributions is very appreciated and greatly helped me.

## <a name="license" /> License

**JMatrix** is licensed under the [Apache License 2.0][apache-2.0]. This license permits you to use, modify, distribute, and sublicense the software, subject to certain conditions.

You are free to use **JMatrix** for both commercial and non-commercial purposes. If you modify the software, you must clearly indicate the changes you made. Any contributions you make to the project are also subject to the same license terms.

The [Apache License 2.0][apache-2.0] allows you to distribute derivative works, but you must include the full text of the license in your distribution. Additionally, you are responsible for ensuring that any downstream recipients of the software are aware of its licensing terms.

For more details about the permissions, limitations, and conditions under which **JMatrix** is licensed, please refer to the [LICENSE][license] file provided with the project. The [LICENSE][license] file contains the complete text of "The License", ensuring full transparency and clarity regarding the terms of use for the software.

By using **JMatrix**, you agree to comply with the terms of the [Apache License 2.0][apache-2.0] and understand your rights and responsibilities as a user of this open-source software.


[mitsuki31]: https://github.com/mitsuki31
[jmatrix]: https://github.com/mitsuki31/jmatrix
[license]: https://github.com/mitsuki31/jmatrix/blob/master/LICENSE
[latest-ver]: https://github.com/mitsuki31/jmatrix/releases/latest


<!-- JMATRIX WIKI LINKS -->
[get-started]: https://github.com/mitsuki31/jmatrix/wiki/Getting-Started
[what-is-matrix]: https://github.com/mitsuki31/jmatrix/wiki/Introduction-to-Matrix
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
