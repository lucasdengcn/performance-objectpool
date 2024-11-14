gradle clean build shadowJar
# pool 0,1,2
java -Xms512M -Xmx1024M -Dobject=small -Dpool=fop -Dduration=2 -Dconcurrent=0 -XX:StartFlightRecording=duration=2m,filename=fop-small.jfr -jar build/libs/performance-objectpool-1.0-SNAPSHOT-all.jar
