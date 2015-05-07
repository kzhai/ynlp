#!/bin/bash

YNLP_HOME=$HOME/Workspace/ynlp
YNLP_LIB_HOME=$YNLP_HOME/lib

REVERB_ENTRY_CLASS=edu.washington.cs.knowitall.util.CommandLineReVerb

if [ $# != 1 ]; then
    echo "usage: run_jar.sh input_file"
	#cd $YNLP_LIB_HOME
	#java -cp "*" $REVERB_ENTRY_CLASS -h
    exit
fi

INPUT_FILE=$(grealpath $1)

#echo 'input file =' $INPUT_FILE

cd $YNLP_LIB_HOME

java -cp "*" $REVERB_ENTRY_CLASS --quiet $INPUT_FILE
