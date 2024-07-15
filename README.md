## tst-exercises

-[setup](#setup)

-[usage](#Usage)


## setup

This application uses Scala 3.4.2 and sbt to build it module. To work with it locally 

1. Install sbt

2. Check out the project in a directory.
 
```shell
  cd ~/<directory>  
  git clone https://github.com/rachnasha/tst.git
```


## usage

1. Compile - go to the project and run sbt compile

```shell
   cd ~/<directory>/tst-exercises
   sbt compile
```

2. To run unit tests

```shell
   cd ~/<directory>/tst-exercises
   sbt test
```

3. Run the app

```shell
   cd ~/<directory>/tst-exercises
   sbt tst-exercises/run
```
 - output from run

```shell
[info] compiling 4 Scala sources to C:\Users\rachn\practice\tst-exercises\target\scala-3.4.2\classes ...
[info] running run
Best Rates:
BestGroupPrice(CB,S1,245.0,Senior)
BestGroupPrice(CA,S1,225.0,Senior)
BestGroupPrice(CB,M1,230.0,Military)
BestGroupPrice(CA,M1,200.0,Military)
-----------
All Combinations:
PromotionCombo(List(P3, P4, P5))
PromotionCombo(List(P5, P1, P4))
PromotionCombo(List(P3, P2))
PromotionCombo(List(P2, P1))
-----------
P1 Combinations:
PromotionCombo(List(P1, P2))
PromotionCombo(List(P1, P4, P5))
-----------
P3 Combinations:
PromotionCombo(List(P3, P2))
PromotionCombo(List(P3, P4, P5))
-----------
P5 Combinations:
PromotionCombo(List(P5, P3, P4))
PromotionCombo(List(P5, P1, P4))
-----------
```




