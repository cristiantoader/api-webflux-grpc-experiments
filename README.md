# API Benchmarking results 

## Part 1 - initial results gpb vs json formats

Surprising results with GPB format underperforming String JSONs in HTTP/2 Flux response regardless of GPB compression.

```
-- Meters ----------------------------------------------------------------------
gpb_person_bytes_per_second
             count = 103396054
         mean rate = 2881112.58 events/second
     1-minute rate = 2202801.66 events/second
     5-minute rate = 1762904.55 events/second
    15-minute rate = 1670774.65 events/second
gpb_person_entries_per_second
             count = 1659116
         mean rate = 46230.52 events/second
     1-minute rate = 35629.14 events/second
     5-minute rate = 28773.73 events/second
    15-minute rate = 27335.10 events/second
json_person_bytes_per_second
             count = 344796422
         mean rate = 9573941.11 events/second
     1-minute rate = 7295568.75 events/second
     5-minute rate = 5826013.01 events/second
    15-minute rate = 5518456.72 events/second
json_person_entries_per_second
             count = 12972703
         mean rate = 360209.68 events/second
     1-minute rate = 276674.82 events/second
     5-minute rate = 222948.02 events/second
    15-minute rate = 211684.23 events/second
```

## Part 2 - bypassing spring gpb converter

Trying not to use the spring ProtobufHttpMessageConverter, results improve but still pure jackson json is x 3 faster.

```

-- Meters ----------------------------------------------------------------------
gpb_bytes_person_entries_per_second
             count = 8501919
         mean rate = 102411.74 events/second
     1-minute rate = 83074.46 events/second
     5-minute rate = 39503.16 events/second
    15-minute rate = 27108.71 events/second
gpb_person_entries_per_second
             count = 3466641
         mean rate = 41819.96 events/second
     1-minute rate = 37851.38 events/second
     5-minute rate = 29222.81 events/second
    15-minute rate = 26748.61 events/second
json_person_entries_per_second
             count = 26582990
         mean rate = 320680.77 events/second
     1-minute rate = 288529.25 events/second
     5-minute rate = 221047.51 events/second
    15-minute rate = 201583.12 events/second
```

## Part 3 - adding grpc service

Added results in comparison with netty / grpc service which should perform best, but probably due to a 
programming/setup error it does not.

```
-- Meters ----------------------------------------------------------------------
grpc_gpb_person_entries_per_second
             count = 1860757
         mean rate = 20053.43 events/second
     1-minute rate = 17866.71 events/second
     5-minute rate = 11080.06 events/second
    15-minute rate = 9055.78 events/second
webflux_gpb_bytes_person_entries_per_second
             count = 2375197
         mean rate = 25541.59 events/second
     1-minute rate = 22700.77 events/second
     5-minute rate = 12304.94 events/second
    15-minute rate = 9290.62 events/second
webflux_gpb_person_entries_per_second
             count = 2726218
         mean rate = 29361.61 events/second
     1-minute rate = 27515.69 events/second
     5-minute rate = 18769.54 events/second
    15-minute rate = 16325.59 events/second
webflux_json_person_entries_per_second
             count = 19226023
         mean rate = 207062.03 events/second
     1-minute rate = 188441.50 events/second
     5-minute rate = 109511.57 events/second
    15-minute rate = 87130.15 events/second
```

### Part 4 - fixing the json webflux bug


The flux returned from the spring webflux server was not actually complete. Probably iterable responses 
are further split by spring in the response for improved buffering.

```
-- Meters ----------------------------------------------------------------------
grpc_gpb_person_entries_per_second
             count = 1012135
         mean rate = 19550.27 events/second
     1-minute rate = 15468.34 events/second
     5-minute rate = 11249.93 events/second
    15-minute rate = 10267.80 events/second
webflux_gpb_bytes_person_entries_per_second
             count = 1193870
         mean rate = 22981.75 events/second
     1-minute rate = 20650.97 events/second
     5-minute rate = 18288.15 events/second
    15-minute rate = 17748.43 events/second
webflux_gpb_person_entries_per_second
             count = 1277971
         mean rate = 24656.01 events/second
     1-minute rate = 22537.76 events/second
     5-minute rate = 20311.68 events/second
    15-minute rate = 19805.18 events/second
webflux_json_person_entries_per_second
             count = 978810
         mean rate = 18883.65 events/second
     1-minute rate = 14460.00 events/second
     5-minute rate = 9834.88 events/second
    15-minute rate = 8774.91 events/second
```