import random
import uuid

import psycopg2
import json
import requests

token = "e79ddaf729eff7549d8d5cdfcd812eddcc1c0bfe"
base_url = "https://api.waqi.info/"
latlng = [
    52.254647, 12.705084,
    52.759636, 13.937280
]
map_query = "%smap/bounds/?token=%s&latlng=%s" % (base_url, token, ",".join(map(str, latlng)))
response = requests.get(map_query)
stations = json.loads(response.content.decode("utf-8"))["data"]
print(stations)


def push_stations_to_psql(stations):
    with psycopg2.connect(database='postgres', user='postgres', password='patolina94', host='localhost') as conn:
        with conn.cursor() as cursor:
            query = """
                            INSERT into 
                                recording_stations 
                                (id, name, latitude, longitude) 
                            VALUES 
                                (%(uid)s, %(uid)s, %(lat)s, %(lon)s);
                        """

            for station in stations:
                station["name"] = "Station %d" % (station["uid"])

            cursor.executemany(query, stations)

        conn.commit()

def push_air_quality_to_psql(extended_stations):
    with psycopg2.connect(database='postgres', user='postgres', password='patolina94', host='localhost') as conn:
        with conn.cursor() as cursor:
            query = """
                            INSERT into 
                                air_quality 
                                (timestamp, station_name, aqi_pm24, aqi_pm10, aqi_no2, aqi_co, aqi_so2, aqi_ozone, current_weather_conditions, air_quality_forecast, weather_forecast, station_id, aqi, id) 
                            VALUES 
                                (%(timestamp)s, %(station_name)s, %(aqi_pm24)s, %(aqi_pm10)s, %(aqi_no2)s, %(aqi_co)s, %(aqi_so2)s, %(aqi_ozone)s, %(current_weather_conditions)s, %(air_quality_forecast)s, %(weather_forecast)s, %(station_id)s, %(aqi)s, %(id)s);
                        """

            cursor.executemany(query, extended_stations)

        conn.commit()


#push_stations_to_psql(stations)

extended_station_data = []
for station in stations:
    station_query = "%sfeed/@%d/?token=%s" % (base_url, station["uid"], token)
    station_data = None
    while station_data is None:
        station_query_response = requests.get(station_query)
        station_data = json.loads(station_query_response.content.decode("utf-8"))["data"]

    station_datum = {
        "aqi": station_data["aqi"],
        "station_name": "Station %d" % station_data["idx"],
        "timestamp": station_data["time"]["s"],
        "aqi_pm24": station_data["iaqi"].get("pm25", {"v": -1})["v"],
        "aqi_pm10": station_data["iaqi"].get("pm10", {"v": -1})["v"],
        "aqi_no2": station_data["iaqi"].get("no2", {"v": -1})["v"],
        "aqi_co": station_data["iaqi"].get("co", {"v": -1})["v"],
        "aqi_so2": station_data["iaqi"].get("so2", {"v": -1})["v"],
        "aqi_ozone": station_data["iaqi"].get("ozone", {"v": -1})["v"],
        "current_weather_conditions": "",
        "air_quality_forecast": "",
        "station_id": station_data["idx"],
        "weather_forecast": "",
        "id": random.randint(0, 8888888)
    }

    extended_station_data.append(station_datum)


push_air_quality_to_psql(extended_station_data)





