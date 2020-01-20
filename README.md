# StormBrendanSpringBootProject

Setting up
------------
git clone this project 
do a mvn clean install -U
run the program from main class StormBrendanApplication

Timestamp is in the format: dd-MM-yyyy HH:mm:ss

20 random workorders are created when the program runs

  
## REST API Calls (Add -v for more info on calls)

### Get all work orders
    curl -X GET localhost:8080/work-orders
    
### Post a new work order
    curl -X POST localhost:8080/work-orders -H 'Content-type:application/json' -d '{"id": "1234", "date": "12-2-1912        12:12:12"}'
    
### Get a specific work order by id (Pass in an id)
    curl -X GET localhost:8080/work-orders/{id} 
    
### Get the position of a work order in the queue (Pass in an id)
    curl -X GET localhost:8080/work-orders/position/{id} 
    
### Get the avergae wait time of a work order (Pass in a timestamp)
    curl -X GET localhost:8080/work-orders/averageWaitTime -H 'Content-type:application/json' -d '01-01-2020 12:12:12'

### Delete a specific work order (Pass in an id)
    curl -X DELETE localhost:8080/work-orders/delete/{id}
    
### Delete a the top work order in the queue
    curl -X DELETE localhost:8080/work-orders/delete/top
    
	
