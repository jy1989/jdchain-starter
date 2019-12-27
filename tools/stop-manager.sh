#!/bin/bash
HOME=`pwd`
PEERS_AMOUNT=4
SCRIPT_FILE=manager-shutdown.sh
for ((i=0; i<$PEERS_AMOUNT; i ++))
do
    echo $HOME
    cd $HOME
    echo $i
    newFolder=peers/peer$i
    cd $newFolder/bin/
    chmod 777 $SCRIPT_FILE
    ./$SCRIPT_FILE
done

echo 'done'