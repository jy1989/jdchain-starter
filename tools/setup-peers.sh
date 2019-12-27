#!/bin/bash
PEER_FOLDER=jdchain-peer-1.1.2.RELEASE
PEERS_AMOUNT=4
MANAGER_PORT_START=8000
PEER_PORT_START=7080
mkdir peers
for ((i=0; i<$PEERS_AMOUNT; i ++))
do
    echo $i
    newFolder=peers/peer$i
    cp -r $PEER_FOLDER $newFolder
    mPort=`expr $MANAGER_PORT_START + $i`
    pPort=`expr $PEER_PORT_START + $i`
    sed -i "" 's/7080/'"${pPort}"'/g' $newFolder/bin/peer-startup.sh 
    sed -i "" 's/8000/'"${mPort}"'/g' $newFolder/bin/manager-startup.sh
done

echo 'done'