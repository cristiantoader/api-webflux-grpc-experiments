Benchmarking results

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