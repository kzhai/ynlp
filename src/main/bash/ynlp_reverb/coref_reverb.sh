#!/bin/bash

YNLP_HOME=$HOME/Workspace/ynlp
YNLP_LIB_HOME=$YNLP_HOME/lib
YNLP_TARGET_CLASS_HOME=$YNLP_HOME/target/classes

REVERB_ENTRY_CLASS=edu.washington.cs.knowitall.util.CommandLineReVerb
REPLACE_LONGEST_COREF_CHAIN=com.yahoo.ynlp.coref.ReplaceLongestCorefChain

if [ $# != 1 ]; then
    echo "usage: coref_reverb.sh input_file"
    exit
fi

INPUT_FILE=$(grealpath $1)

echo 'input file =' $INPUT_FILE

TEMP_INPUT_FILE=$INPUT_FILE.temp

java -cp "$YNLP_LIB_HOME/*:$YNLP_TARGET_CLASS_HOME/" $REPLACE_LONGEST_COREF_CHAIN $INPUT_FILE $TEMP_INPUT_FILE

java -cp "$YNLP_LIB_HOME/*" $REVERB_ENTRY_CLASS --quiet $TEMP_INPUT_FILE | cut -f16,17,18

rm $TEMP_INPUT_FILE