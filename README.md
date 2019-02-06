# Simple Akka Cralwer

Project is just a small application using [Akka](https://www.lightbend.com/akka) framework (Java API).

## Introduction

Application uses Akka's Actors to read latest news from Polish RealMadrid's fanclub website.

## Usage

To run an application, use Maven build manager (or using your favourite IDE).

Running the following command builds and executes an application:
```cmd
mvn clean compile exec:exec
```

If you want to run tests as well, use the following command:
```cmd
mvn clean test exec:exec
```

Application's run sample result should look like this:
```
AkkaCrawlerApp Retrieved result: 
	News(title=Vinícius na celowniku reprezentacji Brazylii, published=2019-02-06T11:50, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86883&kom=ok)
	News(title=Valverde: Real zazwyczaj na Camp Nou rozgrywa dobre mecze, published=2019-02-06T11:03, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86871&kom=ok)
	News(title=Kadra na Barcelonę, published=2019-02-06T10:22, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86880&kom=ok)
	News(title=Bale: Nie rozmawiałem z Zidane'em od finału w Kijowie, published=2019-02-06T09:51, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86873&kom=ok)
	News(title=Real poprosi o zmianę terminu meczu z Levante, published=2019-02-06T09:11, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86882&kom=ok)
	News(title=Misja specjalna dla Lucasa, published=2019-02-06T08:29, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86855&kom=ok)
	News(title=Solari: Oczekujemy naszego wielkiego meczu i zwycięstwa, published=2019-02-06T07:42, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86875&kom=ok)
	News(title=Dzisiejsze okładki, published=2019-02-06T00:47, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86879&kom=ok)
	News(title=Gol miesiąca – podsumowanie, published=2019-02-05T22:30, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86850&kom=ok)
	News(title=Solari zabierze do Barcelony 21 zawodników, published=2019-02-05T21:39, permalink=http://www.realmadryt.pl/index.php?co=aktualnosci&id=86869&kom=ok)
```