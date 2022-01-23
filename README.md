# Traces, metrics and exemplars

This repository shows how to setup Examplars in a Scala project, from the source code until the entire setup of Grafana, Prometheus and Tempo in a docker-compose.


Examplars are a link between metrics and traces.


## Run the project
# Create the traces-metrics-exemplars docker image
```aidl
sbt docker
```

# To test the image
```aidl
docker run -i com.lunatech/lunatech-traces-metrics-exemplars:v0.0.1
```

# Run the docker-compose
```aidl
docker-compose -f docker/docker-compose.yml up
```

# See the Grafana dashboard
Grafana will be available at `localhost:3000`. There is a dashboard already saved that can be accessed in:
```aidl
http://localhost:3000/d/HqWCSkb7k/lunatech-candy?orgId=1&from=now-15m&to=now
```


