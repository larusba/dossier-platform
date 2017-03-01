 CREATE CONSTRAINT ON (site:Site) ASSERT site.url IS UNIQUE
 CREATE CONSTRAINT ON (author:Author) ASSERT author.name IS UNIQUE
 CREATE CONSTRAINT ON (name:Name) ASSERT name.name IS UNIQUE
 
 // MERGE (site:Site {url: {site_url}})
 // MERGE (author:Author {name: {author_name}})
 // MERGE (name:Name {name: {name}})
 // CREATE (news:News {url: {news_url}, title: {news_title}})
 // CREATE (news)-[:PUBLISHED_ON]->(site)
 // CREATE (author)-[:WRITES]->(news)
 // CREATE (name)-[:CITED_IN]->(news)



