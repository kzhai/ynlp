ynlp
====

Please send any bugs reports or questions to Ke Zhai (kzhai@yahoo-inc.com).

Getting Started
---------------

Clone the repo:

```
git clone git@github.com:kzhai/ynlp.git
```

Then build using the standard invocation:

```
mvn clean package
```

If you want to set up your Eclipse environment:

```
mvn eclipse:clean
mvn eclipse:eclipse
```

If you want to download all the dependency jar files:

```
mvn dependency:copy-dependencies -DoutputDirectory=lib
```