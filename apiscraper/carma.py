import random
import uuid

import psycopg2
import json
import requests

location_search_url = "http://carma.org/api/1.1/searchLocations?name=%s"

locations = ["Berlin", "Potsdam"]
location_ids = []

for location in locations:
    location_id = json.loads(requests.get(location_search_url % (location)).content.decode("utf-8"))[0]["id"]
    location_ids.append(location_id)

plant_query = "http://carma.org/api/1.1/searchPlants?location=%s"

plants = []
for location_id in location_ids:
    plant_query_result = requests.get(plant_query % (location_id))
    plant_data = json.loads(plant_query_result.content.decode("utf-8"))

    for plant in plant_data:
        plants.append(
            {
                "lon": plant["location"]["longitude"],
                "lat": plant["location"]["latitude"],
                "name": plant["name"],
                "tons_co2": plant["carbon"]["present"],
                "id": int(plant["id"])
            }
        )
print(plants)



def push_coal_plants_to_psql(plants):
    with psycopg2.connect(database='postgres', user='postgres', password='patolina94', host='localhost') as conn:
        with conn.cursor() as cursor:
            query = """
                            INSERT into 
                                coal_power_plant 
                                (id, name, latitude, longitude, tons_co2) 
                            VALUES 
                                (%(id)s, %(name)s, %(lat)s, %(lon)s, %(tons_co2)s);
                        """

            cursor.executemany(query, plants)

        conn.commit()

push_coal_plants_to_psql(plants)