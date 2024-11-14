gradle clean build shadowJar

java -Xms512M -Xmx1024M -Dobject=small -Dpool=simple2 -Dduration=2 -Dconcurrent=1000 -XX:StartFlightRecording=duration=2m,filename=simple2-small.jfr -jar build/libs/performance-objectpool-1.0-SNAPSHOT-all.jar
