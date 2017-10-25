# RepositoryMiner

RepositoryMiner is a Java framework that helps developers and researchers to mining software repositories. With RepositoryMiner, you can easily extract informations from a software repository (e.g. Git), such as commits, developers, modifications, source code, and software metrics. All the informations extracted are stored in the MongoDB for futher analysis/mining over these data.

Take a look at our wiki and [talk to us](https://groups.google.com/forum/#!forum/visminer).

# What I need to run?

To run the RepositoryMiner you will ony need Java (7 or above) and MongoDB (3 or above) installed.

# Getting Started

Currently the project is not in the maven repositories. So you will need to download the java project. The project is build  using gradle, so you can choose your environment freely.

After downloaded and configured you can create your own jar to use in your project. Soon as possible we will provide the project in the maven repositories.

We recommend to always use the version in the master branch for newest updates and bug fixes.

The project is compound by various others projects. In this file we will show you how to execute each one of our projects. 

The API is very simple and straight foward. After added our project as dependency you only need write a little  of java code to extract the data from the software repository. 

Below we show how execute the modules, take a  look at our wiki too. There we provide some explanations and interesting link about the tool.

# How do I cite RepositoryMiner?
```
@INPROCEEDINGS{170925,
    AUTHOR="Thiago Mendes and Renato Novais and Manoel Mendonca and Luis Carvalho and Felipe Gomes",
    TITLE="RepositoryMiner - uma ferramenta extensível de mineração de repositórios de software para identificação automática de Dívida Técnica",
    BOOKTITLE="CBSoft 2017 - Sessao de Ferramentas () ",
    ADDRESS="",
    DAYS="18-22",
    MONTH="sep",
    YEAR="2017",
    ABSTRACT="Software projects commonly incur in Technical Debt, since small amounts of debt can increase staff productivity in a short term. However, their presence brings risks to the project, making difficult the management as well as reducing staff productivity and project quality. It is possible to identify some of these technical debts through the calculation of metrics and detecting code smells. But, the existing tools only allow the identification of isolated types of metrics and analyze only one version of the software at a time. In this context, this work presents the RepositoryMiner (RM) tool. RM analyzes software repositories, collecting and combining various data related to software, allowing the calculation of software metrics and identification of different types of Technical Debt. It supports software engineers on the identification and monitoring of Technical Debt.",
    KEYWORDS="Manutenção, Reengenharia e Refatoração de Software; Métricas e Medições em Engenharia de Software", 
    URL="http://XXXXX/170925.pdf"
} 
```

# Is there a discussion forum?

You can subscribe to our mailing list: https://groups.google.com/forum/#!forum/visminer.

# How I contribute?

Check the page [How to contribute](https://github.com/visminer/RepositoryMiner/wiki/How-to-contribute)

# License

This software is licensed under the Apache 2.0 License.
