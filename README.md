# CNN news in trends

Server will listen on 8080 port and expose next resources:

### List of all aggregated CNN news
```bash
curl localhost:8080/news
```

### List of all aggregated Google trends
```bash
curl localhost:8080/trends
```

### List of all CNN news in trends
```bash
curl localhost:8080/news-in-trends
```

### To run application execute the following command
```bash
gradle run
```