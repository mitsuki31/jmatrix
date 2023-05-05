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
> JMatrix is a matrix builder written in Java.<br>
> Can creates the matrix, operates addition, subtraction, multiplication and clear the matrix array *(other operations will be added soon)*.<br>

<details>
<summary><h3><a name="list-of-contents"></a>List of Contents</h3>
</summary>

- [How to Build the Project?](README.md#build-project)
    - [Build using Make](README.md#make-build)
        - [Compile all source files](README.md#make-compile)
        - [Create a new jar file](#make-package)
    - [Build using Maven](#maven-build)
        - [Compile all source files](#maven-compile)
        - [Create a new jar file](#maven-package)
- [Authors](#authors)
- [License](#license)
</details>

## <a name="build-project"></a> How to Build the Project?

### <a name="make-build"></a> Build using Make
1. <a name="make-compile"></a> Compile all source files
    ```bash
    make compile
    ```

2. <a name="make-package"></a> Create a new jar file
    ```bash
    make package
    ```
<br>

> Build the project with one line command.
> ```bash
> make compile package
> ```


### <a name="maven-build"></a> Build using Maven
1. <a name="maven-compile"></a> Compile all source files
    > **Note** `Maven` will start downloading all required dependencies for the project *(if not found)*.<br>
    > So be patient, and take a coffee :coffee:.
    ```bash
    mvn compile
    ```

2. <a name="maven-package"></a> Create a new jar file
   ```bash
   mvn package
   ```
<br>

> Build the project with one line command.
> ```bash
> mvn package
> ```
> Or
> ```bash
> mvn compile package
> ```

---

The output directory for both builder (`Make` and `Maven`) is `"target/"` directory.<br>
See [#29](https://github.com/mitsuki31/jmatrix/pull/29).

## <a name="authors"></a> Authors
[Ryuu Mitsuki](https://github.com/mitsuki31)

## <a name="license"></a> :balance_scale: License
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
