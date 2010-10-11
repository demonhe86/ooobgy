drop table weight_record;
create external table weight_record
(
    caller string,
    reciever string,
    call_count integer,
    call_time integer,
    weight integer
)
partitioned by(ds string)
row format delimited fields terminated by '\001' lines terminated by '\n'
stored as textfile
location '/group/taobao/ooobgy/call_record/tables/weight_record';