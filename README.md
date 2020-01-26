# Wolt summer 2020 REST API
*This repository is a submission to [Wolt 2020 Engineering Pre-assignment](https://github.com/woltapp/summer2020). The REST API is implemented with Scalatra and Flask.*

## Building & Running
Easiest way to build and run the project is to use the Dockerized deploy version, which is generated from the sources.

    $ docker-compose up --build
The API is then reachable at http://127.0.0.1:8000/

<br/>

Alternatively, you can run the project using Python and sbt:

    $ pip install -r /HashAPI/requirements.txt
    $ python /HashAPI/flaskapi.py
    $ sbt
    jetty:start
The API is then reachable at http://127.0.0.1:8080/

## REST API Specification
The API implements two main features: search and hash. Search is used to filter down the restaurants with given parameters, and hash is used to rehash given restaurants' url images with [Wolt blurhash-python](https://github.com/woltapp/blurhash-python).

All returns are done in JSON format.

### `/`
#### Returns:
 - Status 302: The main page redirects user to `/restaurants`.

<br/>
 
### `/restaurants`
#### Returns:
 - Status 200 - All loaded restaurants in list.
 
<br/>

### `/restaurants/search`
#### Parameters:

-   _q_: query string. Full or partial match for the string is searched from _name_, _description_ and _tags_ fields. A minimum length for the query string is one character.
-   _lat_: latitude coordinate (customer's location)
-   _lon_ : longitude coordinate (customer's location)

#### Example query:
```
/restaurants/search?q=sushi&lat=60.17045&lon=24.93147
```

#### Returns:

 - Status 200 - A list of restaurants. 
 - Status 400 - With invalid parameters returns error message.

<br/>

### `/restaurants/hash`
#### Parameters:

-   _q_: query string. Full or partial match for the string is searched from _name_, _description_ and _tags_ fields. A minimum length for the query string is one character.
-   _lat_: latitude coordinate (customer's location)
-   _lon_ : longitude coordinate (customer's location)
- _x_comp_ and _y_comp_: adjust the amount of vertical and horizontal AC components in hashed image. Both parameters must be `>= 1` and `<= 9`

#### Example query:
```
/restaurants/hash?q=sushi&lat=60.17045&lon=24.93147&x_comp=3&y_comp=4
```

#### Returns:

 - Status 200 - A tuple list of urls and corresponding hashes.
 - Status 400 - With invalid parameters returns error message.


## Testing
Testing is done automatically while building the Docker-compose. Alternatively you can manually run tests with sbt:

    $ sbt test
To test the hashAPI from sbt you must ensure that the flask API is reachable at `127.0.0.1` and then change the tests from `ignore` to `test`.
