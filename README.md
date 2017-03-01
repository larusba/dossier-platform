# dossier-platform
A sample project to test bigdata infrastructure tecnologies.

The goal of this project is to create a big data platform for web-news analysis.

## crawler
The purpose of a "crawler" is to seek and extract information from news sites. 
The information, such as "title" and "body" are send into a Kafka topic. Nowadays there is only one fake crawler: "mock-crawler".

## data
The "data" directory contains some JSON files with examples of messages that the crawlers have to send.

## mock-crawler
It's a Kafka client, written in java, that sends to Kafka the content of files into a directory.

Example:
```
it.larus.bigdata.dossier.crawler.mock.FileMockCrawler localhost:9092 news-topic ./data/
```

## docker
The docker project contains a composition file that create the enviroment for application testing.
```
cd docker
docker-compose up
```
It enables to use kafka on port 9092 with zookeeper on 2181. Inside Kafka a topic is created with name "news-topic".

It also creates a neo4j instance with http (7474) and BOLT (7687) protocols enabled.
```
username: neo4j
password: admin
data: $HOME/dossier/neo4j/
```

## flink-analyzer
It's a Kafka consumer, write in Java (only a printing feature) and in Scala. 
The goal of project is to set up a Neo4j database of news, linking them with people or compagny cited in the news.
