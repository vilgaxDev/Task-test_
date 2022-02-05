# Read Me 

Pull Dependencies and Run
```cmd
mvn  install
mvn spring-boot:run
```



1. Register Drone

    ```bash
    curl --location --request POST 'http://localhost:8081/api/drone' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "model": "LIGHT_WEIGHT",
        "weightLimit": 500,
        "batteryCapacity": 100
    }'
    ```

2. Load Drone with Medication
    
    ```bash
    curl --location --request PUT 'http://localhost:8081/api/drone/1' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "name": "drone1",
        "weight": 500,
      
        "image": "shorturl.at/fiAEM"
    }'
    ```

3. Available Drones
    ```bash
    curl --location --request GET 'http://localhost:8081/api/drone/available'
    ```

4. Get Drone or Get Drone battery
    ```bash
    curl --location --request GET 'http://localhost:8081/api/drone/1'
    ```

5. Swagger UI
    ```bash
    curl --location --request GET 'http://localhost:8081/swagger-ui/#/'
    ```
   ![](E:\javas\drone-project\screen shots\qwqw.PNG)
![](E:\javas\drone-project\screen shots\basic-error-controller.PNG)
![](E:\javas\drone-project\screen shots\drone-controller.PNG)
![](E:\javas\drone-project\screen shots\Models.PNG)
![](E:\javas\drone-project\screen shots\DroneDTORes.PNG)