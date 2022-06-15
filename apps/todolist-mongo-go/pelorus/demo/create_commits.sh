counter=1
sleep_time=30

while [ $counter -le 20 ]
do
   echo "Bug fix ${counter}" >> README.md
   git add .
   git commit -a -m "Bug fix ${counter}"
   git push
   counter=$(( $counter + 1 ))
   sleep $sleep_time
done

